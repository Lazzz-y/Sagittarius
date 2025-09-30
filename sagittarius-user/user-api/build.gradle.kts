springBoot {
    mainClass.set("none")
}

tasks.bootJar {
    enabled = false
}

dependencies{
    // MyBatis-Flex
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")
}