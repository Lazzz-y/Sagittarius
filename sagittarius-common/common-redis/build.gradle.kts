dependencies{

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Redisson 分布式锁
    api("org.redisson:redisson-spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-cache")
    //implementation("org.apache.commons:commons-pool2")

    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
}