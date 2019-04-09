# 统一注释为：（井号加空格）

CREATE TABLE `t_click_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` varchar(255) DEFAULT NULL,
  `staffId` varchar(255) DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

CREATE TABLE `t_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(20) NOT NULL COMMENT '部门名称',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级部门id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dept_name` (`dept_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门表';

CREATE TABLE `t_main_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL,
  `banner_order` int(11) DEFAULT NULL COMMENT '1:up  2:back',
  `banner_group` int(11) DEFAULT NULL,
  `status` int(255) DEFAULT NULL COMMENT '0:exist  1:dalete',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT NULL,
  `last_update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

CREATE TABLE `t_menu` (
  `id` varchar(32) NOT NULL,
  `menu` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

CREATE TABLE `t_online_user` (
  `userId` varchar(40) NOT NULL DEFAULT '',
  `loginTime` timestamp NULL DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '1--在线    2--不在线',
  `ip` varchar(16) DEFAULT NULL,
  `mac` varchar(50) DEFAULT NULL,
  `autoopttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `t_role` (
  `id` varchar(32) NOT NULL,
  `role` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_staff_push_info` (
  `udid` varchar(50) NOT NULL,
  `channelId` varchar(50) DEFAULT NULL,
  `userId` varchar(50) DEFAULT NULL COMMENT '这个不是用户的ID，是百度云推送的一个参数',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `addtime` timestamp NULL DEFAULT NULL,
  `device_type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`udid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `t_user` (
  `user_id` char(32) NOT NULL,
  `user_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `password` char(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `face` varchar(50) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  `position` varchar(10) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `login_time` timestamp NULL DEFAULT NULL,
  `user_UDID` varchar(40) DEFAULT NULL,
  `user_type` varchar(40) DEFAULT NULL,
  `role` varchar(200) DEFAULT NULL COMMENT 'Console Admin   :   只能操作或查看所有的pc用户和app用户，Beacon user：只能操作或查看beacon，News user：    只能操作或查看新闻，Activity user：  只能操作或查看活动    Beacon user：只能操作或查看beacon News user：    只能操作或查看新闻 Activity user：  只能操作或查看活动',
  `branch` varchar(40) DEFAULT NULL,
  `code_id` char(32) DEFAULT NULL,
  `groupId` varchar(11) DEFAULT NULL COMMENT '所属组Id',
  `headPortrait` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `isVIP` varchar(10) DEFAULT NULL COMMENT '是否是vip',
  `accessRight` varchar(300) DEFAULT NULL COMMENT '可显示菜单',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `t_user_app` (
  `user_id` char(32) NOT NULL,
  `staffId` varchar(35) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `password` char(32) DEFAULT NULL,
  `face` varchar(50) DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `login_time` timestamp NULL DEFAULT NULL,
  `user_UDID` varchar(40) DEFAULT NULL,
  `user_type` varchar(40) DEFAULT NULL,
  `role` varchar(200) DEFAULT NULL,
  `branch` varchar(40) DEFAULT NULL,
  `code_id` char(32) DEFAULT NULL,
  `english_name` varchar(200) DEFAULT NULL,
  `chinese_name` varchar(200) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `push_id` varchar(50) DEFAULT NULL COMMENT '使用百度推送百度给的唯一标示，用于定向推送',
  `device_type` varchar(10) DEFAULT NULL COMMENT '设备类型：ios   或   android',
  `isAdmin` char(1) DEFAULT 'N' COMMENT '是否是图书管理员',
  `dept_id` int(11) DEFAULT '1' COMMENT '部门ID',
  `qrcode` varchar(500) DEFAULT NULL COMMENT '用户二维码地址',
  `photo` varchar(255) DEFAULT NULL,
  `remember` varchar(20) DEFAULT '0',
  `telephone` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `i_d_card` varchar(255) DEFAULT NULL,
  `library_admin` varchar(250) DEFAULT NULL COMMENT '图书馆管理员',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TRIGGER `insert_users` BEFORE INSERT ON `t_user_app` FOR EACH ROW BEGIN
if new.code_id = '' then
set new.code_id = null;
end if;
if new.create_time = '0000-00-00 00:00:00' then
set new.create_time=CURRENT_TIMESTAMP();
end if;

if new.last_time = '0000-00-00 00:00:00' then
set new.last_time=CURRENT_TIMESTAMP();
end if;

if new.login_time = '0000-00-00 00:00:00' then
set new.login_time=CURRENT_TIMESTAMP();
end if;
end;


