package io.github.lazzz.sagittarius.system.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户信息DTO
 * 
 * @author Lazzz 
 * @date 2025/10/30 23:08
**/
@Data
@Accessors(chain = true)
public class UserInfoDTO {

     /* 用户ID */
    private Long userId;

     /* 用户名 */
    private String username;

}

