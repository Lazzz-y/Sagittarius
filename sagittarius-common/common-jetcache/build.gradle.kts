dependencies{

    api("com.alicp.jetcache:jetcache-starter-redis-lettuce")

//    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.apache.commons:commons-pool2")

    implementation("com.github.ben-manes.caffeine:caffeine")

    api(project(":sagittarius-common:common-redisson"))
}