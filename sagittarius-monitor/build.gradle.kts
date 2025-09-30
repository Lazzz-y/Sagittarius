subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

dependencies{
    implementation(project(":sagittarius-monitor:monitor-service"))
    api(project(":sagittarius-monitor:monitor-api"))
}

tasks.bootJar {
    enabled = true
    mainClass.set("io.github.lazzz.sagittarius.monitor.MonitorApplication")
}