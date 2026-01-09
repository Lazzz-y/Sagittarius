package io.github.lazzz.sagittarius.article.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.DateFormat;

import java.util.Date;

/**
 * 文章 Elasticsearch 实体（用于全文检索）
 * 整合 ArticleMeta（元数据）和 Article（内容数据）的核心搜索字段
 *
 * @author Lazzz
 * @date 2025/11/26 20:31
 **/
@Data
@Document(indexName = "article_es")
public class ArticleEs {

    /**
     * 文章ID（与ArticleMeta的主键ID一致，雪花算法生成）
     */
    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    /**
     * 文章标题（用于标题检索）
     */
    @Field(name = "title", type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    /**
     * 文章摘要（用于摘要检索）
     */
    @Field(name = "summary", type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String summary;

    /**
     * Markdown原始内容（用于正文全文检索）
     */
    @Field(name = "content_markdown", type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String contentMarkdown;

    /**
     * 作者ID（用于按作者筛选）
     */
    @Field(name = "author_id", type = FieldType.Keyword)
    private Long authorId;

    /**
     * 分类ID（用于按分类筛选）
     */
    @Field(name = "category_id", type = FieldType.Keyword)
    private Long categoryId;

    /**
     * 状态：0-草稿 1-待审核 2-审核通过 3-驳回（控制检索范围）
     */
    @Field(name = "status", type = FieldType.Integer)
    private Integer status;

    /**
     * 发布时间（用于已发布文章的时间筛选）
     */
    @Field(name = "publish_time", type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date publishTime;

     /**
     * 租户ID（用于多租户场景）
     */
    @Field(name = "tenant_id", type = FieldType.Long)
    private Long tenantId;

}