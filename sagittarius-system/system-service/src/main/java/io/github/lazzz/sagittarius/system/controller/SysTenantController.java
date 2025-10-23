package io.github.lazzz.sagittarius.system.controller;

import io.github.lazzz.sagittarius.system.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 租户信息表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/tenant")
@Tag(name = "06.租户接口")
@RequiredArgsConstructor
public class SysTenantController {

    private final ISysTenantService sysTenantService;

}