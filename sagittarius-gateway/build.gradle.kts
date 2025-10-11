plugins {
    id("java")
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
}

dependencies {

    // Spring Cloud Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    implementation("com.alibaba.cloud:spring-cloud-alibaba-sentinel-gateway")
    
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Spring Security OAuth2 Resource Server (关键依赖)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // knife4j API 文档
    implementation("com.github.xiaoymin:knife4j-gateway-spring-boot-starter")

    // hutool
    implementation("cn.hutool:hutool-all")

    // 公共依赖
    api(project(":sagittarius-common:common-core"))
    api(project(":sagittarius-common:common-redis"))
}