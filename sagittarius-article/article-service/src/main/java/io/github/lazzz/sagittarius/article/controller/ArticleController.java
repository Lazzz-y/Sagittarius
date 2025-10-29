package io.github.lazzz.sagittarius.article.controller;


import io.github.lazzz.sagittarius.article.model.entity.Article;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 文章控制器
 * 
 * @author Lazzz 
 * @date 2025/10/23 13:19
**/
@RestController
@RequiredArgsConstructor
@Tag(name = "01.文章接口")
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final IArticleService articleContentService;

    @GetMapping("/{id}")
    @Operation(summary = "根据元数据ID获取文章内容")
    public Result<ArticleVO> getArticleContentById(@PathVariable Serializable id) {
        return Result.success(articleContentService.getArticleContentById(id));
    }



}

