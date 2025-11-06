package io.github.lazzz.sagittarius.article.controller;


import io.github.lazzz.sagittarius.article.model.request.form.ArticleForm;
import io.github.lazzz.sagittarius.article.model.vo.ArticleVO;
import io.github.lazzz.sagittarius.article.service.IArticleService;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.common.web.annotation.PreventDuplicateResubmit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    private final IArticleService articleService;

    @PostMapping
    @Operation(summary = "创建文章")
    @PreventDuplicateResubmit
    @PreAuthorize("ss.hasAnyPerm('article:create')")
    public Result<Boolean> createArticle(@RequestBody ArticleForm form) {
        return Result.success(articleService.saveNewArticle(form));
    }

    @PutMapping
    @Operation(summary = "更新文章")
    @PreventDuplicateResubmit
    @PreAuthorize("ss.hasAnyPerm('article:update')")
    public Result<Boolean> updateArticle(@RequestBody ArticleForm form) {
        return Result.success();
    }


    @GetMapping("/{id}")
    @Operation(summary = "根据元数据ID获取文章内容")
    public Result<ArticleVO> getArticleContentById(@PathVariable Serializable id) {
        return Result.success(articleService.getArticleByMetaId(id));
    }

}

