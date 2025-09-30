subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

dependencies{
    implementation(project(":sagittarius-category:category-service"))
    api(project(":sagittarius-category:category-api"))
}

tasks.bootJar {
    enabled = true
    mainClass.set("io.github.lazzz.sagittarius.category.CategoryApplication")
}