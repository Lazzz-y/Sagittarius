package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
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
     * 保存文章元数据
     *
     * @param form 文章元数据表单
     * @return {@link Boolean} 是否保存成功
     */
    Boolean saveArticleMeta(ArticleMetaForm form);

    /**
     * 审批文章
     * @param id 文章元数据ID
     */
    Boolean approveArticle(Serializable id);

    /**
     * 根据文章ID获取文章元数据VO
     *
     * @param id 文章元数据ID
     * @return 文章元数据VO
     */
    ArticleMetaVO getArticleMetaByArticleId(Serializable id);




}