package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.request.form.ArticleForm;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.model.vo.EditHistoryVO;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/10/22 13:09
**/
public interface IArticleService {

    /**
     * 保存文章内容
     * @param form 文章实体类
     */
    Boolean saveNewArticle(ArticleForm form);

     /**
     * 更新文章内容
     * @param form 文章实体类
     */
    Boolean updateArticle(ArticleForm form);

    /**
     * 根据文章元数据 ID 获取文章内容
     * @param id 文章元数据 ID
     * @return {@link ArticleVO} 文章内容视图实体类
     */
    ArticleVO getArticleByMetaId(Serializable id);

    /**
     * 根据Mongo文档ID获取文章内容
     * @param mongoDocId Mongo文档ID
     * @return 文章内容视图实体类
     */
    ArticleVO getArticleByMongoDocId(String mongoDocId);

    /**
     * 获取文章编辑历史
     * @param id 文章元数据ID
     * @return 编辑历史列表
     */
    List<EditHistoryVO> getEditHistoryByArticleId(Serializable id);
}

