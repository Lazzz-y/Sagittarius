package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.entity.Article;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/10/22 13:09
**/
public interface ArticleContentService {

    /**
     * 保存文章内容
     * @param article 文章实体类
     */
    void saveNewArticle(Article article);

    /**
     * 根据文章 ID 获取文章内容
     * @param id 文章 ID
     * @return {@link Article} 文章内容实体类
     */
    Article getArticleContentById(String id);
}

