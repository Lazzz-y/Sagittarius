plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

springBoot {
    mainClass.set("none")
}

tasks.bootJar {
    enabled = false
}