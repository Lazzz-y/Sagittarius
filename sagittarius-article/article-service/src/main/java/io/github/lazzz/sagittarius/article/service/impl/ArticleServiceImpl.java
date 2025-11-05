package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.article.model.entity.Article;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.lazzz.sagittarius.article.service.UserCacheService;
import io.github.lazzz.sagittarius.article.utils.HtmlWordsCountUtil;
import io.github.lazzz.sagittarius.article.utils.MarkdownConvertUtils;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;


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
        return articleMetaService.saveArticleMeta(meta);
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
}

