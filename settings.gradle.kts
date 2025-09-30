rootProject.name = "Sagittarius"

// 微服务模块定义
include(":sagittarius-gateway")
include(":sagittarius-auth")

include(":sagittarius-task")
include(":sagittarius-task:task-api")
include(":sagittarius-task:task-service")

include(":sagittarius-category")
include(":sagittarius-category:category-api")
include(":sagittarius-category:category-service")

include(":sagittarius-search")
include(":sagittarius-search:search-api")
include(":sagittarius-search:search-service")

include(":sagittarius-monitor")
include(":sagittarius-monitor:monitor-api")
include(":sagittarius-monitor:monitor-service")

include(":sagittarius-file")
include(":sagittarius-file:file-api")
include(":sagittarius-file:file-service")

include(":sagittarius-report")
include(":sagittarius-report:report-api")
include(":sagittarius-report:report-service")

include(":sagittarius-notification")
include(":sagittarius-notification:notification-api")
include(":sagittarius-notification:notification-service")

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