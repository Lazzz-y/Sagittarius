package io.github.lazzz.sagittarius.article.listener;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import io.github.lazzz.sagittarius.article.event.ArticleSubmitEvent;
import io.github.lazzz.sagittarius.article.model.entity.ArticleEs;
import io.github.lazzz.sagittarius.article.service.IArticleEsService;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/12/06 16:54
**/
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleSubmitListener {

    private final IArticleEsService articleEsService;

    /**
     * 处理文章提交事件
     */
    @RabbitListener(queues = MQConstants.QUEUE_ARTICLE_PUBLISH)
    public void handleArticleSubmit(ArticleSubmitEvent event){
        try {
            log.info("处理文章提交事件，事件ID：{}，租户ID：{}，文章ID：{}",
                    event.getEventId(), event.getTenantId(), event.getArticleEs().getId());
            TenantContext.setTenantId(event.getTenantId());
            ArticleEs articleEs = event.getArticleEs();
            articleEsService.save(articleEs);
        } finally {
            TenantContext.clear();
        }
    }

}

