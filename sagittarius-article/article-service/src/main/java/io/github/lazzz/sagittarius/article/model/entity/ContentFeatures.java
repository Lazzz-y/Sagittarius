package io.github.lazzz.sagittarius.article.model.entity;

import io.github.lazzz.sagittarius.article.model.vo.ContentFeaturesVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;


/**
 * 正文特征（内嵌在ArticleContent中）
 * 用于存储文章的正文特征，包括文章总字数、代码块明细列表、是否包含视频等
 *
 * @author Lazzz
 * @date 2025/10/23 20:56
 **/
@Data
@AutoMappers(
        value = {
                @AutoMapper(target = ContentFeaturesVO.class)
        }
)
public class ContentFeatures {

    /**
     * 文章总字数
     */
    @Schema(description = "文章总字数")
    private Integer wordCount;

    /**
     * 代码块明细列表
     */
    @Schema(description = "代码块明细列表")
    private List<CodeBlock> codeBlocks;

    /**
     * 是否包含视频
     */
    @Schema(description = "是否包含视频")
    private Boolean hasVideo;

    // 可根据业务需求新增字段，如：
    // private Boolean hasFormula; // 是否包含公式
    // private Integer imageCount; // 图片数量
}