dependencies {

    // Spring Data Mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")


    api(project(":sagittarius-article:article-api"))

    implementation(project(":sagittarius-common:common-web"))
    implementation(project(":sagittarius-common:common-core"))
    implementation(project(":sagittarius-common:common-mybatis"))
    implementation(project(":sagittarius-common:common-jetcache"))
    implementation(project(":sagittarius-common:common-security"))
    implementation(project(":sagittarius-common:common-apidoc"))
    implementation(project(":sagittarius-common:common-rabbitmq"))

}