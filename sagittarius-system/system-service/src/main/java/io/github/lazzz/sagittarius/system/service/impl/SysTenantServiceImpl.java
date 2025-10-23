package io.github.lazzz.sagittarius.system.service.impl;


import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysTenantService;
import io.github.lazzz.sagittarius.system.model.entity.SysTenant;
import io.github.lazzz.sagittarius.system.mapper.SysTenantMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 租户信息表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

}