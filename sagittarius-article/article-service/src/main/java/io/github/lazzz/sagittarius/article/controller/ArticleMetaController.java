package io.github.lazzz.sagittarius.article.controller;

import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.common.web.annotation.PreventDuplicateResubmit;
import lombok.RequiredArgsConstructor;
import com.mybatisflex.core.paginate.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 文章主表（元数据） 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/article/meta")
@Tag(name = "文章主表（元数据）控制层")
@RequiredArgsConstructor
public class ArticleMetaController {

    private final IArticleMetaService articleMetaService;

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批文章")
    @PreventDuplicateResubmit
    @PreAuthorize("ss.hasAnyPerm('article:approve')")
    public Result<Boolean> approveArticle(@PathVariable Serializable id) {
        return Result.success(articleMetaService.approveArticle(id));
    }


}