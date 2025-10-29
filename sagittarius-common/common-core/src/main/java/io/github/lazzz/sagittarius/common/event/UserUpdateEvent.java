package io.github.lazzz.sagittarius.common.event;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户更新事件
 * 
 * @author Lazzz 
 * @date 2025/10/25 17:07
**/
@Data
@Accessors(chain = true)
public class UserUpdateEvent {

    /**
     * 用户ID
     */
    Long id;

     /**
      * 用户名
      */
    String username;

     /**
      * 昵称
      */
    String nickname;


}

