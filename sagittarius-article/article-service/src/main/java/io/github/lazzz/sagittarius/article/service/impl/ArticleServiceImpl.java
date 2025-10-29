package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.sagittarius.article.model.entity.Article;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 文章内容服务实现类
 * 负责处理文章内容的存储、检索等操作
 * 
 * @author Lazzz 
 * @date 2025/10/22 13:10
**/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {

    private final MongoTemplate mongoTemplate;

    private final IArticleMetaService articleMetaService;

    private final Converter converter;

    @Override
    public void saveNewArticle(Article article) {
        // 保存文章内容到数据库
        mongoTemplate.save(article);
    }

    @Override
    public ArticleVO getArticleContentById(Serializable id) {
        ArticleMetaVO articleMetaVO = articleMetaService.getArticleMetaByArticleId(id);
        var mongoId = articleMetaVO.getMongoDocId();
        Article article = mongoTemplate.findById(mongoId, Article.class);
        Assert.isTrue(article != null, "文章不存在");
        // 合并文章元数据和内容
        ArticleVO articleVO = converter.convert(article, ArticleVO.class);
        articleVO.setMeta(articleMetaVO);
        return articleVO;
    }
}

