package io.github.lazzz.sagittarius.system.model.bo;


import lombok.Data;

import java.util.Set;

/**
 * 角色权限业务对象
 *
 * @author Lazzz 
 * @date 2025/09/28 17:26
**/
@Data
public class SysRolePermsBO {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色权限列表
     */
    private Set<String> perms;

}

