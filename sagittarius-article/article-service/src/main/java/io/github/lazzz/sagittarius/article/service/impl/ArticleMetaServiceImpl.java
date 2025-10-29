package io.github.lazzz.sagittarius.article.service.impl;


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
    public ArticleMetaVO getArticleMetaByArticleId(Serializable id) {
        ArticleMeta articleMeta = this.getById(id);
        Assert.isTrue(articleMeta != null, "文章元数据不存在");
        return converter.convert(articleMeta, ArticleMetaVO.class);
    }

}