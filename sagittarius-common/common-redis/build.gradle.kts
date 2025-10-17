dependencies{

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

//    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.apache.commons:commons-pool2")

    // hutool
    implementation("cn.hutool:hutool-all")

    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly(project(":sagittarius-common:common-core"))

    api(project(":sagittarius-common:common-redisson"))
}