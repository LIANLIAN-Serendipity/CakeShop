/*
 Navicat Premium Data Transfer

 Source Server         : zhouxuelian
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : cakeshop

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 26/06/2025 13:39:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cake
-- ----------------------------
DROP TABLE IF EXISTS `cake`;
CREATE TABLE `cake`  (
  `cakeId` int(0) NOT NULL AUTO_INCREMENT,
  `cakeName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `introduce` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `price` float(10, 2) NULL DEFAULT NULL,
  `cakePicture` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `caketypeId` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`cakeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cake
-- ----------------------------
INSERT INTO `cake` VALUES (1, '毛巾卷', '毛巾卷蛋糕以薄如纸的可丽饼裹着丰盈奶油，似卷起的柔软毛巾。奶油多选用动物奶油，轻盈绵密，入口即化带淡淡乳香，可夹水果或淋酱。撕开时奶油微微溢出，口感柔韧与顺滑交织，是快手又治愈的甜点。', 89.90, 'http://192.168.131.210:8089/zhouxuelian9/upload/e2ec3e7f-ab0d-49a3-8ee7-a6f8c73b088b.png', 1);
INSERT INTO `cake` VALUES (2, '芒果慕斯', '芒果慕斯裹着阳光般的金黄，底层是烤得焦香的黄油饼干碎，中间夹着大块芒果果肉。慕斯体用新鲜果泥混合淡奶油，入口像咬开流心的芒果冰淇淋，酸甜里裹着奶香，顶部再插片薄荷叶，清爽得像把热带果园吃进嘴里。', 29.80, 'http://192.168.131.210:8089/zhouxuelian9/upload/7b94d670-58c8-42fe-bd74-5f998392e710.png', 2);
INSERT INTO `cake` VALUES (4, '抹茶千层', '芒果慕斯裹着阳光般的金黄，底层是烤得焦香的黄油饼干碎，中间夹着大块芒果果肉。慕斯体用新鲜果泥混合淡奶油，入口像咬开流心的芒果冰淇淋，酸甜里裹着奶香，顶部再插片薄荷叶，清爽得像把热带果园吃进嘴里。', 58.90, 'http://192.168.131.210:8089/zhouxuelian9/upload/17d8f571-3ab2-4b5f-9385-a8f6ca2914af.png', 3);
INSERT INTO `cake` VALUES (5, '樱桃果酱', '樱桃果酱奶油蛋糕以松软戚风为底，抹上绵密奶油后，铺上厚厚一层现熬樱桃果酱，颗颗果肉裹着浓稠酱汁，咬开时酸甜爆浆。表面用奶油挤出花瓣纹路，点缀糖渍樱桃与巧克力碎，粉白红三色交织，像把春日果园封进蛋糕，奶油的柔与果酱的野碰撞出活泼滋味。', 52.50, 'http://192.168.131.210:8089/zhouxuelian9/upload/5ad56e0f-5a82-45ae-bc7d-d70fc1189c1a.png', 1);
INSERT INTO `cake` VALUES (6, '草莓慕斯', '111111', 58.00, 'http://192.168.131.210:8089/zhouxuelian9/upload/f4e7c0dc-1bd0-4b20-8cbe-bc367090052a.png', 2);
INSERT INTO `cake` VALUES (7, '蓝莓千层', '蓝莓千层是一款非常好吃的蛋糕', 88.00, 'http://192.168.131.210:8089/zhouxuelian9/upload/5aae9b6d-f435-440f-9ece-ad87373d885e.png', 3);

-- ----------------------------
-- Table structure for cakeorderdetail
-- ----------------------------
DROP TABLE IF EXISTS `cakeorderdetail`;
CREATE TABLE `cakeorderdetail`  (
  `cakedetailId` int(0) NOT NULL AUTO_INCREMENT,
  `cakeorderId` int(0) NULL DEFAULT NULL,
  `cakeId` int(0) NULL DEFAULT NULL,
  `num` int(0) NULL DEFAULT NULL,
  `subtotal` float(100, 2) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cakedetailId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cakeorderdetail
-- ----------------------------
INSERT INTO `cakeorderdetail` VALUES (1, 1, 1, 2, 129.00, '七分糖');
INSERT INTO `cakeorderdetail` VALUES (2, 2, 2, 2, 129.00, '无糖');
INSERT INTO `cakeorderdetail` VALUES (3, 3, 4, 1, 129.00, '多放水果');
INSERT INTO `cakeorderdetail` VALUES (4, 4, 5, 1, 129.00, '奥利奥夹心');
INSERT INTO `cakeorderdetail` VALUES (5, 5, 6, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (6, 6, 4, 1, 129.00, '111');
INSERT INTO `cakeorderdetail` VALUES (7, 7, 3, 2, 129.00, '11111111111');
INSERT INTO `cakeorderdetail` VALUES (8, 8, 2, 3, 129.00, '55');
INSERT INTO `cakeorderdetail` VALUES (9, 9, 2, 4, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (10, 10, 1, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (11, 11, 4, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (12, 12, 3, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (13, 13, 2, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (14, 14, 4, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (15, 15, 5, 1, 129.00, NULL);
INSERT INTO `cakeorderdetail` VALUES (18, 20, 5, 1, 32.50, '1');
INSERT INTO `cakeorderdetail` VALUES (19, 21, 4, 1, 58.00, '早点送到');
INSERT INTO `cakeorderdetail` VALUES (20, 22, 4, 1, 58.00, '1');
INSERT INTO `cakeorderdetail` VALUES (21, 23, 4, 1, 58.00, '放到家门口');
INSERT INTO `cakeorderdetail` VALUES (22, 24, 4, 1, 58.00, '2');
INSERT INTO `cakeorderdetail` VALUES (23, 25, 5, 1, 32.50, '3');
INSERT INTO `cakeorderdetail` VALUES (24, 26, 3, 1, 9.90, '1');
INSERT INTO `cakeorderdetail` VALUES (25, 27, 5, 1, 32.50, '2');
INSERT INTO `cakeorderdetail` VALUES (26, 28, 1, 1, 59.80, '3');
INSERT INTO `cakeorderdetail` VALUES (27, 29, 5, 1, 32.50, '1');
INSERT INTO `cakeorderdetail` VALUES (28, 30, 2, 1, 29.80, '1');
INSERT INTO `cakeorderdetail` VALUES (29, 31, 3, 1, 9.90, '2');
INSERT INTO `cakeorderdetail` VALUES (30, 32, 3, 1, 9.90, '3');
INSERT INTO `cakeorderdetail` VALUES (31, 33, 1, 1, 89.90, '三分糖');
INSERT INTO `cakeorderdetail` VALUES (32, 33, 5, 1, 52.50, '三分糖');

-- ----------------------------
-- Table structure for cakeorderinfo
-- ----------------------------
DROP TABLE IF EXISTS `cakeorderinfo`;
CREATE TABLE `cakeorderinfo`  (
  `cakeorderId` int(0) NOT NULL AUTO_INCREMENT,
  `cakeuserId` int(0) NULL DEFAULT NULL,
  `cakeorderTime` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `caketotalPrice` float NULL DEFAULT NULL,
  `cakeorderStatus` int(0) NULL DEFAULT NULL,
  `deliveryAddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `customerName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `customerPhone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cakeorderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cakeorderinfo
-- ----------------------------
INSERT INTO `cakeorderinfo` VALUES (1, 1, '2025-06-26 16:51:18', 79, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (2, 1, '2025-06-26 16:51:18', 88, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (3, 5, '2025-06-27 07:55:30', 62.3, 3, '福建省', 'lql', '15926348730');
INSERT INTO `cakeorderinfo` VALUES (4, 4, '2025-06-26 16:42:48', 68, 1, '福建省', 'yn', '14875963201');
INSERT INTO `cakeorderinfo` VALUES (5, 3, '2025-06-26 16:52:01', 9.99, 2, '福建省', 'wllb', '12345678980');
INSERT INTO `cakeorderinfo` VALUES (6, 2, '2025-06-26 16:51:18', 59, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (7, 2, '2025-06-26 16:51:18', 59, 2, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (8, 1, '2025-06-26 16:51:18', 0, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (9, 3, '2025-06-27 07:54:53', 67.9, 3, '福建省', 'yn', NULL);
INSERT INTO `cakeorderinfo` VALUES (10, 4, '2025-06-26 16:41:56', 20, 3, '福建省', 'lxy', NULL);
INSERT INTO `cakeorderinfo` VALUES (11, 5, '2025-06-26 17:11:52', 15.96, 1, '福建省', 'lql', NULL);
INSERT INTO `cakeorderinfo` VALUES (12, 3, '2025-06-26 17:09:00', 11.97, 1, '福建省', 'wllb', NULL);
INSERT INTO `cakeorderinfo` VALUES (20, 2, '2025-06-18 15:08:37', 32.5, 1, '1', '123', '1');
INSERT INTO `cakeorderinfo` VALUES (21, 6, '2025-06-24 23:46:32', 58, 1, '福建省三明市三元区', '小猪', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (22, 2, '2025-06-26 16:51:18', 58, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (23, 4, '2025-06-26 16:51:18', 0, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (24, 2, '2025-06-26 16:51:18', 79, 1, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (25, 3, '2025-06-25 00:43:15', 32.5, 1, '3', '3', '3');
INSERT INTO `cakeorderinfo` VALUES (26, 4, '2025-06-25 00:49:06', 9.9, 1, '1', '1', '1');
INSERT INTO `cakeorderinfo` VALUES (27, 2, '2025-06-25 02:43:36', 32.5, 1, '2', '2', '2');
INSERT INTO `cakeorderinfo` VALUES (28, 5, '2025-06-25 02:46:32', 59.8, 1, '3', '3', '3');
INSERT INTO `cakeorderinfo` VALUES (29, 2, '2025-06-25 02:49:33', 32.5, 1, '1', '1', '1');
INSERT INTO `cakeorderinfo` VALUES (30, 2, '2025-06-25 20:07:50', 29.8, 1, '1', '1', '1');
INSERT INTO `cakeorderinfo` VALUES (31, 1, '2025-06-25 20:13:56', 9.9, 1, '2', '2', '2');
INSERT INTO `cakeorderinfo` VALUES (32, 0, '2025-06-26 16:51:18', 0, 2, '福建省', 'lxy', '12345678910');
INSERT INTO `cakeorderinfo` VALUES (33, 1, '2025-06-26 04:09:51', 142.4, 1, '福建省三明市三元区', 'zhuzhu', '1345871120');

-- ----------------------------
-- Table structure for caketype
-- ----------------------------
DROP TABLE IF EXISTS `caketype`;
CREATE TABLE `caketype`  (
  `caketypeId` int(0) NOT NULL,
  `caketypeName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`caketypeId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of caketype
-- ----------------------------
INSERT INTO `caketype` VALUES (1, '奶油蛋糕');
INSERT INTO `caketype` VALUES (2, '慕斯蛋糕');
INSERT INTO `caketype` VALUES (3, '千层蛋糕');

-- ----------------------------
-- Table structure for cakeuserinfo
-- ----------------------------
DROP TABLE IF EXISTS `cakeuserinfo`;
CREATE TABLE `cakeuserinfo`  (
  `cakeuserId` int(0) NOT NULL AUTO_INCREMENT,
  `cakeusername` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `cakeuserImage` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cakeuserId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cakeuserinfo
-- ----------------------------
INSERT INTO `cakeuserinfo` VALUES (1, 'zxl', '123', '女', '海南省海口市', '18686560986', 'http://192.168.131.210:8089/zhouxuelian9/upload/4eb51f3d-e1d3-4819-ab68-7a75fd0426cf.png', '1');
INSERT INTO `cakeuserinfo` VALUES (2, 'lxy', '123', '女', '福建省三明市永安市', '12345678910', 'http://192.168.131.210:8089/zhouxuelian9/upload/5905fe41-0494-4647-99a1-45f656092126.png', '2');
INSERT INTO `cakeuserinfo` VALUES (13, 'yn', '123', '女', '福建省漳州市', '14254687952', 'http://192.168.131.210:8089/zhouxuelian9/upload/bf829c4c-9e3b-4d4c-8351-4392e47eb804.png', '1');
INSERT INTO `cakeuserinfo` VALUES (14, 'sjx', '123', '女', '福建省泉州市', '19854621302', 'http://192.168.131.210:8089/zhouxuelian9/upload/7c17f1a5-29d9-4fe6-a66d-68e6c02a3c3e.png', '2');
INSERT INTO `cakeuserinfo` VALUES (15, 'wllb', '111', '女', '福建省三明市大田县', '17465841256', 'http://192.168.131.210:8089/zhouxuelian9/upload/longlong.jpg', '2');
INSERT INTO `cakeuserinfo` VALUES (16, 'lql', '123', '女', '福建省三明市', '12345678912', 'http://192.168.131.210:8089/zhouxuelian9/upload/niuniu.jpg', '2');
INSERT INTO `cakeuserinfo` VALUES (18, '12', '1', '男', '1', '1', 'http://192.168.131.210:8089/zhouxuelian9/upload/78851037-8dfa-4580-ac34-9a349f877eaa.png', '2');
INSERT INTO `cakeuserinfo` VALUES (20, 'bb', '123', '女', '123', '12345678910', 'http://192.168.131.210:8089/zhouxuelian9/upload/9250eef2-f57f-48bd-a321-128735f971c6.jpg', '2');
INSERT INTO `cakeuserinfo` VALUES (22, 'zz', '123', '男', 'zx', '123', 'http://192.168.131.210:8089/zhouxuelian9/upload/8e592200-8751-4dc5-80e9-e93942ae4803.png', '2');

-- ----------------------------
-- Table structure for interceptioninfo
-- ----------------------------
DROP TABLE IF EXISTS `interceptioninfo`;
CREATE TABLE `interceptioninfo`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `msg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interceptioninfo
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `rolename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员');
INSERT INTO `role` VALUES ('2', '普通用户');

SET FOREIGN_KEY_CHECKS = 1;
