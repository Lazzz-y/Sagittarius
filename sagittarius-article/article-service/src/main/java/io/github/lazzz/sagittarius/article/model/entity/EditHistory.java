package io.github.lazzz.sagittarius.article.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * 编辑历史明细（内嵌在ArticleContent中）
 * 用于存储文章的编辑历史，包括版本号、编辑者ID、编辑时间和变更日志
 *
 * @author Lazzz
 * @date 2025/10/23 20:57
 **/
@Data
public class EditHistory {

    /**
     * 版本号（与ArticleContent的version对应）
     */
    private Integer version;

    /**
     * 编辑者ID（关联用户表）
     */
    private Long editorId;

    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date editTime;

    /**
     * 变更日志（如“首次发布”“修改了代码块”）
     */
    private String changeLog;
}