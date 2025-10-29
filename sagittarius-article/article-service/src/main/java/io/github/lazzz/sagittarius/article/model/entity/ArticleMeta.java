package io.github.lazzz.sagittarius.article.model.entity;

import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.github.lazzz.sagittarius.common.base.BaseSnowflakeLogicEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * 文章主表（元数据） 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文章主表（元数据）")
@EqualsAndHashCode(callSuper = true)
@Table(value = "article_meta")
@AutoMappers(
        value = {
                @AutoMapper(target = ArticleMetaVO.class)
        }
)
public class ArticleMeta extends BaseSnowflakeLogicEntity {


    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    @Column(value = "title")
    private String title;

    /**
     * 文章摘要（可选，用于列表页展示）
     */
    @Schema(description = "文章摘要（可选，用于列表页展示）")
    @Column(value = "summary")
    private String summary;

    /**
     * 作者ID（关联用户表）
     */
    @Schema(description = "作者ID（关联用户表）")
    @Column(value = "author_id")
    private Long authorId;

    /**
     * 分类ID（关联分类表）
     */
    @Schema(description = "分类ID（关联分类表）")
    @Column(value = "category_id")
    private Long categoryId;

    /**
     * 状态：0-草稿 1-待审核 2-审核通过 3-驳回
     */
    @Schema(description = "状态：0-草稿 1-待审核 2-审核通过 3-驳回")
    @Column(value = "status")
    private Integer status;

    /**
     * 阅读量
     */
    @Schema(description = "阅读量")
    @Column(value = "view_count", onInsertValue = "0")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    @Column(value = "like_count", onInsertValue = "0")
    private Integer likeCount;

    /**
     * 评论数
     */
    @Schema(description = "评论数")
    @Column(value = "comment_count", onInsertValue = "0")
    private Integer commentCount;

    /**
     * 是否推荐：0-否 1-是
     */
    @Schema(description = "是否推荐：0-否 1-是")
    @Column(value = "is_recommended", onInsertValue = "0")
    private Integer isRecommended;

    /**
     * 关联MongoDB的文档ID（存储正文）
     */
    @Schema(description = "关联MongoDB的文档ID（存储正文）")
    @Column(value = "mongo_doc_id")
    private String mongoDocId;

    /**
     * 提交审核时间
     */
    @Schema(description = "提交审核时间")
    @Column(value = "submit_audit_time")
    private Date submitAuditTime;

    /**
     * 审核通过（发布）时间
     */
    @Schema(description = "审核通过（发布）时间")
    @Column(value = "publish_time")
    private Date publishTime;

}
