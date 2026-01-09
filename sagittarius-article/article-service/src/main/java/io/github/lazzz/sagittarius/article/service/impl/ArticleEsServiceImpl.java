package io.github.lazzz.sagittarius.article.service.impl;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.util.ObjectBuilder;
import io.github.lazzz.sagittarius.article.mapper.ArticleEsRepository;
import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import io.github.lazzz.sagittarius.article.model.vo.ArticleEsHighlightVO;
import io.github.lazzz.sagittarius.article.service.IArticleEsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.*;

/**
 * 文章ES服务实现类
 *
 * @author Lazzz
 * @date 2025/11/27 13:13
 **/
@Service
@RequiredArgsConstructor
public class ArticleEsServiceImpl implements IArticleEsService {

    private final ArticleEsRepository articleEsRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    // ========== 基础CRUD实现 ==========
    @Override
    public void save(ArticleEs articleEs) {
        articleEsRepository.save(articleEs);
    }

    @Override
    public void bulkSave(List<ArticleEs> articleEsList) {
        articleEsRepository.saveAll(articleEsList);
    }

    @Override
    public void delete(ArticleEs articleEs) {
        articleEsRepository.delete(articleEs);
    }

    @Override
    public void bulkDelete(List<ArticleEs> articleEsList) {
        articleEsRepository.deleteAll(articleEsList);
    }

    @Override
    public void deleteById(Long id) {
        articleEsRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        articleEsRepository.deleteAll();
    }

    @Override
    public Page<ArticleEs> searchBasic(String keyword, Long categoryId, Integer status, Pageable pageable) {
        // 构建 bool 查询条件
        var query = bool(b -> {
            // 关键词匹配（标题/摘要/正文）
            boolQueryGen(keyword, b);
            // 分类筛选
            return getBoolQueryObjectBuilder(categoryId, status, b);
        });
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
        // 执行查询
        SearchHits<ArticleEs> searchHits = elasticsearchOperations.search(nativeQuery, ArticleEs.class);
        List<ArticleEs> content = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

    @Override
    public SearchHits<ArticleEsHighlightVO> searchWithHighlight(String keyword, Long authorId, Long categoryId, Integer status, Pageable pageable) {
        var query = bool(b -> {
            boolQueryGen(keyword, b);
            if (authorId != null) {
                b.filter(f -> f.term(t -> t.field("author_id").value(authorId)));
            }
            return getBoolQueryObjectBuilder(categoryId, status, b);
        });

        // 高亮规则配置
        HighlightParameters highlightParams = HighlightParameters.builder()
                .withPreTags("<em style='color:red;'>")
                .withPostTags("</em>")
                .build();
        HighlightField titleField = new HighlightField("title",
                HighlightFieldParameters.builder()
                        .withFragmentSize(200)
                        .withNumberOfFragments(1)
                        .build());
        HighlightField summaryField = new HighlightField("summary",
                HighlightFieldParameters.builder()
                        .withFragmentSize(200)
                        .withNumberOfFragments(1)
                        .build());
        HighlightField contentField = new HighlightField("content_markdown",
                HighlightFieldParameters.builder()
                        .withFragmentSize(200)
                        .withNumberOfFragments(1)
                        .build());
        List<HighlightField> highlightFields = List.of(titleField, summaryField, contentField);
        Highlight highlight = new Highlight(highlightParams, highlightFields);
        HighlightQuery highlightQuery = new HighlightQuery(highlight, ArticleEsHighlightVO.class);



        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .withHighlightQuery(highlightQuery)
                .build();
        // 执行查询
        return elasticsearchOperations.search(nativeQuery, ArticleEsHighlightVO.class);
    }

    // ========== 高亮结果转Page ==========
    @Override
    public Page<ArticleEsHighlightVO> convertHighlightToPage(SearchHits<ArticleEsHighlightVO> searchHits, Pageable pageable) {
        List<ArticleEsHighlightVO> content = searchHits.stream().map(hit -> {
            ArticleEsHighlightVO vo = hit.getContent();
            vo.setTitleHighlight(hit.getHighlightFields().getOrDefault("title", List.of()));
            vo.setSummaryHighlight(hit.getHighlightFields().getOrDefault("summary", List.of()));
            vo.setContentMarkdownHighlight(hit.getHighlightFields().getOrDefault("content_markdown", List.of()));
            return vo;
        }).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

    private ObjectBuilder<BoolQuery> getBoolQueryObjectBuilder(Long categoryId, Integer status, BoolQuery.Builder b) {
        if (categoryId != null) {
            b.filter(f -> f.term(t -> t.field("category_id").value(categoryId)));
        }
        if (status != null) {
            b.filter(f -> f.term(t -> t.field("status").value(status)));
        }
        return b;
    }

    private void boolQueryGen(String keyword, BoolQuery.Builder b) {
        if (keyword != null && !keyword.isEmpty()) {
            b.should(m -> m.match(ma -> ma.field("title").query(keyword).boost(3.0f)))
                    .should(m -> m.match(ma -> ma.field("summary").query(keyword).boost(2.0f)))
                    .should(m -> m.match(ma -> ma.field("content_markdown").query(keyword)));
        }
    }

}

