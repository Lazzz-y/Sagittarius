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

    //api
    implementation(project(":sagittarius-user:user-api"))

    // common
    implementation(project(":sagittarius-common:common-core"))
    implementation(project(":sagittarius-common:common-mybatis"))
}