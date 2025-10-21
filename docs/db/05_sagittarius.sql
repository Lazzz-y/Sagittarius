-- MySQL dump 10.13  Distrib 8.0.39, for Linux (aarch64)
--
-- Host: localhost    Database: sagittarius
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict`
(
    `id`        bigint unsigned DEFAULT NULL,
    `type_code` varchar(64) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `name`      varchar(50) COLLATE utf8mb4_general_ci  DEFAULT '',
    `value`     varchar(50) COLLATE utf8mb4_general_ci  DEFAULT '',
    `sort`      int                                     DEFAULT '0',
    `status`    tinyint                                 DEFAULT '0',
    `defaulted` tinyint                                 DEFAULT '0',
    `remark`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
    `tenant_id` bigint                                  DEFAULT NULL,
    `create_at` datetime                                DEFAULT NULL,
    `update_at` datetime                                DEFAULT NULL,
    `create_by` bigint                                  DEFAULT NULL,
    `update_by` bigint                                  DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK
TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict`
VALUES (335987889412485120, 'sex', '保密', '0', 1, 1, 0, '', 1, '2025-10-15 16:36:55', '2025-10-18 21:43:15',
        231351564697079808, 231351564697079808),
       (336316035311484928, 'sex', '男', '1', 1, 1, 0, '', 1, '2025-10-16 14:20:51', '2025-10-16 14:20:51',
        231351564697079808, 231351564697079808),
       (336316065288175616, 'sex', '女', '2', 1, 1, 0, '', 1, '2025-10-16 14:20:58', '2025-10-16 14:20:58',
        231351564697079808, 231351564697079808);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type`
(
    `id`        bigint unsigned DEFAULT NULL,
    `name`      varchar(50) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `code`      varchar(50) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `status`    tinyint                                 DEFAULT NULL,
    `remark`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `tenant_id` bigint                                  DEFAULT NULL,
    `create_at` datetime                                DEFAULT NULL,
    `update_at` datetime                                DEFAULT NULL,
    `create_by` bigint                                  DEFAULT NULL,
    `update_by` bigint                                  DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK
TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type`
VALUES (336315625376989184, '性别', 'sex', 1, '性别字典', 1, '2025-10-16 14:19:14', '2025-10-16 14:19:14',
        231351564697079808, 231351564697079808);
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu`
(
    `id`          bigint unsigned NOT NULL,
    `parent_id`   bigint unsigned NOT NULL,
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
    `path`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
    `component`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
    `icon`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '菜单图标',
    `sort_order`  int                                                           DEFAULT '0' COMMENT '排序',
    `is_visible`  tinyint                                                       DEFAULT '1' COMMENT '是否可见: 1-是, 0-否',
    `menu_type`   tinyint                                                       DEFAULT '1' COMMENT '菜单类型:1-菜单,2-目录,3-按钮,4-外链',
    `redirect`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转路径',
    `tree_path`   varchar(255) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '构建路径',
    `always_show` tinyint                                                       DEFAULT NULL COMMENT '[目录]只有一个子路由是否始终显示(1:是 0:否)',
    `keep_alive`  tinyint                                                       DEFAULT NULL COMMENT '[菜单]是否开启页面缓存(1:是 0:否)',
    `tenant_id`   bigint                                                       NOT NULL,
    `create_at`   datetime(3) NOT NULL COMMENT '创建时间',
    `update_at`   datetime(3) NOT NULL COMMENT '更新时间',
    `create_by`   bigint                                                        DEFAULT NULL COMMENT '创建人ID',
    `update_by`   bigint                                                        DEFAULT NULL COMMENT '更新人ID',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
    PRIMARY KEY (`id`),
    KEY           `idx_parent_id` (`parent_id`),
    KEY           `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK
TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu`
VALUES (231369376425578496, 0, '仪表盘', '/dashboard', 'Layout', 'dashboard', 1, 1, 2, NULL, NULL, NULL, NULL, 1,
        '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578497, 0, '内容管理', '/content', 'Layout', 'document', 2, 1, 2, NULL, NULL, NULL, NULL, 1,
        '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578498, 0, '用户管理', '/user', 'Layout', 'user', 3, 1, 2, NULL, NULL, NULL, NULL, 1,
        '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578499, 0, '系统管理', '/system', 'Layout', 'system', 4, 1, 2, NULL, NULL, NULL, NULL, 1,
        '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578500, 231369376425578496, '工作台', '/dashboard/workbench', 'Dashboard', 'workbench', 1, 1, 1,
        NULL, NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578501, 231369376425578496, '数据分析', '/dashboard/analytics', 'Analytics', 'chart', 2, 1, 1, NULL,
        NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578502, 231369376425578497, '文章管理', '/content/posts', 'PostList', 'post', 1, 1, 1, NULL, NULL,
        NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578503, 231369376425578497, '发布文章', '/content/posts/create', 'PostCreate', 'edit', 2, 1, 1,
        NULL, NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578504, 231369376425578497, '分类管理', '/content/categories', 'CategoryList', 'category', 3, 1, 1,
        NULL, NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578505, 231369376425578497, '标签管理', '/content/tags', 'TagList', 'tag', 4, 1, 1, NULL, NULL,
        NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578506, 231369376425578497, '评论管理', '/content/comments', 'CommentList', 'comment', 5, 1, 1,
        NULL, NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578507, 231369376425578498, '用户列表', '/user/list', 'UserList', 'user-list', 1, 1, 1, NULL, NULL,
        NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578508, 231369376425578498, '角色管理', '/user/roles', 'RoleList', 'role', 2, 1, 1, NULL, NULL,
        NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578509, 231369376425578499, '菜单管理', '/system/menus', 'MenuList', 'menu', 1, 1, 1, NULL, NULL,
        NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578510, 231369376425578499, '权限管理', '/system/permissions', 'PermissionList', 'permission', 2, 1,
        1, NULL, NULL, NULL, NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (231369376425578511, 231369376425578499, '操作日志', '/system/logs', 'LogList', 'log', 3, 1, 1, NULL, NULL, NULL,
        NULL, 1, '2025-09-25 12:37:42.000', '2025-09-25 12:37:42.000', NULL, NULL, 0),
       (335717122725834752, 231369376425578499, 'wwwweeee', '/system/test', 'Test', 'test', 4, 1, 1, '/test',
        'null,231369376425578499', 1, 1, 1, '2025-10-14 22:40:59.381', '2025-10-14 22:40:59.381', 231351564697079808,
        231351564697079808, 1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_menu_relation`
--

