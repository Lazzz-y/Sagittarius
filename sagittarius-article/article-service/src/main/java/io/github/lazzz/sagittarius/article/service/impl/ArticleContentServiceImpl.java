package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.sagittarius.article.model.entity.Article;
import io.github.lazzz.sagittarius.article.service.ArticleContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * 文章内容服务实现类
 * 负责处理文章内容的存储、检索等操作
 * 
 * @author Lazzz 
 * @date 2025/10/22 13:10
**/
@Service
@RequiredArgsConstructor
public class ArticleContentServiceImpl implements ArticleContentService {

    private final MongoTemplate mongoTemplate;

    @Override
    public void saveNewArticle(Article article) {
        // 保存文章内容到数据库
        mongoTemplate.save(article);
    }

    @Override
    public Article getArticleContentById(String id) {
        return mongoTemplate.findById(id, Article.class);
    }
}

