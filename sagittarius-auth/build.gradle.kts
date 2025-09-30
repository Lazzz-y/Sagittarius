plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Sentinel
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")

    // Oauth2.0
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")

    // Redis
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    
    // MySQL
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.mysql:mysql-connector-j")

    // Hutool 工具库
    implementation("cn.hutool:hutool-all")

    // MyBatis-Flex
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")

    // Captcha
    implementation("cloud.tianai.captcha:tianai-captcha-springboot-starter")

    // common
    api(project(":sagittarius-common:common-core"))
    api(project(":sagittarius-common:common-redis"))
    api(project(":sagittarius-common:common-apidoc"))
    api(project(":sagittarius-user:user-api"))
}