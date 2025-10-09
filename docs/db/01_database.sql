/*
* Sagittarius 业务数据库
* MySQL8.x版本
*/

-- ----------------------------
-- Nacos 数据库
-- ----------------------------
CREATE DATABASE nacos_config CHARACTER SET utf8 COLLATE utf8_bin;

-- ----------------------------
-- XXL-JOB 数据库
-- ----------------------------
CREATE DATABASE `xxl_job` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

-- ----------------------------
-- OAuth2数据库
-- ----------------------------
CREATE DATABASE IF NOT EXISTS sagittarius_oauth2 DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

-- ----------------------------
-- Sagittarius 业务数据库
-- ----------------------------
CREATE DATABASE IF NOT EXISTS sagittarius DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

-- ----------------------------
-- Seata 数据库
-- ----------------------------
CREATE DATABASE seata DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;