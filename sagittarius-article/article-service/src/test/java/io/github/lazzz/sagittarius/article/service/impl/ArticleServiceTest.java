package io.github.lazzz.sagittarius.article.service.impl;


import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/10/25 14:16
**/
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private IArticleService articleService;

    @Test
    void getArticleById() {
        TenantContext.setTenantId(1L);
        ArticleVO articleVO = articleService.getArticleContentById(336316065288175717L);
        System.out.println(articleVO);
    }

}

