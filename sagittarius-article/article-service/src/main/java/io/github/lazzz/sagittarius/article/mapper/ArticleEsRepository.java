package io.github.lazzz.sagittarius.article.mapper;

import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章ES仓储接口
 * 继承ElasticsearchRepository实现基础CRUD，扩展搜索能力
 *
 * @author Lazzz
 */
@Repository
public interface ArticleEsRepository extends ElasticsearchRepository<ArticleEs, Long> { }