subprojects {
    dependencies{
        api(project(":sagittarius-common:common-apidoc"))
        implementation(project(":sagittarius-common:common-web"))
    }
}

allprojects{
    springBoot {
        mainClass.set("none")
    }

    tasks.bootJar {
        enabled = false
    }
}