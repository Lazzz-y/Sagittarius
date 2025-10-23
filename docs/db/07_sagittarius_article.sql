USE `sagittarius_article`;


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
DROP TABLE IF EXISTS `article_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_audit_log` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `article_id` bigint unsigned NOT NULL COMMENT '文章ID',
  `auditor_id` bigint unsigned NOT NULL COMMENT '审核员ID（关联用户表）',
  `audit_time` datetime NOT NULL COMMENT '审核时间',
  `audit_result` tinyint NOT NULL COMMENT '审核结果：1-通过 2-驳回',
  `comment` varchar(500) DEFAULT NULL COMMENT '审核意见（驳回时必填）',
  `before_status` tinyint NOT NULL COMMENT '审核前状态',
  `after_status` tinyint NOT NULL COMMENT '审核后状态',
  `tenant_id` bigint unsigned NOT NULL COMMENT '租户ID（关联租户表）',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID（关联用户表）',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID（关联用户表）',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章审核日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `article_audit_log` WRITE;
/*!40000 ALTER TABLE `article_audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_audit_log` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `article_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_media` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `article_id` bigint unsigned NOT NULL COMMENT '文章ID',
  `file_id` varchar(64) NOT NULL COMMENT '文件模块返回的唯一标识（如Minio的objectId）',
  `file_url` varchar(512) NOT NULL COMMENT '文件访问URL（Minio+CDN）',
  `file_type` tinyint NOT NULL COMMENT '文件类型：1-图片 2-视频 3-音频',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序（如封面图排第1）',
  `width` int DEFAULT NULL COMMENT '图片宽度（仅图片类型）',
  `height` int DEFAULT NULL COMMENT '图片高度（仅图片类型）',
  `size` bigint NOT NULL COMMENT '文件大小（字节）',
  `tenant_id` bigint unsigned NOT NULL COMMENT '租户ID（关联租户表）',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID（关联用户表）',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID（关联用户表）',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章-多媒体关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `article_media` WRITE;
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `article_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_meta` (
  `id` bigint unsigned NOT NULL COMMENT '文章ID（主键）',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要（可选，用于列表页展示）',
  `author_id` bigint NOT NULL COMMENT '作者ID（关联用户表）',
  `category_id` bigint NOT NULL COMMENT '分类ID（关联分类表）',
  `status` tinyint NOT NULL COMMENT '状态：0-草稿 1-待审核 2-审核通过 3-驳回',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '阅读量',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数',
  `is_recommended` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐：0-否 1-是',
  `mongo_doc_id` varchar(64) DEFAULT NULL COMMENT '关联MongoDB的文档ID（存储正文）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `submit_audit_time` datetime DEFAULT NULL COMMENT '提交审核时间',
  `publish_time` datetime DEFAULT NULL COMMENT '审核通过（发布）时间',
  `tenant_id` bigint unsigned NOT NULL COMMENT '租户ID（关联租户表）',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID（关联用户表）',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID（关联用户表）',
  PRIMARY KEY (`id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status_create_time` (`status`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章主表（元数据）';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `article_meta` WRITE;
/*!40000 ALTER TABLE `article_meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_meta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


SET FOREIGN_KEY_CHECKS = 1;
