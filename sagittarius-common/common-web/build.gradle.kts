dependencies{

    /* Spring Boot */
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.springframework.cloud:spring-cloud-starter-openfeign")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")

    implementation("cn.hutool:hutool-all")
    implementation("org.apache.commons:commons-lang3")
//    api("io.github.openfeign:feign-core")
//    api("io.github.openfeign:feign-httpclient")

    /* common */
    api(project(":sagittarius-common:common-core"))
    api(project(":sagittarius-common:common-redis"))
    compileOnly(project(":sagittarius-common:common-security"))

}