DROP TABLE IF EXISTS `sys_menu_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu_relation`
(
    `menu_id`       bigint unsigned NOT NULL COMMENT '菜单ID',
    `permission_id` bigint unsigned NOT NULL COMMENT '权限ID',
    `tenant_id`     bigint NOT NULL,
    `create_at`     datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at`     datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by`     bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by`     bigint DEFAULT NULL COMMENT '更新人ID',
    UNIQUE KEY `uk_menu_permission` (`menu_id`,`permission_id`,`update_at`),
    KEY             `idx_menu_id` (`menu_id`),
    KEY             `idx_permission_id` (`permission_id`),
    CONSTRAINT `sys_menu_relation_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE,
    CONSTRAINT `sys_menu_relation_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu_relation`
--

LOCK
TABLES `sys_menu_relation` WRITE;
/*!40000 ALTER TABLE `sys_menu_relation` DISABLE KEYS */;
INSERT INTO `sys_menu_relation`
VALUES (231369376425578500, 231366525271937024, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578502, 231366525271937026, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578503, 231366525271937025, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578506, 231366525271937036, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578507, 231366525271937031, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578508, 231366525271937034, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578509, 231366525271937039, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808),
       (231369376425578510, 231366525271937040, 1, '2025-09-25 13:34:13.000', '2025-09-25 13:34:13.000',
        231351564697079808, 231351564697079808);
/*!40000 ALTER TABLE `sys_menu_relation` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission`
(
    `id`            bigint unsigned NOT NULL,
    `perm_code`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码',
    `perm_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
    `resource_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '资源类型',
    `resource_id`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '*' COMMENT '资源ID, *表示所有',
    `action`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '操作类型',
    `description`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
    `tenant_id`     bigint                                                        NOT NULL,
    `create_at`     datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at`     datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by`     bigint                                                        DEFAULT NULL COMMENT '创建人ID',
    `update_by`     bigint                                                        DEFAULT NULL COMMENT '更新人ID',
    `deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `perm_code` (`perm_code`),
    KEY             `idx_perm_code` (`perm_code`),
    KEY             `idx_resource_type` (`resource_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK
TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission`
VALUES (231366525271937024, 'sys:dashboard:read', '查看仪表盘', 'dashboard', '*', 'read', '查看系统仪表盘', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937025, 'sys:post:add', '创建文章', 'post', '*', 'create', '创建新文章', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937026, 'sys:post:read', '查看文章', 'post', '*', 'read', '查看文章列表和详情', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937027, 'sys:post:edit', '编辑文章', 'post', '*', 'update', '编辑已有文章', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937028, 'sys:post:delete', '删除文章', 'post', '*', 'delete', '删除文章', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937029, 'sys:post:publish', '发布文章', 'post', '*', 'publish', '发布文章', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937030, 'sys:post:audit', '审核文章', 'post', '*', 'audit', '审核文章内容', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937031, 'sys:user:read', '查看用户', 'user', '*', 'read', '查看用户信息', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937032, 'sys:user:edit', '编辑用户', 'user', '*', 'update', '编辑用户信息', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937033, 'sys:user:delete', '删除用户', 'user', '*', 'delete', '删除用户', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937034, 'sys:user:role:assign', '分配角色', 'user', '*', 'assign_role', '为用户分配角色', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937035, 'sys:comment:add', '发表评论', 'comment', '*', 'create', '发表评论', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937036, 'sys:comment:read', '查看评论', 'comment', '*', 'read', '查看评论列表', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937037, 'sys:comment:delete', '删除评论', 'comment', '*', 'delete', '删除评论', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937038, 'sys:comment:audit', '审核评论', 'comment', '*', 'audit', '审核评论内容', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937039, 'sys:system:menu:manage', '菜单管理', 'system', '*', 'manage', '管理系统菜单', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937040, 'sys:system:permission:manage', '权限管理', 'system', '*', 'manage', '管理系统权限', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0),
       (231366525271937041, 'sys:system:role:manage', '角色管理', 'system', '*', 'manage', '管理系统角色', 1,
        '2025-09-25 12:35:27.000', '2025-09-25 12:35:27.000', 231351564697079808, 231351564697079808, 0);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role`
(
    `id`          bigint unsigned NOT NULL,
    `role_code`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码: ROLE_ADMIN, ROLE_EDITOR, ROLE_USER',
    `role_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
    `sort`        int                                                           DEFAULT NULL COMMENT '显示顺序',
    `tenant_id`   bigint                                                       NOT NULL,
    `create_at`   datetime(3) NOT NULL COMMENT '创建时间',
    `update_at`   datetime(3) NOT NULL COMMENT '更新时间',
    `create_by`   bigint                                                        DEFAULT NULL COMMENT '创建人ID',
    `update_by`   bigint                                                        DEFAULT NULL COMMENT '更新人ID',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_code` (`role_code`),
    UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK
TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role`
VALUES (231351564713857024, 'SUPER_ADMIN', '超级管理员', '系统最高权限管理员', 1, 1, '2025-09-25 20:19:29.000',
        '2025-09-25 20:19:39.000', 231351564697079808, 231351564697079808, 0),
       (231351564713857025, 'ADMIN', '管理员', '系统管理员', 2, 1, '2025-09-25 20:19:36.000', '2025-09-25 20:19:39.000',
        231351564697079808, 231351564697079808, 0),
       (231351564713857026, 'EDITOR', '编辑', '内容编辑人员', 3, 1, '2025-09-25 20:19:34.000',
        '2025-09-25 20:19:40.000', 231351564697079808, 231351564697079808, 0),
       (231351564713857027, 'USER', '普通用户', '注册用户', 4, 1, '2025-09-25 20:19:37.000', '2025-10-10 20:09:03.000',
        231351564697079808, 231351564697079808, 0),
       (231351564713857028, 'GUEST', '访客', '未登录用户', 5, 1, '2025-09-25 20:19:38.000', '2025-09-25 20:19:41.000',
        231351564697079808, 231351564697079808, 0),
       (334256768858677248, 'Test', '测试', '123', 6, 1, '2025-10-10 21:58:04.000', '2025-10-12 19:17:56.903',
        231351564697079808, 231351564697079808, 0),
       (334899867758796800, 'TTTT', 'TTTTT', '123', 7, 1, '2025-10-12 16:33:31.000', '2025-10-12 18:55:41.000',
        231351564697079808, 231351564697079808, 0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission`
(
    `role_id`       bigint unsigned NOT NULL COMMENT '角色ID',
    `permission_id` bigint unsigned NOT NULL COMMENT '权限ID',
    `tenant_id`     bigint NOT NULL,
    `create_at`     datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at`     datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by`     bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by`     bigint DEFAULT NULL COMMENT '更新人ID',
    UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`,`update_at`),
    KEY             `idx_role_id` (`role_id`),
    KEY             `idx_permission_id` (`permission_id`),
    CONSTRAINT `sys_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `sys_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK
TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission`
VALUES (231351564713857024, 231366525271937024, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937025, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937026, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937027, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937028, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937029, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937030, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937031, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937032, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937033, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937034, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937035, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937036, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937037, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857024, 231366525271937038, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937024, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937025, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937026, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937027, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937028, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937029, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937030, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937031, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937032, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937034, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937035, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937036, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937037, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857025, 231366525271937038, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937024, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937025, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937026, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937028, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937034, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857026, 231366525271937035, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857027, 231366525271937025, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857027, 231366525271937034, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808),
       (231351564713857027, 231366525271937035, 1, '2025-09-25 12:40:19.000', '2025-09-25 12:40:19.000',
        231351564697079808, 231351564697079808);
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '租户ID（自增主键，全局唯一）',
    `tenant_key`   varchar(64)  NOT NULL COMMENT '租户唯一标识（用于域名/API隔离，如blog_enterprise_a）',
    `tenant_name`  varchar(128) NOT NULL COMMENT '租户名称（如XX科技有限公司博客）',
    `tenant_type`  tinyint      NOT NULL DEFAULT '1' COMMENT '租户类型：1-个人租户，2-企业租户，3-团队租户',
    `domain`       varchar(255)          DEFAULT NULL COMMENT '租户独立域名（如''blog.enterprise-a.com''，空表示使用平台二级域名）',
    `logo_url`     varchar(512)          DEFAULT NULL COMMENT '租户Logo地址',
    `theme_config` json                  DEFAULT NULL COMMENT '租户主题配置（JSON格式，如{"color":"#337ab7","layout":"fluid"}）',
    `contact_info` json                  DEFAULT NULL COMMENT '联系人信息（JSON格式，如{"name":"张三","phone":"13800138000","email":"contact@enterprise-a.com"}）',
    `billing_plan` varchar(32)  NOT NULL DEFAULT 'free' COMMENT '计费方案：free-免费版，standard-标准版，enterprise-企业版',
    `quota_config` json         NOT NULL COMMENT '资源配额配置（JSON格式）：{"max_articles":1000,"max_storage":10737418240,"max_users":50}',
    `used_storage` bigint       NOT NULL DEFAULT '0' COMMENT '已使用存储（字节，用于配额校验）',
    `status`       tinyint      NOT NULL DEFAULT '1' COMMENT '租户状态：0-禁用（不可访问），1-正常，2-过期（需续费），3-冻结（违规冻结）',
    `expire_time`  datetime              DEFAULT NULL COMMENT '过期时间（NULL表示永久有效，用于付费租户）',
    `lock_reason`  varchar(512)          DEFAULT NULL COMMENT '冻结原因（仅status=3时有效）',
    `create_at`    datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at`    datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by`    bigint       NOT NULL COMMENT '创建人ID（关联sys_user.id）',
    `update_by`    bigint                DEFAULT NULL COMMENT '更新人ID（关联sys_user.id）',
    `deleted`      tinyint      NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_key` (`tenant_key`) COMMENT '租户标识唯一',
    UNIQUE KEY `uk_domain` (`domain`) COMMENT '独立域名唯一',
    KEY            `idx_status` (`status`) COMMENT '按状态查询租户',
    KEY            `idx_expire_time` (`expire_time`) COMMENT '按过期时间查询（用于提醒续费）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK
TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user`
(
    `id`            bigint unsigned NOT NULL COMMENT '主键ID',
    `username`      varchar(50)  NOT NULL COMMENT '用户名',
    `nickname`      varchar(50)  NOT NULL COMMENT '昵称',
    `password`      varchar(100) NOT NULL COMMENT '密码',
    `sex`           tinyint               DEFAULT '0' COMMENT '性别 0-未知 1-男 2-女',
    `email`         varchar(100)          DEFAULT NULL COMMENT '邮箱',
    `phone`         varchar(20)           DEFAULT NULL COMMENT '手机号',
    `avatar`        varchar(255)          DEFAULT NULL COMMENT '头像',
    `status`        tinyint      NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
    `user_type`     tinyint               DEFAULT '0' COMMENT '用户类型（0-普通用户，1-博客作者，2-内容管理员）',
    `bio`           varchar(500)          DEFAULT NULL COMMENT '个人简介',
    `website`       varchar(255)          DEFAULT NULL COMMENT '个人网站',
    `article_count` int                   DEFAULT '0' COMMENT '文章数量',
    `comment_count` int                   DEFAULT '0' COMMENT '评论数量',
    `view_count`    int                   DEFAULT '0' COMMENT '被访问次数',
    `tenant_id`     bigint       NOT NULL,
    `create_at`     datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at`     datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by`     bigint                DEFAULT NULL COMMENT '创建人ID',
    `update_by`     bigint                DEFAULT NULL COMMENT '更新人ID',
    `deleted`       tinyint(1) DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `login_name` (`username`) USING BTREE,
    UNIQUE KEY `email` (`email`),
    UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK
TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user`
VALUES (231351564697079808, 'admin', '系统管理员', '$2a$10$33lm/UWPXbTShD1nKUNKle1F7mEinpic18Vjrw0YfimGDF9Ou5zKS', 0,
        'zydot998@gmail.com', '18982222313', 'https://example.com/avatars/admin.png', 1, 2, '系统管理员',
        'https://example.com', 0, 0, 0, 1, '2025-09-22 16:48:05.000', '2025-09-22 16:48:06.000', NULL, NULL, 0),
       (334093861909602304, 'demo', 'T1', '$2a$10$0.1WMRpzk.jqmLTOLel/iOqcJFg1oMUH5t/imitCw2YhnYFW5vm1u', 0, NULL, NULL,
        NULL, 1, 0, NULL, NULL, 0, 0, 0, 1, '2025-10-10 11:10:44.000', '2025-10-12 16:57:35.000', 231351564697079808,
        231351564697079808, 0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role`
(
    `user_id`   bigint unsigned NOT NULL COMMENT '用户ID',
    `role_id`   bigint unsigned NOT NULL COMMENT '角色ID',
    `tenant_id` bigint NOT NULL,
    `create_at` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at` datetime(3) DEFAULT NULL COMMENT '更新时间',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    UNIQUE KEY `uk_user_role` (`user_id`,`role_id`,`update_at`),
    KEY         `idx_user_id` (`user_id`),
    KEY         `idx_role_id` (`role_id`),
    CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK
TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role`
VALUES (231351564697079808, 231351564713857024, 1, '2025-09-27 22:58:41.000', '2025-09-27 22:58:47.000', NULL, NULL),
       (334093861909602304, 231351564713857027, 1, '2025-10-11 16:06:21.000', '2025-10-11 16:06:21.000',
        231351564697079808, 231351564697079808);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           varchar(128) NOT NULL COMMENT 'global transaction id',
    `context`       varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` longblob     NOT NULL COMMENT 'rollback info',
    `log_status`    int          NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   datetime(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  datetime(6) NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AT transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK
TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK
TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-18 22:08:54
