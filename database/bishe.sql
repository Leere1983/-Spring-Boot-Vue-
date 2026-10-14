/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3309
 Source Server Type    : MySQL
 Source Server Version : 50562 (5.5.62)
 Source Host           : localhost:3309
 Source Schema         : bishe

 Target Server Type    : MySQL
 Target Server Version : 50562 (5.5.62)
 File Encoding         : 65001

 Date: 29/05/2026 20:44:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '帐号',
  `pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-正常,1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (1, 'admin', 'admin', 0);

-- ----------------------------
-- Table structure for dingdanfahuo
-- ----------------------------
DROP TABLE IF EXISTS `dingdanfahuo`;
CREATE TABLE `dingdanfahuo`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `jingpaidingdanid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '竞拍订单id',
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `shangpintupian` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品图片',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `dingdanbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
  `wuliugongsi` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流公司',
  `wuliudanhao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流单号',
  `fahuobeizhu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发货备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发货时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dingdanfahuo_jingpaidingdanid_index`(`jingpaidingdanid`) USING BTREE,
  INDEX `dingdanfahuo_fenlei_index`(`fenlei`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单发货' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dingdanfahuo
-- ----------------------------
INSERT INTO `dingdanfahuo` VALUES (3, 4, '04100746245', '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', '963', '100', 1, '031118162382', '京东', '45544545', '1', '2026-03-11 18:18:50');
INSERT INTO `dingdanfahuo` VALUES (4, 8, '041007496829', '劳力士手表', '/upload/d6e36ef746d8d3004240aa35a56f1676.png', '1559938615', 'tianming200', 2, 'JP17788698074758872', '京东', '1341456416', '', '2026-05-16 03:29:06');

-- ----------------------------
-- Table structure for dingdanpingjia
-- ----------------------------
DROP TABLE IF EXISTS `dingdanpingjia`;
CREATE TABLE `dingdanpingjia`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `jingpaidingdanid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '竞拍订单id',
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `dingdanbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `pingfen` int(11) NOT NULL DEFAULT 0 COMMENT '评分',
  `pingjia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评价',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dingdanpingjia_jingpaidingdanid_index`(`jingpaidingdanid`) USING BTREE,
  INDEX `dingdanpingjia_fenlei_index`(`fenlei`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单评价' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dingdanpingjia
-- ----------------------------
INSERT INTO `dingdanpingjia` VALUES (3, 4, '04100746245', '古画寻芳', 1, '100', '031118162382', '963', 5, '法国恢复规划法规恢复规划发给和', '2026-03-11 18:19:20');
INSERT INTO `dingdanpingjia` VALUES (4, 8, '041007496829', '劳力士手表', 2, 'tianming200', 'JP17788698074758872', '1559938615', 5, '好', '2026-05-16 03:30:18');

-- ----------------------------
-- Table structure for gonggao
-- ----------------------------
DROP TABLE IF EXISTS `gonggao`;
CREATE TABLE `gonggao`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `biaoti` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `neirong` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `faburiqi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布日期',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `issh` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否审核',
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of gonggao
-- ----------------------------
INSERT INTO `gonggao` VALUES (1, '这个瓷器精品展为嘉德香港春拍揭开序幕', '<p><a style=\"overflow-wrap: normal; color: rgb(51, 51, 51); text-decoration: none;\" href=\"http://www.paiorg.com/user/4075/upload/25-03-12/1741743894_656_afile.jpg\" target=\"_blank\" rel=\"noopener\"><img style=\"overflow-wrap: normal; border: 1px outset rgb(238, 238, 238);\" title=\"简素为绚！这个瓷器精品展为嘉德香港春拍揭开序幕\" src=\"http://www.paiorg.com/user/4075/upload/25-03-12/1741743894_656_afile.jpg\" alt=\"简素为绚！这个瓷器精品展为嘉德香港春拍揭开序幕\" width=\"600\" height=\"260\"></a></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">中国嘉德香港2025年春拍将于4月开幕。本次春拍将涵盖中国书画、亚洲二十世纪及当代艺术、瓷器及工艺品、金石雅玩、生活尚品等五大板块，汇聚1500件珍品。&nbsp;</p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">在春拍正式拉开之前，嘉德香港将于3月陆续在北京、上海、杭州、香港举办精品巡展。首站北京瓷器工艺品精品展3月9日起在北京嘉德艺术中心亮相。此次春拍，瓷器、玉器、文玩杂项等共有700余件珍品，北京精品展精选其中26件拍品，集中展示。</p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">同时，嘉德香港还携手竹月堂推出新书《简素为绚&mdash;&mdash;吉美博物馆及竹月堂藏中国五至十八世纪一道釉》。该书汇聚了知名瓷器收藏家简永桢30年的收藏精华和吉美博物馆的部分藏品，由香港中文大学中国文化研究所名誉研究员林业强编写，深度挖掘、系统阐述中国五至十八世纪单色釉瓷器的艺术魅力与文化内涵。简永桢特意精选10件精彩藏品，在北京嘉德艺术中心公开展出三天 。</p>', '2026-03-07 17:15:35', 'admin', '是', 0);
INSERT INTO `gonggao` VALUES (2, '近7000 件艺术珍品集结一堂，北京保利2024秋拍预展启幕', '<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">12月5日，北京保利2024秋拍预展于北京国贸大酒店启幕。本季秋拍共计26个专场、近7000件艺术珍品。秋拍期间，北京保利拍卖各平台官方视频账号还将同步推出十余场直播活动，带藏家身临其境感受预展与拍卖的每一个精彩瞬间。</p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">预展现场，中国书画板块将呈献近现代及当代书画大师名家精彩佳作600余件，包括傅抱石&ldquo;金刚坡时期&rdquo;代表作《对弈图》《洗手图》《梅花道人诗意》，吴冠中&ldquo;水乡题材&rdquo;经典油画《江南春早》，弘一晚年书法代表作《行书〈菜根谭〉》，民国天津富商范竹斋珍藏张大千仿古十二屏之一《仿王蒙清渰垂钓图》，李可染山水写生代表作《鱼米之乡》等名作。</p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">古代书画板块部分，由过云楼旧藏任熊《姚大梅诗意图册》领衔；牌记罕见、品相完美、且拥有明确年款的元代全本《新增说文韵府群玉》领衔古籍文献部分。瓷器佛像艺术品方面，堪为存世孤品的&ldquo;明成化青花外海石榴纹内梵文卧足碗 &rdquo;作为本季中国古董珍玩部分的扛鼎之作。现当代艺术部则精心筹备了 180余幅油画与雕塑佳作， 既囊括陈逸飞、刘炜、曾梵志等中坚力量，黄宇兴、秦琦、韦嘉、马轲等市场热门，还有肖江、张书笺等艺术家的作品。</p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 2em;\">此外，珠宝钟表尚品展区汇聚了大气非凡的满绿翡翠、艳丽浓郁的珍稀彩色宝石、光影盈动的钻石，以及各类品牌和珍贵钟表，展现珠宝钟表的优雅时光之美。 邮品钱币部分带来了邮票、钱币及信札三个专题的精品，名酒茗品部分则带来一 系列经典普洱茶，展现中国传统文化的深厚底蕴。</p>', '2026-03-07 17:15:35', 'admin', '是', 0);
INSERT INTO `gonggao` VALUES (3, '利永紫砂联手淘宝拍卖会 推进O2O电商全网战略布局', '<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">业内人士预计，此次双方强强联手，势必将推动紫砂产业<span style=\"overflow-wrap: normal;\">020</span>全局战略大跨步向前发展，既打开了行业拍卖新格局，又翻开了紫砂业的新篇章，传扬中国文化，弘扬中华国粹。正如业内人士评论所言：线上拍卖就是为喜好者提供一个简便的艺术品交易平台，使更多不同层次的人进入这个圈子，让艺术品消费进入生活，从而开拓更广阔的艺术品市场。</span></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21.1pt;\"><strong style=\"overflow-wrap: normal;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">未来，拍卖会将成为紫砂聚集地</span></strong></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">诸如紫砂艺术机构，如何和互联网联合？曾有淘宝拍卖人士表示，自行搭建平台是一种方式，而更加方便可行的，是和淘宝合作，充分利用淘宝网几亿用户，潜在的未来客户优势，建立自己的在线业务存在，让艺术品不再是土豪的玩物，而是触及正在崛起的中国&ldquo;殷实阶级&rdquo;。</span></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">显然，利永准确地判断出了这一点。据了解，淘宝拍卖会与百年利永于仪式上同时宣布，双方即将启动超过<span style=\"overflow-wrap: normal;\">3</span>亿元的战略合作。此外，利永还将拿出现代壶艺泰斗顾景舟绝世藏品之一，在淘宝拍卖会上进行拍卖。两个重大举动，无疑将对行业产生天翻地覆的影响。</span></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">&nbsp;</span></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\">业内人士认为，此举拓宽了大众购买艺术品的渠道，也使中端消费人群有机会在网络购物体验中触及艺术品领域。未来，淘宝拍卖会平台将成为紫砂藏品面世聚集地，成为有着传统文化影响力的平台。而百年利永与淘宝的联手，也将被视为一次典型的传统文化与互联网科技的跨界合作案例，它让古韵文化从此焕发出新的活力。</span></p>\n<p style=\"overflow-wrap: normal; margin: 5px 0px; padding: 0px; text-indent: 21pt;\"><span style=\"overflow-wrap: normal; line-height: 24px; font-family: 宋体;\"><img style=\"overflow-wrap: normal; border: 0px;\" src=\"http://www.paiorg.com/user/4215/upload/14-09-15/1410747302_mfile_54164ba6d5517.jpg\" alt=\"\" width=\"600\" height=\"400\"></span></p>', '2026-03-07 17:15:35', 'admin', '是', 0);

-- ----------------------------
-- Table structure for jingpaidingdan
-- ----------------------------
DROP TABLE IF EXISTS `jingpaidingdan`;
CREATE TABLE `jingpaidingdan`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `shangpinxinxiid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品信息id',
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `shangpintupian` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品图片',
  `dangqianjingpaijia` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '当前竞拍价',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `dingdanbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
  `dingdanzhuangtai` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单状态',
  `zhifujine` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
  `shouxufei` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '手续费',
  `lianxiren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人',
  `lianxidianhua` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `lianxidizhi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系地址',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `issh` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否审核',
  `iszf` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否支付',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `jingpaidingdan_shangpinxinxiid_index`(`shangpinxinxiid`) USING BTREE,
  INDEX `jingpaidingdan_fenlei_index`(`fenlei`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '竞拍订单' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of jingpaidingdan
-- ----------------------------
INSERT INTO `jingpaidingdan` VALUES (4, 1, '04100746245', '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', 20000.00, '963', '100', 1, '031118162382', '已评价', 20200.00, 200.00, '张三', '13496396396', '新疆乌鲁木齐', '2026-03-11 18:16:17', '是', '是');
INSERT INTO `jingpaidingdan` VALUES (8, 4, '041007496829', '劳力士手表', '/upload/d6e36ef746d8d3004240aa35a56f1676.png', 100000.00, '1559938615', 'tianming200', 2, 'JP17788698074758872', '已评价', 101000.00, 1000.00, '王明', '17389784333', '新疆乌鲁木齐', '2026-05-16 03:22:45', '是', '是');

-- ----------------------------
-- Table structure for jingpaijilu
-- ----------------------------
DROP TABLE IF EXISTS `jingpaijilu`;
CREATE TABLE `jingpaijilu`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `shangpinxinxiid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品信息id',
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `shangpintupian` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品图片',
  `qipaijia` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '起拍价',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `jiajiafudu` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '加价幅度',
  `jingpaijia` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '竞拍价',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `jingpaijilu_shangpinxinxiid_index`(`shangpinxinxiid`) USING BTREE,
  INDEX `jingpaijilu_fenlei_index`(`fenlei`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '竞拍记录' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of jingpaijilu
-- ----------------------------
INSERT INTO `jingpaijilu` VALUES (8, 1, '04100746245', 1, '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', 15000.00, '100', 1000.00, 16000.00, '963', '2026-03-09 18:13:41');
INSERT INTO `jingpaijilu` VALUES (9, 1, '04100746245', 1, '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', 15000.00, '100', 1000.00, 17000.00, '001', '2026-03-09 18:14:13');
INSERT INTO `jingpaijilu` VALUES (10, 1, '04100746245', 1, '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', 15000.00, '100', 1000.00, 20000.00, '963', '2026-03-09 18:14:43');
INSERT INTO `jingpaijilu` VALUES (18, 4, '041007496829', 2, '劳力士手表', '/upload/d6e36ef746d8d3004240aa35a56f1676.png', 50000.00, 'tianming200', 10000.00, 100000.00, '1559938615', '2026-05-16 03:21:48');

-- ----------------------------
-- Table structure for liuyanban
-- ----------------------------
DROP TABLE IF EXISTS `liuyanban`;
CREATE TABLE `liuyanban`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `xingming` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `lianxidianhua` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `fankuineirong` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反馈内容',
  `fankuiyonghu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈用户',
  `fankuizhuangtai` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '反馈状态',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '反馈时间',
  `fankuihuifu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反馈回复',
  `huifushijian` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '回复时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '留言板' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of liuyanban
-- ----------------------------
INSERT INTO `liuyanban` VALUES (1, '小明', '13656566555', '您好', '001', '已回复', '2026-03-07 17:50:56', '您好', '2026-03-07 17:52:46');
INSERT INTO `liuyanban` VALUES (2, '李丽丽', '13499999999', '您好', '963', '已回复', '2026-03-11 18:19:29', '您好', '2026-03-11 18:19:44');

-- ----------------------------
-- Table structure for lunbotu
-- ----------------------------
DROP TABLE IF EXISTS `lunbotu`;
CREATE TABLE `lunbotu`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '连接地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '轮播图' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of lunbotu
-- ----------------------------
INSERT INTO `lunbotu` VALUES (1, '23442', '/upload/35b259a290a14015a673dd5d17d34d29.jpg', '#');
INSERT INTO `lunbotu` VALUES (2, '23432432', '/upload/cadd5a25f7e568ba0a455efb0fa1be52.jpg', '#');

-- ----------------------------
-- Table structure for querenshouhuo
-- ----------------------------
DROP TABLE IF EXISTS `querenshouhuo`;
CREATE TABLE `querenshouhuo`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `jingpaidingdanid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '竞拍订单id',
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `shangpintupian` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品图片',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `dingdanbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单编号',
  `shouhuobeizhu` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收货备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收货时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `querenshouhuo_jingpaidingdanid_index`(`jingpaidingdanid`) USING BTREE,
  INDEX `querenshouhuo_fenlei_index`(`fenlei`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '确认收货' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of querenshouhuo
-- ----------------------------
INSERT INTO `querenshouhuo` VALUES (3, 4, '04100746245', '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', '963', '100', 1, '031118162382', '11', '2026-03-11 18:19:11');
INSERT INTO `querenshouhuo` VALUES (4, 8, '041007496829', '劳力士手表', '/upload/d6e36ef746d8d3004240aa35a56f1676.png', '1559938615', 'tianming200', 2, 'JP17788698074758872', '', '2026-05-16 03:29:16');

-- ----------------------------
-- Table structure for shangjia
-- ----------------------------
DROP TABLE IF EXISTS `shangjia`;
CREATE TABLE `shangjia`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `zhanghao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号',
  `mima` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `dianpumingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '店铺名称',
  `qianbaoyue` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '钱包余额',
  `fuzeren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '负责人',
  `shenfenzheng` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证',
  `dianhua` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '电话',
  `youxiang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `dizhi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '地址',
  `jianjie` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简介',
  `zizhizhengshu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资质证书',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-正常,1-已删除)',
  `issh` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否审核',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商家' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shangjia
-- ----------------------------
INSERT INTO `shangjia` VALUES (1, '100', '100', '天美拍卖行', 35722.00, '张三', '230781198305226701', '13656565655', '56565@qq.com', '北京101', '天美拍卖行', '/upload/101dc94571ae96444affc7c40b99bf15.png', 0, '是');
INSERT INTO `shangjia` VALUES (2, 'tianming200', '123456', '天铭拍卖行', 1090000.00, '李四', '310230196902143721', '13787877777', '6776@qq.com', '北京1055', '天铭拍卖行', '/upload/a6d206befae79aa5b3e13a95495c66b8.png', 0, '是');

-- ----------------------------
-- Table structure for shangjiatixian
-- ----------------------------
DROP TABLE IF EXISTS `shangjiatixian`;
CREATE TABLE `shangjiatixian`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `shangjiaid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '商家id',
  `zhanghao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号',
  `dianpumingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '店铺名称',
  `fuzeren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '负责人',
  `tixianjine` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '提现金额',
  `tixianriqi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提现日期',
  `tixianshangjia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '提现商家',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `shangjiatixian_shangjiaid_index`(`shangjiaid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商家提现' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shangjiatixian
-- ----------------------------
INSERT INTO `shangjiatixian` VALUES (1, 1, '100', '天美拍卖行', '张三', 1.00, '2025-04-10 07:40:15', 'admin');
INSERT INTO `shangjiatixian` VALUES (3, 1, '100', '天美拍卖行', '张三', 1234.00, '2026-03-07 17:35:46', '100');

-- ----------------------------
-- Table structure for shangpinfenlei
-- ----------------------------
DROP TABLE IF EXISTS `shangpinfenlei`;
CREATE TABLE `shangpinfenlei`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `fenleimingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品分类' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shangpinfenlei
-- ----------------------------
INSERT INTO `shangpinfenlei` VALUES (1, '古董艺术');
INSERT INTO `shangpinfenlei` VALUES (2, '名贵珠宝');
INSERT INTO `shangpinfenlei` VALUES (3, '汽车房产');

-- ----------------------------
-- Table structure for shangpinxinxi
-- ----------------------------
DROP TABLE IF EXISTS `shangpinxinxi`;
CREATE TABLE `shangpinxinxi`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `shangpinbianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编号',
  `fenlei` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '分类',
  `shangpinmingcheng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `shangpintupian` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品图片',
  `qipaijia` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '起拍价',
  `jiajiafudu` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '加价幅度',
  `dangqianjingpaijia` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '当前竞拍价',
  `qipaishijian` datetime NULL DEFAULT NULL COMMENT '起拍时间',
  `jieshushijian` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `jingpairenshu` int(11) NOT NULL DEFAULT 0 COMMENT '竞拍人数',
  `shangpinxiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品详情',
  `shangpinzhuangtai` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品状态',
  `jingpairen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '竞拍人',
  `faburen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发布人',
  `issh` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否审核',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-正常,1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `shangpinxinxi_fenlei_index`(`fenlei`) USING BTREE,
  INDEX `idx_qipaishijian`(`qipaishijian`) USING BTREE,
  INDEX `idx_jieshushijian`(`jieshushijian`) USING BTREE,
  INDEX `idx_shangpinzhuangtai`(`shangpinzhuangtai`) USING BTREE,
  INDEX `idx_issh`(`issh`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shangpinxinxi
-- ----------------------------
INSERT INTO `shangpinxinxi` VALUES (1, '04100746245', 1, '古画寻芳', '/upload/23472d30dcc8afcf34fcecfbc3033aa2.png', 15000.00, 1000.00, 20000.00, '2026-04-11 22:22:02', '2026-04-18 15:47:09', 3, '<p><span style=\"color: rgb(0, 0, 0); font-family: 宋体, Arial, HELVETICA; font-size: 16px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: justify; text-indent: 32px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial; display: inline !important; float: none;\">春归大地，万物复苏，正是赏花的好时节。年年岁岁花开放，穿越历史的长河，这些五颜六色、争奇斗艳的花朵，被不同时代的画家以笔墨定格，成就了一幅幅饱含诗意与生机的佳作，让我们在今天得以穿越时空，品赏画中的&ldquo;花花世界&rdquo;，感受其独特的艺术韵味与文化内涵。</span></p>', '已成交', '963', '100', '是', 0);
INSERT INTO `shangpinxinxi` VALUES (2, '041007479044', 1, '明代青花套', '/upload/949ac28f7e3e0e4f5c7d8d74591d7a1a.png', 25000.00, 3000.00, 25000.00, '2026-05-14 15:40:19', '2026-05-15 18:41:02', 0, '<p>明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套明代青花套</p>', '已流拍', '', '200', '是', 0);
INSERT INTO `shangpinxinxi` VALUES (3, '041007486171', 2, '祖母绿翡翠项链', '/upload/b6afbde72ffd708e3c74a5c94f53d1f8.png', 50000.00, 2000.00, 50000.00, '2026-05-15 10:07:08', '2026-05-19 15:11:02', 0, '<p>祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链祖母绿翡翠项链</p>', '竞拍中', '', '100', '是', 0);
INSERT INTO `shangpinxinxi` VALUES (4, '041007496829', 2, '劳力士手表', '/upload/d6e36ef746d8d3004240aa35a56f1676.png', 50000.00, 10000.00, 100000.00, '2026-05-13 15:47:02', '2026-05-28 11:55:02', 2, '<table style=\"border-collapse: collapse; border-spacing: 0px; width: 865px; border: 1px solid rgb(229, 229, 229); color: rgb(0, 0, 0); font-family: 微软雅黑, tahoma, arial; font-size: 12px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\">\n<tbody>\n<tr style=\"border: 1px solid rgb(229, 229, 229);\">\n<td class=\"param_info_tit\" style=\"margin: 0px; padding: 0px; width: 144px; background: rgb(251, 251, 251); vertical-align: top; border: 1px solid rgb(229, 229, 229);\"><span style=\"font-size: 16px; line-height: 20px; display: block; margin: 18px 0px 0px 30px;\">基本信息</span></td>\n<td class=\"param_info_txt\" style=\"margin: 0px; padding: 18px 0px 18px 30px; position: relative; width: 549px; border: 1px solid rgb(229, 229, 229);\">\n<ul style=\"margin: 0px; padding: 0px; list-style: none;\">\n<li style=\"margin: 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">编号</strong>m126334-0033</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">品牌</strong><a style=\"color: rgb(0, 0, 0); text-decoration: none; outline: none;\" href=\"https://watch.xbiao.com/rolex/\" target=\"_blank\" rel=\"noopener\">劳力士</a></li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">系列</strong><a style=\"color: rgb(0, 0, 0); text-decoration: none; outline: none;\" href=\"https://watch.xbiao.com/rolex/10000127/\" target=\"_blank\" rel=\"noopener\">日志型</a></li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">机芯类型</strong><a style=\"color: rgb(0, 0, 0); text-decoration: none; outline: none;\" href=\"https://watch.xbiao.com/rolex/a2/\" target=\"_blank\" rel=\"noopener\">自动机械</a></li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">性别</strong><a style=\"color: rgb(0, 0, 0); text-decoration: none; outline: none;\" href=\"https://watch.xbiao.com/rolex/men/\" target=\"_blank\" rel=\"noopener\">男士</a></li>\n</ul>\n<span class=\"param-edit\" style=\"position: absolute; right: 38px; top: 18px;\"><br></span></td>\n</tr>\n<tr style=\"border: 1px solid rgb(229, 229, 229);\">\n<td class=\"param_info_tit\" style=\"margin: 0px; padding: 0px; width: 144px; background: rgb(251, 251, 251); vertical-align: top; border: 1px solid rgb(229, 229, 229);\"><span style=\"font-size: 16px; line-height: 20px; display: block; margin: 18px 0px 0px 30px;\">机芯</span></td>\n<td class=\"param_info_txt\" style=\"margin: 0px; padding: 18px 0px 18px 30px; position: relative; width: 549px; border: 1px solid rgb(229, 229, 229);\">\n<ul style=\"margin: 0px; padding: 0px; list-style: none;\">\n<li style=\"margin: 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">机芯型号</strong><a style=\"color: rgb(0, 0, 0); text-decoration: none; outline: none;\" href=\"https://jixin.xbiao.com/rolex/12221.html\" target=\"_blank\" rel=\"noopener\">3235</a></li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">机芯类型</strong>自动机械</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">出产厂商</strong>劳力士</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">振频</strong>28800每小时振荡次数</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">游丝</strong>顺磁性蓝色Parachrom游丝</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">避震</strong>高性能Paraflex缓震装置</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">宝石数</strong>31个</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">动力储备</strong>70小时</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">技术认证</strong>超卓天文台，瑞士天文台</li>\n</ul>\n<span class=\"param-edit\" style=\"position: absolute; right: 38px; top: 18px;\"><br></span></td>\n</tr>\n<tr style=\"border: 1px solid rgb(229, 229, 229);\">\n<td class=\"param_info_tit\" style=\"margin: 0px; padding: 0px; width: 144px; background: rgb(251, 251, 251); vertical-align: top; border: 1px solid rgb(229, 229, 229);\"><span style=\"font-size: 16px; line-height: 20px; display: block; margin: 18px 0px 0px 30px;\">材质</span></td>\n<td class=\"param_info_txt\" style=\"margin: 0px; padding: 18px 0px 18px 30px; position: relative; width: 549px; border: 1px solid rgb(229, 229, 229);\">\n<ul style=\"margin: 0px; padding: 0px; list-style: none;\">\n<li style=\"margin: 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表壳材质</strong>白色黄金及蚝式钢</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表盘材质</strong>暂无</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表圈材质</strong>蚝式钢</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表镜材质</strong>蓝宝石水晶玻璃</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表冠材质</strong>蚝式钢</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表带材质</strong>蚝式钢</li>\n<li style=\"margin: 6px 0px 0px; padding: 0px 80px 0px 118px; list-style: none; position: relative; line-height: 20px; font-size: 14px; color: rgb(126, 126, 126); min-height: 20px;\"><strong style=\"position: absolute; top: 0px; left: 0px; color: rgb(0, 0, 0); font-weight: normal;\">表扣材质</strong>蚝式钢</li>\n</ul>\n</td>\n</tr>\n</tbody>\n</table>', '已成交', '1559938615', 'tianming200', '是', 0);

-- ----------------------------
-- Table structure for shoucang
-- ----------------------------
DROP TABLE IF EXISTS `shoucang`;
CREATE TABLE `shoucang`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户',
  `xwid` int(11) NOT NULL DEFAULT 0 COMMENT '关联表id',
  `biao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联表',
  `biaoti` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shoucang
-- ----------------------------
INSERT INTO `shoucang` VALUES (2, '001', 1, 'shangpinxinxi', '古画寻芳', '2026-03-07 17:52:05');
INSERT INTO `shoucang` VALUES (3, '002', 1, 'shangpinxinxi', '古画寻芳', '2026-03-07 17:56:33');
INSERT INTO `shoucang` VALUES (4, '002', 3, 'gonggao', '利永紫砂联手淘宝拍卖会 推进O2O电商全网战略布局', '2026-03-07 17:57:09');
INSERT INTO `shoucang` VALUES (5, '963', 3, 'shangpinxinxi', '祖母绿翡翠项链', '2026-03-09 18:12:20');
INSERT INTO `shoucang` VALUES (6, '963', 4, 'shangpinxinxi', '劳力士手表', '2026-03-09 18:12:24');
INSERT INTO `shoucang` VALUES (7, '963', 2, 'shangpinxinxi', '明代青花套', '2026-03-09 18:12:31');
INSERT INTO `shoucang` VALUES (8, '001', 4, 'shangpinxinxi', '劳力士手表', '2026-04-08 17:11:15');
INSERT INTO `shoucang` VALUES (9, '1559938615', 4, 'shangpinxinxi', '劳力士手表', '2026-04-18 21:48:56');

-- ----------------------------
-- Table structure for siliao
-- ----------------------------
DROP TABLE IF EXISTS `siliao`;
CREATE TABLE `siliao`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '编号',
  `shouxinren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收信人',
  `faxinren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发信人',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息最后时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '私聊' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of siliao
-- ----------------------------
INSERT INTO `siliao` VALUES (3, '03071752068724', '100', '001', '2026-03-07 17:54:05');
INSERT INTO `siliao` VALUES (4, '03091812331887', '200', '963', '2026-03-09 18:12:53');
INSERT INTO `siliao` VALUES (5, '04081711061340', '200', '001', '2026-04-08 17:11:10');
INSERT INTO `siliao` VALUES (6, '05151925101838', '200', '1559938615', '2026-05-15 19:25:10');

-- ----------------------------
-- Table structure for xiaoxi
-- ----------------------------
DROP TABLE IF EXISTS `xiaoxi`;
CREATE TABLE `xiaoxi`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `siliaoid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '私聊id',
  `bianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '编号',
  `neirong` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `fasongren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发送人',
  `fasongshijian` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发送时间',
  `shifouzhakan` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否查看',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xiaoxi_siliaoid_index`(`siliaoid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of xiaoxi
-- ----------------------------
INSERT INTO `xiaoxi` VALUES (6, 3, '', '{\"type\":\"txt\",\"msg\":\"法国恢复规划法规\"}', '001', '2026-03-07 17:52:08', '是');
INSERT INTO `xiaoxi` VALUES (7, 3, '', '{\"type\":\"txt\",\"msg\":\"和法规和风格\"}', '100', '2026-03-07 17:54:05', '是');
INSERT INTO `xiaoxi` VALUES (8, 4, '', '{\"type\":\"txt\",\"msg\":\"法国恢法国恢复高鸿股份\"}', '963', '2026-03-09 18:12:36', '是');
INSERT INTO `xiaoxi` VALUES (9, 4, '', '{\"type\":\"txt\",\"msg\":\"法国恢复规划法规\"}', '200', '2026-03-09 18:12:53', '是');
INSERT INTO `xiaoxi` VALUES (10, 5, '', '{\"type\":\"txt\",\"msg\":\"啊啊啊啊啊啊啊啊啊啊\"}', '001', '2026-04-08 17:11:10', '是');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `yonghuming` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `mima` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `xingming` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `xingbie` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '性别',
  `qianbaoyue` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '钱包余额',
  `shouji` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机',
  `youxiang` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `shenfenzheng` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '身份证',
  `touxiang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-正常,1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (1, '001', '001', '王明', '男', 500000.00, '13656566555', '565656@qq.com', '310230196902143721', '/upload/dcb198e9c1003d742802829137ed7b02.png', 1);
INSERT INTO `yonghu` VALUES (2, '1331111336', '002', '张小小', '女', 2998.00, '13565656565', '56565@qq.com', '142202196512112908', '/upload/71f915a334a76c8220cbde90e937b055.png', 0);
INSERT INTO `yonghu` VALUES (3, '003', '003', '小兰', '女', 2667.00, '13566656565', '5656@qq.com', '142202196512112908', '/upload/59a80ecd9200b6901097f65cac9edbf7.png', 0);
INSERT INTO `yonghu` VALUES (4, '963', '963', '李丽丽', '女', 0.00, '13499999999', '999@qq.com', '430511196412112660', '/upload/87a6eb5f01c6b87079e396ed0db48bdf.png', 0);
INSERT INTO `yonghu` VALUES (5, '1559938615', '123456', '王明', '男', 500000.00, '13656566559', '565656@qq.com', '310230196902143721', '/upload/dcb198e9c1003d742802829137ed7b02.png', 0);

-- ----------------------------
-- Table structure for yonghutixian
-- ----------------------------
DROP TABLE IF EXISTS `yonghutixian`;
CREATE TABLE `yonghutixian`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `yonghuid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `yonghuming` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `xingming` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `tixianjine` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '提现金额',
  `tixianriqi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提现日期',
  `tixianren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '提现人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `yonghutixian_yonghuid_index`(`yonghuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户提现' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yonghutixian
-- ----------------------------

-- ----------------------------
-- Table structure for zaixianchongzhi
-- ----------------------------
DROP TABLE IF EXISTS `zaixianchongzhi`;
CREATE TABLE `zaixianchongzhi`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `chongzhibianhao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充值编号',
  `chongzhijine` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '充值金额',
  `chongzhiriqi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '充值日期',
  `chongzhiren` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '充值人',
  `iszf` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '否' COMMENT '是否支付',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-正常,1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '在线充值' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of zaixianchongzhi
-- ----------------------------
INSERT INTO `zaixianchongzhi` VALUES (6, '030717471539', 500000.00, '2026-03-07 17:47:37', '001', '是', 0);

SET FOREIGN_KEY_CHECKS = 1;
