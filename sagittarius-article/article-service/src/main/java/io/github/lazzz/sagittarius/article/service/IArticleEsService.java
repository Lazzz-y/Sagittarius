package io.github.lazzz.sagittarius.article.service;


import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import io.github.lazzz.sagittarius.article.model.vo.ArticleEsHighlightVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章 Elasticsearch 服务接口
 *
 * @author Lazzz
 * @date 2025/11/26 20:41
 **/
public interface IArticleEsService {

    // ========== 基础CRUD ==========
    /**
     * 单条保存/更新
     */
    void save(ArticleEs articleEs);

    /**
     * 批量保存/更新
     */
    void bulkSave(List<ArticleEs> articleEsList);

    /**
     * 单条删除
     */
    void delete(ArticleEs articleEs);

    /**
     * 批量删除
     */
    void bulkDelete(List<ArticleEs> articleEsList);

    /**
     * 根据ID删除
     */
    void deleteById(Long id);

    /**
     * 删除所有
     */
    void deleteAll();

    // ========== 搜索 ==========
    /**
     * 基础搜索（无高亮）
     */
    Page<ArticleEs> searchBasic(String keyword, Long categoryId, Integer status, Pageable pageable);

    /**
     * 高亮搜索（支持多条件筛选）
     */
    SearchHits<ArticleEsHighlightVO> searchWithHighlight(String keyword, Long authorId, Long categoryId, Integer status, Pageable pageable);

    /**
     * 高亮搜索结果转换为Page（方便前端分页）
     */
    Page<ArticleEsHighlightVO> convertHighlightToPage(SearchHits<ArticleEsHighlightVO> searchHits, Pageable pageable);
}

