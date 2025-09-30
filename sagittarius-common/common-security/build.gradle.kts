dependencies{

    /* Spring Boot */
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // hutool
    implementation("cn.hutool:hutool-all")

    /* common */
    compileOnly(project(":sagittarius-common:common-core"))
    compileOnly(project(":sagittarius-common:common-redis"))
}