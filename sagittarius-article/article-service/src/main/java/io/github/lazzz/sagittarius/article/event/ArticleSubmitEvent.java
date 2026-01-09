package io.github.lazzz.sagittarius.article.event;


import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * 文章提交事件
 * 
 * @author Lazzz 
 * @date 2025/12/06 16:40
**/
@Data
@Accessors(chain = true)
public class ArticleSubmitEvent {

    /**
     * 事件ID
     */
    private UUID eventId;

     /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 文章ES实体
     */
    private ArticleEs articleEs;
}

