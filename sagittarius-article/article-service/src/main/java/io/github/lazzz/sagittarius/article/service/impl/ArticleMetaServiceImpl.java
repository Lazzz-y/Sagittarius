package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import io.github.lazzz.sagittarius.article.mapper.ArticleMetaMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.mybatisflex.core.paginate.Page;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 文章主表（元数据） 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ArticleMetaServiceImpl extends ServiceImpl<ArticleMetaMapper, ArticleMeta> implements IArticleMetaService {

    private final Converter converter;

    @Override
    public Boolean saveArticleMeta(ArticleMetaForm form) {
        ArticleMeta articleMeta = converter.convert(form, ArticleMeta.class);
        return this.save(articleMeta);
    }

    @Override
    @Transactional
    public Boolean approveArticle(Serializable id) {
        Assert.notNull(id, "文章元数据ID不能为空");

        // 获取文章元数据
        ArticleMeta articleMeta = this.getById(id);
        Assert.isTrue(articleMeta != null, "文章元数据不存在");

        // 更新状态为审核通过（2）
        boolean success = updateChain()
                .set(ArticleMeta::getStatus, 2)
                .set(ArticleMeta::getPublishTime, LocalDateTime.now())
                .where(ArticleMeta::getId).eq(id)
                .update();

        return success;
    }

    @Override
    public ArticleMetaVO getArticleMetaByArticleId(Serializable id) {
        Assert.notNull(id, "文章ID不能为空");
        ArticleMeta articleMeta = this.getById(id);
        Assert.isTrue(articleMeta != null, "文章元数据不存在");
        return converter.convert(articleMeta, ArticleMetaVO.class);
    }

    @Override
    public Page<ArticleMetaVO> getArticleMetaPage(Page<ArticleMeta> page, Map<String, Object> params) {
        // 构建查询条件
        var queryWrapper = this.queryChain().from(ArticleMeta.class)
                .like(ArticleMeta::getTitle, params.get("title"), params.containsKey("title"))
                .eq(ArticleMeta::getStatus, params.get("status"), params.containsKey("status"))
                .eq(ArticleMeta::getCategoryId, params.get("categoryId"), params.containsKey("categoryId"))
                .eq(ArticleMeta::getAuthorId, params.get("authorId"), params.containsKey("authorId"))
                .eq(ArticleMeta::getIsRecommended, params.get("isRecommended"), params.containsKey("isRecommended"))
                .orderBy(ArticleMeta::getUpdateAt, false);

        Page<ArticleMeta> rs = this.page(page, queryWrapper);
        return rs.map(item -> converter.convert(item, ArticleMetaVO.class));
    }

    @Override
    public Boolean updateArticleMeta(Serializable id, ArticleMetaForm form) {
        Assert.notNull(id, "文章ID不能为空");
        ArticleMeta articleMeta = this.getById(id);
        Assert.notNull(articleMeta, "文章元数据不存在");

        ArticleMeta updateMeta = converter.convert(form, ArticleMeta.class);
        updateMeta.setId(articleMeta.getId());

        return this.updateById(updateMeta);
    }

    @Override
    public Boolean deleteArticle(Serializable id) {
        Assert.notNull(id, "文章ID不能为空");
        return this.removeById(id);
    }

    @Override
    public Boolean submitForReview(Serializable id) {
        Assert.notNull(id, "文章ID不能为空");
        return updateChain()
                // 待审核
                .set(ArticleMeta::getStatus, 1)
                .set(ArticleMeta::getSubmitAuditTime, LocalDateTime.now())
                .where(ArticleMeta::getId).eq(id)
                .update();
    }

    @Override
    public Boolean rejectArticle(Serializable id, String reason) {
        Assert.notNull(id, "文章ID不能为空");
        // 状态3表示驳回
        return updateChain()
                .set(ArticleMeta::getStatus, 3)
                .where(ArticleMeta::getId).eq(id)
                .update();
    }

    @Override
    public Boolean setRecommended(Serializable id, Integer isRecommended) {
        Assert.notNull(id, "文章ID不能为空");
        return updateChain()
                .set(ArticleMeta::getIsRecommended, isRecommended)
                .where(ArticleMeta::getId).eq(id)
                .update();
    }

}