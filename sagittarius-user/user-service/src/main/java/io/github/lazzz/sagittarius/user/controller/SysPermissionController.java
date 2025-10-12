package io.github.lazzz.sagittarius.user.controller;

import io.github.lazzz.sagittarius.common.annotation.RefreshableController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统权限表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RefreshableController
@RequestMapping("/sysPermission")
@Tag(name = "03.权限接口")
@RequiredArgsConstructor
public class SysPermissionController {

}