dependencies {

    compileOnly(project(":sagittarius-common:common-core"))

    implementation(project(":sagittarius-common:common-jetcache"))

    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter")

    implementation("cn.hutool:hutool-all")

    api(project(":sagittarius-system:system-api"))

}