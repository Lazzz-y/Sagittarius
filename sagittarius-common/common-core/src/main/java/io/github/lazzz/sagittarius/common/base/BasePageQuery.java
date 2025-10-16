package io.github.lazzz.sagittarius.common.base;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数基类
 *
 * @author Lazzz
 * @date 2025/10/15 14:14
 **/
@Data
@Schema
public class BasePageQuery<T> implements Serializable {

    @Schema(description = "页码", example = "1")
    private int pageNum = 1;

    @Schema(description = "每页记录数", example = "10")
    private int pageSize = 10;

    /**
     * 转换为分页对象
     *
     * @author Lazzz
     * @date 2025/10/10
     * @return {@link Page <T>}
     */
    public Page<T> toPage() {
        return new Page<>(this.getPageNum(), this.getPageSize());
    }
}