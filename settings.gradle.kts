rootProject.name = "Sagittarius"

// 微服务模块定义
include(":sagittarius-gateway")
include(":sagittarius-auth")
include(":sagittarius-task")
include(":sagittarius-category")
include(":sagittarius-search")
include(":sagittarius-file")
include(":sagittarius-monitor")

include(":sagittarius-report")
include(":sagittarius-notification")

include(":sagittarius-user")
include("sagittarius-user:user-api")
include("sagittarius-user:user-service")

include(":sagittarius-common")
include(":sagittarius-common:common-web")
include(":sagittarius-common:common-core")
include(":sagittarius-common:common-redis")
include(":sagittarius-common:common-apidoc")
include(":sagittarius-common:common-mybatis")
include(":sagittarius-common:common-rabbitmq")
include(":sagittarius-common:common-security")