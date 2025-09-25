plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring Boot Admin Server
    implementation("de.codecentric:spring-boot-admin-starter-server")
    
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Boot Security
    implementation("org.springframework.boot:spring-boot-starter-security")
}