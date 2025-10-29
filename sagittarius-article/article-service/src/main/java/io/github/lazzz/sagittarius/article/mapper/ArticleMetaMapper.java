package io.github.lazzz.sagittarius.article.mapper;

import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章主表（元数据） 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface ArticleMetaMapper extends BaseMapper<ArticleMeta> {


}
