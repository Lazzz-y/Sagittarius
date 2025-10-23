package io.github.lazzz.sagittarius.article.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * 文章内容实体类
 * 用于存储文章的详细内容，与文章元数据 ArticleMeta 是一对一关系
 * 包含文章的 HTML 内容、Markdown 内容等
 *
 * @author Lazzz 
 * @date 2025/10/22 11:25
**/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "article")
public class Article {

    /**
     * 文档ID（对应MongoDB的_id字段，自动生成或手动指定）
     * 与MySQL的article表中mongo_doc_id字段关联
     */
    @Id
    private String id;

    /**
     * 渲染后的HTML内容（用于前端展示）
     * 例如：<p>正文...</p><img src="...">
     */
    private String contentHtml;

    /**
     * Markdown原始内容（用于编辑回溯）
     * 例如：# 标题\n![图片](url)
     */
    private String contentMarkdown;

    /**
     * 正文特征（动态字段，可选）
     */
    private ContentFeatures contentFeatures;

    /**
     * 编辑版本号（首次发布为1，修改后递增）
     */
    private Integer version = 1;

    /**
     * 编辑历史（保留最近3次修改，可选）
     */
    private List<EditHistory> editHistory;

    /**
     * 创建时间（文档首次存入MongoDB的时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间（文档最后修改时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

