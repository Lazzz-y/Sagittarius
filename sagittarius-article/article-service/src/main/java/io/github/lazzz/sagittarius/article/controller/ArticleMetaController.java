package io.github.lazzz.sagittarius.article.controller;

import io.github.lazzz.sagittarius.article.model.request.form.ArticleMetaForm;
import io.github.lazzz.sagittarius.common.web.annotation.PreventDuplicateResubmit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.bind.annotation.RequestParam;
import io.github.lazzz.sagittarius.article.service.IArticleMetaService;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/page")
    @Operation(summary = "分页查询文章列表")
    public Result<Page<ArticleMetaVO>> getArticleMetaPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Integer isRecommended) {

        Page<ArticleMeta> pageRequest = new Page<>(page, size);
        Map<String, Object> params = Map.of(
                "title", title,
                "status", status,
                "categoryId", categoryId,
                "authorId", authorId,
                "isRecommended", isRecommended
        );
        return Result.success(articleMetaService.getArticleMetaPage(pageRequest, params));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取文章元数据")
    public Result<ArticleMetaVO> getArticleMeta(@PathVariable Serializable id) {
        return Result.success(articleMetaService.getArticleMetaByArticleId(id));
    }

    @PostMapping
    @Operation(summary = "创建文章元数据")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasAnyPerm('article:create')")
    public Result<Boolean> createArticleMeta(@RequestBody ArticleMetaForm form) {
        return Result.success(articleMetaService.saveArticleMeta(form));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章元数据")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasAnyPerm('article:update')")
    public Result<Boolean> updateArticleMeta(@PathVariable@NotNull(message = "文章ID不能为空") Serializable id, @RequestBody ArticleMetaForm form) {
        return Result.success(articleMetaService.updateArticleMeta(id, form));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    @PreAuthorize("@ss.hasAnyPerm('article:delete')")
    public Result<Boolean> deleteArticle(@PathVariable@NotNull(message = "文章ID不能为空") Serializable id) {
        return Result.success(articleMetaService.deleteArticle(id));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批文章")
    @PreventDuplicateResubmit
    @PreAuthorize("ss.hasAnyPerm('article:approve')")
    public Result<Boolean> approveArticle(@PathVariable@NotNull(message = "文章ID不能为空") Serializable id) {
        return Result.success(articleMetaService.approveArticle(id));
    }

    @PutMapping("/{id}/submit")
    @Operation(summary = "提交审核")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasAnyPerm('article:submit')")
    public Result<Boolean> submitForReview(@PathVariable@NotNull(message = "文章ID不能为空") Serializable id) {
        return Result.success(articleMetaService.submitForReview(id));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "驳回文章")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasAnyPerm('article:reject')")
    public Result<Boolean> rejectArticle(
            @PathVariable@NotNull(message = "文章ID不能为空") Serializable id,
            @RequestParam@NotBlank(message = "驳回原因不能为空") String reason) {
        return Result.success(articleMetaService.rejectArticle(id, reason));
    }

    @PutMapping("/{id}/recommend")
    @Operation(summary = "设置/取消推荐")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasAnyPerm('article:recommend')")
    public Result<Boolean> setRecommended(@PathVariable Serializable id, @RequestParam Integer isRecommended) {
        return Result.success(articleMetaService.setRecommended(id, isRecommended));
    }
}