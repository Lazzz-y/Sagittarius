plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Cloud Alibaba Nacos Discovery
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    
    // Spring Cloud Alibaba Nacos Config
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    
    // Spring Boot 文件上传下载
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    
    // MinIO 客户端
    implementation("io.minio:minio:8.5.14")
    
    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}