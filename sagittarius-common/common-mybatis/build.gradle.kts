dependencies{
    // MySQL
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.mysql:mysql-connector-j")

    // MyBatis-Flex
    api("com.mybatis-flex:mybatis-flex-spring-boot3-starter")

    // lang3
    implementation("org.apache.commons:commons-lang3")

    // hutool
    implementation("cn.hutool:hutool-all")

    // jakarta
    implementation("jakarta.validation:jakarta.validation-api")

    // hibernate-validator
    implementation("org.hibernate:hibernate-validator")

    // seata 分布式事务
//    api("com.alibaba.cloud:spring-cloud-starter-alibaba-seata")

    compileOnly(project(":sagittarius-common:common-web"))
    compileOnly(project(":sagittarius-common:common-security"))
}