dependencies{

    /* Spring Boot */
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // hutool
    implementation("cn.hutool:hutool-all")

    /* common */
    api(project(":sagittarius-common:common-core"))
    api(project(":sagittarius-common:common-redis"))
}