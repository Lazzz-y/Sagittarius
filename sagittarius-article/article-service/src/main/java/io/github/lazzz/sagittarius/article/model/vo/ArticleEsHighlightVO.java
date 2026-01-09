package io.github.lazzz.sagittarius.article.model.vo;

import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.HighlightField;

import java.util.List;

/**
 * 带高亮的文章搜索结果VO
 *
 * @author Lazzz
 * @date 2025/11/26 20:41
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleEsHighlightVO extends ArticleEs {
    /**
     * 高亮的标题片段
     */
    private List<String> titleHighlight;

    /**
     * 高亮的摘要片段
     */
    private List<String> summaryHighlight;

    /**
     * 高亮的正文片段
     */
    @HighlightField
    private List<String> contentMarkdownHighlight;
}