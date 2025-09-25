package io.github.lazzz.sagittarius.auth.config;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.lazzz.sagittarius.auth.model.SysUserDetails;
import io.github.lazzz.sagittarius.auth.oauth2.handler.CustomAuthenticationFailureHandler;
import io.github.lazzz.sagittarius.auth.oauth2.handler.CustomAuthenticationSuccessHandler;
import io.github.lazzz.sagittarius.auth.oauth2.ext.password.PasswordAuthenticationConverter;
import io.github.lazzz.sagittarius.auth.oauth2.ext.password.PasswordAuthenticationProvider;
import io.github.lazzz.sagittarius.auth.oauth2.jackson.SysUserMixin;
import io.github.lazzz.sagittarius.common.constant.AuthConstants;
import io.github.lazzz.sagittarius.common.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Lazzz
 * @className AuthorizationServerConfig
 * @description 配置授权服务器
 * @date 2025/09/19 21:34
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    @Value("${sagittarius.jwt.jks.path}")
    private String jskPath;

    @Value("${sagittarius.jwt.jks.alias}")
    private String jskAlias;

    @Value("${sagittarius.jwt.jks.password}")
    private String jskPassword;

    @Value("${sagittarius.jwt.jks.enable}")
    private Boolean enableJks;

    private final StringRedisTemplate redis;

    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

    /**
     * 授权服务器端点配置
     */
    @Bean
    @Order(0)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<?> tokenGenerator
    ) throws Exception {
        // 应用默认的 OAuth2 授权服务器安全配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 自定义配置
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                .tokenEndpoint(tokenEndpoint -> {
                            tokenEndpoint
                                    .accessTokenRequestConverters(
                                            authenticationConverters ->
                                            {
                                                authenticationConverters.addAll(
                                                        List.of(
                                                                new PasswordAuthenticationConverter()
                                                        )
                                                );
                                            }
                                    ).authenticationProviders(
                                            authenticationProviders ->
                                            {
                                                // 自定义授权模式提供者(Provider)
                                                authenticationProviders.addAll(
                                                        List.of(
                                                                new PasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator)
                                                        )
                                                );
                                            }
                                    )
                                    .accessTokenResponseHandler(new CustomAuthenticationSuccessHandler())
                                    .errorResponseHandler(new CustomAuthenticationFailureHandler());
                        }


                ).oidc(Customizer.withDefaults());
        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /*
     * 配置 JWK 源，为授权服务器提供用于签署令牌的密钥
     * 这是配置 JWT 令牌生成器的核心
     * 自动暴露端点:
     *   /oauth2/jwks: 获取公钥
     *   /oauth2/token: 获取令牌
     *   /oauth2/authorize: 授权端点
     */
    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
        // 如果 keyPair 不为 null 说明真正使用密钥库
        if (enableJks) {
            KeyPair keyPair = keyPair();
            RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey(keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();
            JWKSet jwkSet = new JWKSet(rsaKey);
            return new ImmutableJWKSet<>(jwkSet);
        }
        // 优先从 Redis 查找 JWK
        String jwkSetStr = redis.opsForValue().get(RedisConstants.JWK_SET_KEY);
        if (StrUtil.isNotBlank(jwkSetStr)) {
            JWKSet jwkSet = JWKSet.parse(jwkSetStr);
            return new ImmutableJWKSet<>(jwkSet);
        } else {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();
            // 构建RSAKey
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            JWKSet jwkSet = new JWKSet(rsaKey);
            // 将JWKSet存储在Redis中
            redis.opsForValue().set(RedisConstants.JWK_SET_KEY, jwkSet.toString(Boolean.FALSE));
            return new ImmutableJWKSet<>(jwkSet);
        }
    }

    @SneakyThrows
    private KeyPair keyPair() {
        if (!enableJks) {
            return null;
        }
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream inputStream = new FileInputStream(jskPath);
        keyStore.load(inputStream, jskPassword.toCharArray());
        RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(jskAlias, jskPassword.toCharArray());
        RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate(jskAlias).getPublicKey();
        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 生成RSA密钥对
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 配置 JWT 解码器，授权服务器本身有时也需要解码 JWT（例如刷新令牌时）。
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 授权服务器配置
     * 令牌签发、获取令牌等端点
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        // 初始化 OAuth2 客户端
        initBlogClient(registeredClientRepository);
        initTestClient(registeredClientRepository);

        return registeredClientRepository;
    }

    /**
     * 配置 OAuth2 授权服务，用于管理 OAuth2 授权信息的存储和检索
     *
     * <p>该 Bean 负责：</p>
     * <ul>
     *   <li>创建基于 JDBC 的 OAuth2 授权服务实现</li>
     *   <li>配置数据库访问层，使用 JdbcTemplate 进行数据持久化</li>
     *   <li>设置自定义的行映射器，用于处理 OAuth2 授权数据的序列化和反序列化</li>
     *   <li>配置 Jackson ObjectMapper 以支持 Spring Security 特定类型的序列化</li>
     *   <li>支持自定义用户详情类的序列化（通过 Mixin 方式）</li>
     * </ul>
     *
     * <p>OAuth2 授权服务负责存储以下信息：</p>
     * <ul>
     *   <li>授权码 (Authorization Code)</li>
     *   <li>访问令牌 (Access Token)</li>
     *   <li>刷新令牌 (Refresh Token)</li>
     *   <li>客户端注册信息</li>
     *   <li>用户授权信息</li>
     * </ul>
     *
     * @param jdbcTemplate               JDBC 模板，用于数据库操作
     * @param registeredClientRepository 客户端仓库，用于管理 OAuth2 客户端信息
     * @return 配置好的 OAuth2 授权服务实例
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        // 创建基于 JDBC 的 OAuth2 授权服务。这个服务使用 JdbcTemplate 和客户端仓库来存储和检索 OAuth2 授权数据
        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
        // 创建并配置用于处理数据库中 OAuth2 授权数据的行映射器
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper
                = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        // 设置 LOB 处理器，用于处理数据库中的大对象数据
        rowMapper.setLobHandler(new DefaultLobHandler());
        // 创建 ObjectMapper 实例，用于 OAuth2 授权数据的序列化和反序列化
        ObjectMapper objectMapper = new ObjectMapper();
        // 获取 JdbcOAuth2AuthorizationService 类的类加载器
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        // 获取 Spring Security 相关的 Jackson 模块
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        // 注册 Spring Security 模块到 ObjectMapper
        objectMapper.registerModules(securityModules);
        // 注册 OAuth2 授权服务器专用的 Jackson 模块
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        // 添加自定义 Mixin，用于序列化/反序列化扩展的用户信息类
        // 这里假设 SysUserDetails 是你的自定义用户详情类，SysUserMixin 是对应的 Mixin 类
        // 你需要根据实际的类名进行替换
        objectMapper.addMixIn(SysUserDetails.class, SysUserMixin.class);
        objectMapper.addMixIn(Long.class, Object.class);

        // 将配置好的 ObjectMapper 设置到行映射器中
        rowMapper.setObjectMapper(objectMapper);

        // 将自定义行映射器设置到授权服务器中
        service.setAuthorizationRowMapper(rowMapper);
        return service;
    }

    /**
     * 配置 OAuth2 授权同意服务，用于管理用户授权信息
     *
     * <p>该 Bean 负责：</p>
     * <ul>
     *   <li>创建基于 JDBC 的 OAuth2 授权同意服务实现</li>
     *   <li>配置数据库访问层，使用 JdbcTemplate 进行数据持久化</li>
     *   <li>设置自定义的行映射器，用于处理 OAuth2 授权同意数据的序列化和反序列化</li>
     *   <li>配置 Jackson ObjectMapper 以支持 Spring Security 特定类型的序列化</li>
     *   <li>支持自定义用户详情类的序列化（通过 Mixin 方式）</li>
     *   <li>配置自定义的授权同意服务，用于处理用户授权信息 </li>
     *
     * @param jdbcTemplate               JDBC 模板，用于数据库操作
     * @param registeredClientRepository 客户端仓库，用于管理 OAuth2 客户端信息
     * @return 配置好的 OAuth2 授权服务实例
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                         RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 配置 OAuth2 令牌生成器，用于生成访问令牌、刷新令牌和 JWT 令牌
     *
     * <p>该 Bean 负责：</p>
     * <ul>
     *   <li>创建基于 JWK 的令牌生成器实现</li>
     *   <li>设置 JWK 源，用于生成 JWT 令牌</li>
     *   <li>设置自定义的 JWT 令牌自定义器，用于自定义 JWT 令牌的生成过程</li>
     *   <li>创建访问令牌生成器，用于生成访问令牌</li>
     *   <li>创建刷新令牌生成器，用于生成刷新令牌</li>
     *   <li>返回 DelegatingOAuth2TokenGenerator 实例，用于生成访问令牌、刷新令牌和 JWT 令牌</li>
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                accessTokenGenerator,
                refreshTokenGenerator
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 正式客户端
     */
    private void initBlogClient(JdbcRegisteredClientRepository registeredClientRepository) {
        String clientId = "blog";
        String clientSecret = "123456";
        String clientName = "blog_client";
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        String id = registeredClient != null ? registeredClient.getId() : UUID.randomUUID().toString();
        String encodeSecret = passwordEncoder().encode(clientSecret);
        RegisteredClient blogClient = RegisteredClient.withId(id)
                .clientId(clientId)
                .clientSecret(encodeSecret)
                .clientName(clientName)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 密码模式
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("http://127.0.0.1:8080/authorized")
                .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientRepository.save(blogClient);
    }

    /**
     * 初始化测试客户端，用于OAuth2密码模式测试
     */
    private void initTestClient(JdbcRegisteredClientRepository registeredClientRepository) {
        // 检查客户端是否已存在
        RegisteredClient existingClient = registeredClientRepository.findByClientId(AuthConstants.KNIFE4J_CLIENT_ID);
        if (existingClient != null) {
            return;
        }

        // 创建测试客户端
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(AuthConstants.KNIFE4J_CLIENT_ID)
                .clientSecret(passwordEncoder().encode(AuthConstants.KNIFE4J_CLIENT_SECRET))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/callback")
                .scope("read")
                .scope("write")
                .tokenSettings(
                        TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofHours(1))
                                .refreshTokenTimeToLive(Duration.ofDays(30))
                                .build()
                )
                .build();

        registeredClientRepository.save(client);
    }


}
