package io.github.lazzz.sagittarius.article.model.request.form;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * 文章元数据视图层
 * 
 * @author Lazzz 
 * @date 2025/10/25 13:09
**/
@Data
public class ArticleMetaForm {

    /**
     * 文章元数据ID
     */
    private Long id;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * 文章摘要（可选，用于列表页展示）
     */
    @Schema(description = "文章摘要（可选，用于列表页展示）")
    private String summary;

    /**
     * 作者ID（关联用户表）
     */
    @Schema(description = "作者ID（关联用户表）")
    private Long authorId;

    /**
     * 分类ID（关联分类表）
     */
    @Schema(description = "分类ID（关联分类表）")
    private Long categoryId;

    /**
     * 状态：0-草稿 1-待审核 2-审核通过 3-驳回
     */
    @Schema(description = "状态：0-草稿 1-待审核 2-审核通过 3-驳回")
    private Integer status;

    /**
     * 阅读量
     */
    @Schema(description = "阅读量")
    private Integer viewCount = 0;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    private Integer likeCount = 0;

    /**
     * 评论数
     */
    @Schema(description = "评论数")
    private Integer commentCount = 0;

    /**
     * 是否推荐：0-否 1-是
     */
    @Schema(description = "是否推荐：0-否 1-是")
    private Integer isRecommended = 0;

    /**
     * 关联MongoDB的文档ID（存储正文）
     */
    @Schema(description = "关联MongoDB的文档ID（存储正文）")
    private String mongoDocId;

    /**
     * 提交审核时间
     */
    @Schema(description = "提交审核时间")
    private Date submitAuditTime;

    /**
     * 审核通过（发布）时间
     */
    @Schema(description = "审核通过（发布）时间")
    private Date publishTime;


}

