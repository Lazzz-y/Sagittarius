package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;

import java.io.Serializable;

/**
 * 文章主表（元数据） 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface IArticleMetaService extends IService<ArticleMeta> {

    /**
     * 根据文章ID获取文章元数据VO
     *
     * @param id 文章元数据ID
     * @return 文章元数据VO
     */
    ArticleMetaVO getArticleMetaByArticleId(Serializable id);




}