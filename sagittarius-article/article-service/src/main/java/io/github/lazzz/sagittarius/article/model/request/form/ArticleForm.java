package io.github.lazzz.sagittarius.article.model.request.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lazzz.sagittarius.article.model.vo.ArticleMetaVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 文章插入表单
 *
 * @author Lazzz
 * @date 2025/10/31 20:35
 **/
@Data
public class ArticleForm {

    /**
     * 文档ID（对应MongoDB的_id字段，自动生成或手动指定）
     * 与MySQL的article表中mongo_doc_id字段关联
     * 如果为null，则自动生成
     */
    @Schema(description = "文章ID")
    private String id;

    /**
     * 渲染后的HTML内容（用于前端展示）
     * 例如：<p>正文...</p><img src="...">
     */
    @Schema(description = "渲染后的HTML内容（用于前端展示）")
    private String contentHtml;

    /**
     * Markdown原始内容（用于编辑回溯）
     * 例如：# 标题\n![图片](url)
     */
    @Schema(description = "Markdown原始内容（用于编辑回溯）")
    @NotBlank(message = "文章正文内容不能为空")
    private String contentMarkdown;

    /**
     * 文章总字数
     */
    @Schema(description = "文章总字数")
    private Integer wordCount;

    /**
     * 编辑版本号（首次发布为1，修改后递增）
     */
    @Schema(description = "编辑版本号（首次发布为1，修改后递增）")
    private Integer version;

    /**
     * 编辑历史列表
     */
    @Schema(description = "编辑历史列表（按版本号排序）")
    private List<EditHistoryForm> editHistory;

    /**
     * 创建时间（文档首次存入MongoDB的时间）
     */
    @Schema(description = "创建时间（文档首次存入MongoDB的时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间（文档最后修改时间）
     */
    @Schema(description = "更新时间（文档最后修改时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 文章元数据（如标题、摘要、作者、分类、状态等）
     */
    @Schema(description = "文章元数据（如标题、摘要、作者、分类、状态等）")
    private ArticleMetaForm meta;

}

