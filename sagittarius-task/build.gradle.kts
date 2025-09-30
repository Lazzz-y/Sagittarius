subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

dependencies{
    implementation(project(":sagittarius-task:task-service"))
    api(project(":sagittarius-task:task-api"))
}

tasks.bootJar {
    enabled = true
    mainClass.set("io.github.lazzz.sagittarius.task.TaskApplication")
}