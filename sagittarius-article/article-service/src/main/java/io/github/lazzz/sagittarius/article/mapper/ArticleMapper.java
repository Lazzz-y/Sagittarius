package io.github.lazzz.sagittarius.article.mapper;


import io.github.lazzz.sagittarius.article.model.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 文章 MongoDB
 * 
 * @author Lazzz 
 * @date 2025/10/23 22:50
**/
public interface ArticleMapper extends MongoRepository<Article, String> {
}

