/*
Navicat MySQL Data Transfer

Source Server         : spring-china
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : spring-china

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-06-10 16:13:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '帖子创建时间戳',
  `updatedAt` timestamp NULL DEFAULT NULL,
  `categoryId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` longtext NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除，-1：已删除，0：草稿箱，1：正常',
  `isTop` bit(1) DEFAULT b'0' COMMENT '是否置顶，0：不置顶，1：置顶',
  `visitCount` int(11) DEFAULT NULL,
  `contentHtml` longtext,
  `isDigest` bit(1) DEFAULT NULL,
  `likeCount` int(11) DEFAULT NULL,
  `commentCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_topic_fk_idx` (`categoryId`),
  KEY `user_topic_fk_idx` (`userId`),
  CONSTRAINT `category_topic_fk` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_topic_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic_comment
-- ----------------------------
DROP TABLE IF EXISTS `topic_comment`;
CREATE TABLE `topic_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `topicId` bigint(20) NOT NULL,
  `content` longtext NOT NULL,
  `contentHtml` longtext NOT NULL,
  `stickTop` bit(1) DEFAULT b'0' COMMENT '是否置顶，0：不置顶，1：置顶',
  `likeCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `topic_topic_comment_fk_idx` (`topicId`),
  KEY `user_topic_comment_fk_idx` (`userId`),
  CONSTRAINT `topic_topic_comment_fk` FOREIGN KEY (`topicId`) REFERENCES `topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_topic_comment_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `topic_comment_like`;
CREATE TABLE `topic_comment_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `commentId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `topic_comment_topic_comment_like_fk_idx` (`commentId`),
  KEY `user_topic_comment_like_fk_idx` (`userId`),
  CONSTRAINT `topic_comment_topic_comment_like_fk` FOREIGN KEY (`commentId`) REFERENCES `topic_comment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_topic_comment_like_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic_like
-- ----------------------------
DROP TABLE IF EXISTS `topic_like`;
CREATE TABLE `topic_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `topicId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `topic_topic_like_fk_idx` (`topicId`),
  KEY `user_topic_like_fk_idx` (`userId`),
  CONSTRAINT `topic_topic_like_fk` FOREIGN KEY (`topicId`) REFERENCES `topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_topic_like_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for topic_view
-- ----------------------------
DROP TABLE IF EXISTS `topic_view`;
CREATE TABLE `topic_view` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `topicId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `topic_topic_view_fk_idx` (`topicId`),
  KEY `user_topic_view_fk_idx` (`userId`),
  CONSTRAINT `topic_topic_view_fk` FOREIGN KEY (`topicId`) REFERENCES `topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_topic_view_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userName` varchar(20) NOT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `nickName` varchar(20) NOT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `password` char(32) DEFAULT NULL,
  `passwordSalt` char(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `roleName` varchar(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '角色，0：普通用户，1：VIP，2：管理员',
  PRIMARY KEY (`id`),
  KEY `user_user_role_fk_idx` (`userId`),
  CONSTRAINT `user_user_role_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
