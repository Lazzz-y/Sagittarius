plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // common
    implementation(project(":sagittarius-common:common-security"))
    implementation(project(":sagittarius-common:common-redis"))
//    implementation(project(":sagittarius-common:common-apidoc"))
}