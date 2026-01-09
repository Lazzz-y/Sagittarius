package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.article.event.ArticleSubmitEvent;
import io.github.lazzz.sagittarius.article.model.entity.Article;
import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import io.github.lazzz.sagittarius.article.model.entity.EditHistory;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.model.vo.EditHistoryVO;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.lazzz.sagittarius.article.service.UserCacheService;
import io.github.lazzz.sagittarius.article.utils.HtmlWordsCountUtil;
import io.github.lazzz.sagittarius.article.utils.MarkdownConvertUtils;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 文章内容服务实现类
 * 负责处理文章内容的存储、检索等操作
 * 
 * @author Lazzz 
 * @date 2025/10/22 13:10
**/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {

    private final MongoTemplate mongoTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final IArticleMetaService articleMetaService;

    private final UserCacheService userCacheService;

    private final Converter converter;

    @Override
    @Transactional
    public Boolean saveNewArticle(ArticleForm form) {
        var html = MarkdownConvertUtils.md2Html(form.getContentMarkdown());
        var article = converter.convert(form, Article.class);
        var wordCount = HtmlWordsCountUtil.countHtmlTextLength(html);
        var meta = form.getMeta();
        var date = new Date(System.currentTimeMillis());
        article.setWordCount(wordCount);
        article.setContentHtml(html);
        article.setCreateTime(date);
        article.setUpdateTime(date);
        article.setVersion(1);
        mongoTemplate.save(article);
        meta.setAuthorId(SecurityUtils.getUserId());
        meta.setMongoDocId(article.getId());
        meta.setViewCount(0);
        meta.setLikeCount(0);
        meta.setCommentCount(0);
        meta.setIsRecommended(0);
        if (meta.getStatus() == 0) {
            meta.setSubmitAuditTime(date);
        }
        var rs = articleMetaService.saveArticleMeta(meta);
        if (rs) {
            // 发布文章事件
            var event = new ArticleSubmitEvent();
            event.setTenantId(TenantContext.getTenantId());
            event.setArticleEs(converter.convert(article, ArticleEs.class));
            event.setEventId(UUID.randomUUID());
            rabbitTemplate.convertAndSend(MQConstants.QUEUE_ARTICLE_PUBLISH, event);
        }
        return rs;
    }

    @Override
    @Transactional
    public Boolean updateArticle(ArticleForm form) {
        Assert.notNull(form.getId(), "文章ID不能为空");
        Assert.notNull(form.getMeta(), "文章元数据不能为空");

        // 获取现有文章
        var existingArticle = mongoTemplate.findById(form.getId(), Article.class);
        Assert.notNull(existingArticle, "文章不存在");

        // 转换Markdown为HTML并统计字数
        var html = MarkdownConvertUtils.md2Html(form.getContentMarkdown());
        var wordCount = HtmlWordsCountUtil.countHtmlTextLength(html);

        // 记录编辑历史
        var newVersion = existingArticle.getVersion() + 1;
        var editHistory = existingArticle.getEditHistory();
        if (editHistory == null) {
            editHistory = new java.util.ArrayList<>();
        }
        var history = new EditHistory();
        history.setVersion(existingArticle.getVersion());
        history.setEditorId(SecurityUtils.getUserId());
        history.setEditTime(new Date());
        history.setChangeLog("编辑文章内容");
        editHistory.add(history);

        // 保留最近3次编辑历史
        if (editHistory.size() > 3) {
            editHistory = editHistory.subList(editHistory.size() - 3, editHistory.size());
        }

        // 更新文章
        var article = converter.convert(form, Article.class);
        article.setWordCount(wordCount);
        article.setContentHtml(html);
        article.setUpdateTime(new Date(System.currentTimeMillis()));
        article.setVersion(newVersion);
        article.setEditHistory(editHistory);

        mongoTemplate.save(article);

        return true;
    }

    @Override
    public ArticleVO getArticleByMetaId(Serializable id) {
        ArticleMetaVO articleMetaVO = articleMetaService.getArticleMetaByArticleId(id);
        var mongoId = articleMetaVO.getMongoDocId();
        Article article = mongoTemplate.findById(mongoId, Article.class);
        Assert.isTrue(article != null, "文章不存在");
        var authorId = articleMetaVO.getAuthorId();
        var authorName = userCacheService.getUserNameById(authorId);
        articleMetaVO.setAuthorName(authorName);
        // 合并文章元数据和内容
        ArticleVO articleVO = converter.convert(article, ArticleVO.class);
        articleVO.setMeta(articleMetaVO);
        return articleVO;
    }

    @Override
    public ArticleVO getArticleByMongoDocId(String mongoDocId) {
        Article article = mongoTemplate.findById(mongoDocId, Article.class);
        Assert.notNull(article, "文章不存在");

        // 获取文章元数据
        var articleMetaVO = articleMetaService.queryChain()
                .from(ArticleMeta.class)
                .eq(ArticleMeta::getMongoDocId, mongoDocId)
                .oneAs(ArticleMetaVO.class);
        Assert.notNull(articleMetaVO, "文章元数据不存在");

        var authorName = userCacheService.getUserNameById(articleMetaVO.getAuthorId());
        articleMetaVO.setAuthorName(authorName);
        // 合并文章元数据和内容
        ArticleVO articleVO = converter.convert(article, ArticleVO.class);
        articleVO.setMeta(articleMetaVO);
        return articleVO;
    }

    @Override
    public List<EditHistoryVO> getEditHistoryByArticleId(Serializable id) {
        // 获取文章元数据
        ArticleMetaVO articleMetaVO = articleMetaService.getArticleMetaByArticleId(id);
        var mongoId = articleMetaVO.getMongoDocId();

        // 获取文章内容
        Article article = mongoTemplate.findById(mongoId, Article.class);
        Assert.notNull(article, "文章不存在");

        // 转换编辑历史
        if (article.getEditHistory() == null) {
            return List.of();
        }
        return article.getEditHistory().stream()
                .map(history -> converter.convert(history, EditHistoryVO.class))
                .collect(Collectors.toList());
    }
}

