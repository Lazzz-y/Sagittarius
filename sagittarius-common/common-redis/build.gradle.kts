dependencies{

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Redisson 分布式锁
    api("org.redisson:redisson-spring-boot-starter")

    api("com.alicp.jetcache:jetcache-starter-redis-lettuce")

//    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.apache.commons:commons-pool2")

    implementation("com.github.ben-manes.caffeine:caffeine")

    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly(project(":sagittarius-common:common-core"))
}