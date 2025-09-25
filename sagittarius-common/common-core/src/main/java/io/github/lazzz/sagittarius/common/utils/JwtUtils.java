package io.github.lazzz.sagittarius.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT工具类
 * 用于生成和解析JWT令牌
 */
@Slf4j
public class JwtUtils {

    /**
     * 密钥
     */
    private static final String SECRET_KEY = "Sagittarius_SpringCloud_Microservice_Secret_Key";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 过期时间(毫秒)，默认30分钟
     */
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * 黑名单存储已失效的令牌
     */
    private static final Map<String, Date> TOKEN_BLACKLIST = new ConcurrentHashMap<>();

    /**
     * 生成JWT令牌
     */
    public static String generateToken(Long userId, String username, Map<String, Object> claims) {
        // 设置过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        
        // 创建自定义claims
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("userId", userId);
        customClaims.put("username", username);
        if (claims != null) {
            customClaims.putAll(claims);
        }

        // 生成令牌
        return Jwts.builder()
                .setClaims(customClaims)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 生成JWT令牌（简化版）
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, null);
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseToken(String token) {
        try {
            // 检查令牌是否在黑名单中
            if (isTokenBlacklisted(token)) {
                return null;
            }

            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("userId", Long.class);
        }
        return null;
    }

    /**
     * 从令牌中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("username", String.class);
        }
        return null;
    }

    /**
     * 验证令牌是否有效
     */
    public static boolean validateToken(String token) {
        try {
            // 检查令牌是否在黑名单中
            if (isTokenBlacklisted(token)) {
                return false;
            }

            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            // 检查令牌是否过期
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            log.error("验证JWT令牌失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将令牌加入黑名单
     */
    public static void addTokenToBlacklist(String token) {
        if (token != null) {
            TOKEN_BLACKLIST.put(token, new Date());
            // 定时清理过期的黑名单令牌（可以在实际项目中使用定时任务）
        }
    }

    /**
     * 检查令牌是否在黑名单中
     */
    public static boolean isTokenBlacklisted(String token) {
        return token != null && TOKEN_BLACKLIST.containsKey(token);
    }

    /**
     * 获取令牌前缀
     */
    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    /**
     * 从Authorization头中提取令牌
     */
    public static String extractTokenFromHeader(String authorization) {
        if (authorization != null && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}