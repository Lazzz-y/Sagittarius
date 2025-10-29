package io.github.lazzz.sagittarius.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码块视图层
 *
 * @author Lazzz
 * @date 2025/10/23 20:54
 */
@Data
public class CodeBlockVO {

    /**
     * 代码语言（如java、sql、python）
     */
    @Schema(description = "代码语言（如java、sql、python）")
    private String language;

    /**
     * 代码行数
     */
    @Schema(description = "代码行数")
    private Integer lines;
}