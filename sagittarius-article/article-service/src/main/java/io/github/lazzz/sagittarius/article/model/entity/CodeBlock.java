package io.github.lazzz.sagittarius.article.model.entity;

import io.github.lazzz.sagittarius.article.model.vo.CodeBlockVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码块明细（内嵌在ContentFeatures中）
 * 用于存储文章中的代码块信息，包括代码语言和代码行数
 *
 * @author Lazzz
 * @date 2025/10/23 20:54
 */
@Data
@AutoMappers(
        value = {
                @AutoMapper(target = CodeBlockVO.class)
        }
)
public class CodeBlock {

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