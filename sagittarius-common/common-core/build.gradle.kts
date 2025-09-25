dependencies {
    // Spring Boot 基础依赖
    implementation("org.springframework.boot:spring-boot-starter")
    
    // Jakarta Servlet API (Spring Boot 3 使用 jakarta 包名)
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // MyBatis-Flex
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")
    
    // Commons Lang3
    implementation("org.apache.commons:commons-lang3")
    
    // Hutool 工具库
    implementation("cn.hutool:hutool-all")
    
    // Fastjson
    implementation("com.alibaba:fastjson")
}