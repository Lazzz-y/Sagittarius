package io.github.lazzz.sagittarius.article.model.request.query;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.article.model.entity.ArticleMeta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 文章元数据分页查询参数
 * 
 * @author Lazzz 
 * @date 2025/11/22 15:01
**/
@Data
public class ArticleMatePageQuery {

    @Schema(description = "当前页码")
    @NotNull(message = "当前页码不能为空")
    private Long pageNumber;

    @Schema(description = "每页数量")
    @NotNull(message = "每页数量不能为空")
    private Long pageSize;

    private String title;
    @Schema(description = "文章状态")
    private Integer status;
    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "作者ID")
    private Long authorId;
    @Schema(description = "是否推荐")
    private Integer isRecommended;

    public Page<ArticleMeta> toPage() {
        return new Page<>(pageNumber, pageSize);
    }

}

