package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import io.github.lazzz.sagittarius.article.mapper.ArticleMetaMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.util.Assert;

import java.io.Serializable;

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
    public Boolean approveArticle(Serializable id) {
        Assert.notNull(id, "文章元数据ID不能为空");
        return updateChain().set(ArticleMeta::getStatus, 1).where(ArticleMeta::getId).eq(id).update();
    }

    @Override
    public ArticleMetaVO getArticleMetaByArticleId(Serializable id) {
        Assert.notNull(id, "文章ID不能为空");
        ArticleMeta articleMeta = this.getById(id);
        Assert.isTrue(articleMeta != null, "文章元数据不存在");
        return converter.convert(articleMeta, ArticleMetaVO.class);
    }

}