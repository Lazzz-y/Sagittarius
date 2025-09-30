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
-- Table structure for table `sys_menu`
--

use sagittarius;

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID, 0为顶级菜单',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `is_visible` tinyint DEFAULT '1' COMMENT '是否可见: 1-是, 0-否',
  `menu_type` tinyint DEFAULT '1' COMMENT '菜单类型: 1-菜单, 2-按钮, 3-接口',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'仪表盘','/dashboard','Layout','dashboard',1,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(2,0,'内容管理','/content','Layout','document',2,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(3,0,'用户管理','/user','Layout','user',3,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(4,0,'系统管理','/system','Layout','system',4,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(5,1,'工作台','/dashboard/workbench','Dashboard','workbench',1,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(6,1,'数据分析','/dashboard/analytics','Analytics','chart',2,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(7,2,'文章管理','/content/posts','PostList','post',1,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(8,2,'发布文章','/content/posts/create','PostCreate','edit',2,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(9,2,'分类管理','/content/categories','CategoryList','category',3,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(10,2,'标签管理','/content/tags','TagList','tag',4,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(11,2,'评论管理','/content/comments','CommentList','comment',5,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(12,3,'用户列表','/user/list','UserList','user-list',1,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(13,3,'角色管理','/user/roles','RoleList','role',2,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(14,4,'菜单管理','/system/menus','MenuList','menu',1,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(15,4,'权限管理','/system/permissions','PermissionList','permission',2,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0),(16,4,'操作日志','/system/logs','LogList','log',3,1,1,'2025-09-25 12:37:42','2025-09-25 12:37:42',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu_relation`
--

DROP TABLE IF EXISTS `sys_menu_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_permission` (`menu_id`,`permission_id`),
  KEY `idx_menu_id` (`menu_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `sys_menu_relation_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sys_menu_relation_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu_relation`
--

LOCK TABLES `sys_menu_relation` WRITE;
/*!40000 ALTER TABLE `sys_menu_relation` DISABLE KEYS */;
INSERT INTO `sys_menu_relation` VALUES (1,5,1,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(2,7,3,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(3,8,2,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(4,11,13,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(5,12,8,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(6,13,11,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(7,14,16,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0),(8,15,17,'2025-09-25 13:34:13','2025-09-25 13:34:13',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_menu_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `perm_code` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码',
  `perm_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `resource_type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型',
  `resource_id` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '*' COMMENT '资源ID, *表示所有',
  `action` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `perm_code` (`perm_code`),
  KEY `idx_perm_code` (`perm_code`),
  KEY `idx_resource_type` (`resource_type`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,'sys:dashboard:read','查看仪表盘','dashboard','*','read','查看系统仪表盘','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(2,'sys:post:create','创建文章','post','*','create','创建新文章','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(3,'sys:post:read','查看文章','post','*','read','查看文章列表和详情','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(4,'sys:post:update','编辑文章','post','*','update','编辑已有文章','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(5,'sys:post:delete','删除文章','post','*','delete','删除文章','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(6,'sys:post:publish','发布文章','post','*','publish','发布文章','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(7,'sys:post:audit','审核文章','post','*','audit','审核文章内容','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(8,'sys:user:read','查看用户','user','*','read','查看用户信息','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(9,'sys:user:update','编辑用户','user','*','update','编辑用户信息','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(10,'sys:user:delete','删除用户','user','*','delete','删除用户','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(11,'sys:user:role:assign','分配角色','user','*','assign_role','为用户分配角色','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(12,'sys:comment:create','发表评论','comment','*','create','发表评论','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(13,'sys:comment:read','查看评论','comment','*','read','查看评论列表','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(14,'sys:comment:delete','删除评论','comment','*','delete','删除评论','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(15,'sys:comment:audit','审核评论','comment','*','audit','审核评论内容','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(16,'sys:system:menu:manage','菜单管理','system','*','manage','管理系统菜单','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(17,'sys:system:permission:manage','权限管理','system','*','manage','管理系统权限','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0),(18,'sys:system:role:manage','角色管理','system','*','manage','管理系统角色','2025-09-25 12:35:27','2025-09-25 12:35:27',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码: ROLE_ADMIN, ROLE_EDITOR, ROLE_USER',
  `role_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `is_system` tinyint DEFAULT '0' COMMENT '是否系统角色: 1-是, 0-否',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'ROLE_SUPER_ADMIN','超级管理员','系统最高权限管理员',0,'2025-09-25 20:19:29','2025-09-25 20:19:39',NULL,NULL,0),(2,'ROLE_ADMIN','管理员','系统管理员',0,'2025-09-25 20:19:36','2025-09-25 20:19:39',NULL,NULL,0),(3,'ROLE_EDITOR','编辑','内容编辑人员',0,'2025-09-25 20:19:34','2025-09-25 20:19:40',NULL,NULL,0),(4,'ROLE_USER','普通用户','注册用户',0,'2025-09-25 20:19:37','2025-09-25 20:19:41',NULL,NULL,0),(5,'ROLE_GUEST','访客','未登录用户',0,'2025-09-25 20:19:38','2025-09-25 20:19:41',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `sys_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (1,1,1,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(2,1,2,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(3,1,3,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(4,1,4,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(5,1,5,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(6,1,6,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(7,1,7,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(8,1,8,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(9,1,9,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(10,1,10,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(11,1,11,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(12,1,12,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(13,1,13,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(14,1,14,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(15,1,15,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(16,2,1,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(17,2,2,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(18,2,3,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(19,2,4,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(20,2,5,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(21,2,6,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(22,2,7,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(23,2,8,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(24,2,9,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(25,2,11,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(26,2,12,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(27,2,13,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(28,2,14,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(29,2,15,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(30,3,1,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(31,3,2,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(32,3,3,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(33,3,5,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(34,3,11,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(35,3,12,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(36,4,2,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(37,4,11,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0),(38,4,12,'2025-09-25 12:40:19','2025-09-25 12:40:19',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `user_type` tinyint DEFAULT '0' COMMENT '用户类型（0-普通用户，1-博客作者，2-内容管理员）',
  `bio` varchar(500) DEFAULT NULL COMMENT '个人简介',
  `website` varchar(255) DEFAULT NULL COMMENT '个人网站',
  `article_count` int DEFAULT '0' COMMENT '文章数量',
  `comment_count` int DEFAULT '0' COMMENT '评论数量',
  `view_count` int DEFAULT '0' COMMENT '被访问次数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','系统管理员','$2a$10$33lm/UWPXbTShD1nKUNKle1F7mEinpic18Vjrw0YfimGDF9Ou5zKS','zydot998@gmial.com','18982222313','https://example.com/avatars/admin.png',1,2,'系统管理员','https://example.com',0,0,0,'2025-09-22 16:48:05','2025-09-22 16:48:06',NULL,NULL,0),(2,'demo','demo','$2a$10$PWTST0HehnOZX6rrw.I.ruJBfSt1cpc.4CISLeIVHvZucrMSJbWJ.','1621499819@qq.com',NULL,'',1,0,'个人简介','个人网站',0,0,0,'2025-09-27 11:07:11','2025-09-27 11:07:11',-1,-1,0),(13,'demo01','用户-ZGVtbzAx','$2a$10$/ucuRFpHYXojVXvFuU5Quu3Q5.XAAxXSm2iJAsXglx8q74CZI6iYK','zydot998@qq.com','','',1,0,'个人简介','个人网站',0,0,0,'2025-09-27 13:44:11','2025-09-27 13:44:11',NULL,NULL,0),(14,'demo02','用户-ZGVtbzAy','$2a$10$pIuyaTsOr6VYAEuxpuhUu.PXOl5UCemxxMTVtyRGbuiCDfDK2ZOk6','','','',1,0,'个人简介','个人网站',0,0,0,'2025-09-27 13:44:40','2025-09-27 13:44:40',NULL,NULL,0),(15,'demo03','用户-ZGVtbzAz','$2a$10$YdBrCF.4F26YMg9AzbEXPOua/Zn6cbKLMTyzGsAs5TjTzafHiqwca','','','',1,0,'个人简介','个人网站',0,0,0,'2025-09-27 13:44:46','2025-09-27 13:44:46',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除;1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,1,'2025-09-27 22:58:41','2025-09-27 22:58:47',NULL,NULL,0);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-28  4:48:15
