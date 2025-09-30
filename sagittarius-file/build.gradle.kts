subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

dependencies{
    implementation(project(":sagittarius-file:file-service"))
    api(project(":sagittarius-file:file-api"))
}

tasks.bootJar {
    enabled = true
    mainClass.set("io.github.lazzz.sagittarius.file.FileApplication")
}