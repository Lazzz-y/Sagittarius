subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

dependencies{
    implementation(project(":sagittarius-notification:notification-service"))
    api(project(":sagittarius-notification:notification-api"))
}

tasks.bootJar {
    enabled = true
    mainClass.set("io.github.lazzz.sagittarius.notification.NotificationApplication")
}