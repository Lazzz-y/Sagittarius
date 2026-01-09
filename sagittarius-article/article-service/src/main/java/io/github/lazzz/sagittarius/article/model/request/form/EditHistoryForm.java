package io.github.lazzz.sagittarius.article.model.request.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 编辑历史视图层
 * 
 * @author Lazzz 
 * @date 2025/10/25 12:57
**/
@Data
public class EditHistoryForm {

    /**
     * 历史修订版本号
     */
    @Schema(description = "历史修订版本号")
    private Integer version;

    /**
     * 编辑者ID（关联用户表）
     */
    @Schema(description = "编辑者ID（关联用户表）")
    private Long editorId;

    /**
     * 编辑时间
     */
    @Schema(description = "编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date editTime;

    /**
     * 变更日志（如“首次发布”“修改了代码块”）
     */
    @Schema(description = "变更日志（如“首次发布”“修改了代码块”）")
    private String changeLog;

}

