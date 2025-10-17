dependencies{

    api("com.alicp.jetcache:jetcache-starter-redis-lettuce")

    api("org.redisson:redisson-spring-boot-starter")

//    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.apache.commons:commons-pool2")

    implementation("com.github.ben-manes.caffeine:caffeine")

    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")

    // hutool
    implementation("cn.hutool:hutool-all")

    compileOnly(project(":sagittarius-common:common-core"))

    compileOnly(project(":sagittarius-system:system-api"))
}