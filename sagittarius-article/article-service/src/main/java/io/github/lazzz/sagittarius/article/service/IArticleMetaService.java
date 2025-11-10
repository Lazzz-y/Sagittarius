package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;

import java.io.Serializable;
import java.util.Map;

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

    /**
     * 分页查询文章元数据列表
     *
     * @param page 分页参数
     * @param params 查询条件（status, categoryId, authorId等）
     * @return 文章元数据VO分页结果
     */
    Page<ArticleMetaVO> getArticleMetaPage(Page<ArticleMeta> page, Map<String, Object> params);

    /**
     * 更新文章元数据
     *
     * @param id 文章元数据ID
     * @param form 文章元数据表单
     * @return 是否更新成功
     */
    Boolean updateArticleMeta(Serializable id, ArticleMetaForm form);

    /**
     * 删除文章
     *
     * @param id 文章元数据ID
     * @return 是否删除成功
     */
    Boolean deleteArticle(Serializable id);

    /**
     * 提交审核
     *
     * @param id 文章元数据ID
     * @return 是否提交成功
     */
    Boolean submitForReview(Serializable id);

    /**
     * 驳回文章
     *
     * @param id 文章元数据ID
     * @param reason 驳回原因
     * @return 是否驳回成功
     */
    Boolean rejectArticle(Serializable id, String reason);

    /**
     * 设置/取消推荐
     *
     * @param id 文章元数据ID
     * @param isRecommended 是否推荐（0-否 1-是）
     * @return 是否设置成功
     */
    Boolean setRecommended(Serializable id, Integer isRecommended);

}