plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Cloud OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    
    // MySQL
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.mysql:mysql-connector-j")

    // MyBatis-Flex
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    
    // RabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}