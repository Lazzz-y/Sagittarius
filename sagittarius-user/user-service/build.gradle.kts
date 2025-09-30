dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Sentinel
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")

    // hutool
    implementation("cn.hutool:hutool-all")

    //api
    api(project(":sagittarius-user:user-api"))

    // common
    implementation(project(":sagittarius-common:common-core"))
    implementation(project(":sagittarius-common:common-mybatis"))
    implementation(project(":sagittarius-common:common-security"))
    
}