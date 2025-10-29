plugins {
    // 根项目插件
    id("java-library")
    id("java")
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4"
}

// 根项目基本配置
group = "io.github.lazzz"
version = "1.0-SNAPSHOT"

// 应用于所有项目的配置
allprojects {
    group = "io.github.lazzz"
//    version = "1.0-SNAPSHOT"
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")

    // 仓库配置
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter/") }
        maven { url = uri("https://mvn.getui.com/nexus/content/repositories/releases/") }
        maven {
            url = uri("http://mirrors.huaweicloud.com/repository/maven/")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }

    // 编码设置
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    // 依赖管理配置
    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.5")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
            mavenBom("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2023.0.3.3")
            mavenBom("de.codecentric:spring-boot-admin-dependencies:3.5.5")
        }
        dependencies{
            dependency("io.swagger.core.v3:swagger-annotations:2.2.15")
            dependency("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.5.0")
            dependency("com.github.xiaoymin:knife4j-gateway-spring-boot-starter:4.5.0")
            dependency("com.mybatis-flex:mybatis-flex-spring-boot3-starter:1.11.1")
            dependency("com.mysql:mysql-connector-j:8.0.33")
            dependency("cn.hutool:hutool-all:5.8.22")
            dependency("com.alibaba:fastjson:2.0.41")
            dependency("com.alibaba:easyexcel:3.3.2")
            dependency("org.apache.commons:commons-lang3:3.14.0")
            dependency("jakarta.servlet:jakarta.servlet-api:6.0.0")
            dependency("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.17.13")
            dependency("org.redisson:redisson-spring-boot-starter:3.51.0")
            dependency("org.hibernate:hibernate-validator:8.0.2.Final")
            dependency("io.minio:minio:8.5.14")
            dependency("io.github.linpeilie:mapstruct-plus-spring-boot-starter:1.5.0")
            dependency("io.github.linpeilie:mapstruct-plus-processor:1.5.0")
            dependency("org.springdoc:springdoc-openapi-starter-common:2.3.0")
            dependency("cloud.tianai.captcha:tianai-captcha-springboot-starter:1.5.2")
            dependency("io.micrometer:micrometer-tracing-bom:1.5.4")
            dependency("com.github.ben-manes.caffeine:caffeine:3.2.2")
            dependency("com.alicp.jetcache:jetcache-starter-redis-lettuce:2.7.8")
            dependency("com.esotericsoftware.kryo:kryo5:5.6.2")
        }
    }
}

// 仅应用于子项目的配置
subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    val openFeignClientsProp: String? by project
    val openFeignClients: List<String> = openFeignClientsProp?.split(",")?.map { it.trim() } ?: emptyList()
    if (project.name in openFeignClients){
        println("${project.name} 开启 OpenFeign 客户端")
        dependencies {
            api("org.springframework.cloud:spring-cloud-starter-openfeign")
            api("io.github.openfeign:feign-okhttp")
        }
    }

    if(!project.name.contains("common")){
        dependencies{

            implementation("org.springdoc:springdoc-openapi-starter-common")
            // Micrometer 整合 Brave 追踪器
            implementation("io.micrometer:micrometer-tracing-bridge-brave")
            // Micrometer 指标追踪
            implementation("io.micrometer:micrometer-tracing")
            // Micrometer 观察者
            implementation("io.micrometer:micrometer-observation")
            // Micrometer 整合 Openfeign
            implementation("io.github.openfeign:feign-micrometer")
            // 将追踪器添加到zipkin
            implementation("io.zipkin.reporter2:zipkin-reporter-brave")
        }
    }

    dependencies{

        // lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // mapstruct
        implementation("io.github.linpeilie:mapstruct-plus-spring-boot-starter")
        annotationProcessor("io.github.linpeilie:mapstruct-plus-processor")

        // swagger3
        implementation("io.swagger.core.v3:swagger-annotations")

        // Spring Cloud Alibaba Nacos Discovery
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")

        // Spring Cloud Alibaba Sentinel
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")
        implementation("com.alibaba.csp:sentinel-datasource-nacos")

        // Spring Cloud Alibaba Nacos Config
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

        // Spring Cloud LoadBalancer
        implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

        // Spring Boot Admin
        implementation("de.codecentric:spring-boot-admin-starter-server")
        // Spring Boot Actuator
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        // 为 Apple Silicon Mac 添加 Netty DNS 解析器原生库支持
        if (System.getProperty("os.arch") == "aarch64" || System.getProperty("os.name").contains("Mac")) {
            runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.6.Final:osx-aarch_64")
        }

        // Spring Boot Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("org.junit.jupiter:junit-jupiter-params")
    }
    // 根项目的测试任务配置
    tasks.test {
        useJUnitPlatform()
    }
}