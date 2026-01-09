dependencies {

    // Spring Data Mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("com.vladsch.flexmark:flexmark:0.64.8")
    implementation("com.vladsch.flexmark:flexmark-util:0.64.8")
    implementation("com.vladsch.flexmark:flexmark-ext-tables:0.64.8")
    implementation("com.vladsch.flexmark:flexmark-html2md-converter:0.64.8")
    // hutool
    implementation("cn.hutool:hutool-all")

    api(project(":sagittarius-article:article-api"))

    implementation(project(":sagittarius-common:common-web"))
    implementation(project(":sagittarius-common:common-core"))
    implementation(project(":sagittarius-common:common-mybatis"))
    implementation(project(":sagittarius-common:common-jetcache"))
    implementation(project(":sagittarius-common:common-dict"))
    implementation(project(":sagittarius-common:common-security"))
    implementation(project(":sagittarius-common:common-apidoc"))
    implementation(project(":sagittarius-common:common-rabbitmq"))
    implementation(project(":sagittarius-common:common-elasticsearch"))

}