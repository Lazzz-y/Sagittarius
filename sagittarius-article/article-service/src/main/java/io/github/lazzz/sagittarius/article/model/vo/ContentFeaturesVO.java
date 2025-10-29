package io.github.lazzz.sagittarius.article.model.vo;

import io.github.lazzz.sagittarius.article.model.entity.CodeBlock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 正文特征视图层
 *
 * @author Lazzz
 * @date 2025/10/23 20:56
 **/
@Data
public class ContentFeaturesVO {

    /**
     * 文章总字数
     */
    @Schema(description = "文章总字数")
    private Integer wordCount;

    /**
     * 代码块列表
     */
    @Schema(description = "代码块列表")
    private List<CodeBlockVO> codeBlocks;

    /**
     * 是否包含视频
     */
    @Schema(description = "是否包含视频")
    private Boolean hasVideo;
}