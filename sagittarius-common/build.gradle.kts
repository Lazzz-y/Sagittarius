plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

//
allprojects {
// 禁用bootJar任务，因为这是一个库模块
    springBoot {
        mainClass.set("none")
    }

    tasks.bootJar {
        enabled = false
    }
}