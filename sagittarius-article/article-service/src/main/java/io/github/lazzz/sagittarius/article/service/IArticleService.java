package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.request.form.ArticleForm;
import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;

import java.io.Serializable;

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
}

