-- 创建数据库
CREATE DATABASE IF NOT EXISTS family_tree DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE family_tree;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
  phone VARCHAR(20) UNIQUE COMMENT '手机号',
  email VARCHAR(100) UNIQUE COMMENT '邮箱',
  password_hash VARCHAR(255) COMMENT '密码哈希',
  avatar_url VARCHAR(500) COMMENT '头像URL',
  status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_phone (phone),
  INDEX idx_email (email)
) COMMENT='用户表';

-- 家族表
CREATE TABLE IF NOT EXISTS families (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL COMMENT '家族名称',
  surname VARCHAR(50) NOT NULL COMMENT '姓氏',
  origin_place VARCHAR(200) COMMENT '祖籍地',
  admin_id BIGINT NOT NULL COMMENT '族长用户ID',
  description TEXT COMMENT '家族简介',
  member_count INT DEFAULT 0 COMMENT '成员数量',
  status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-停用',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_admin (admin_id)
) COMMENT='家族表';


-- 家族成员表
CREATE TABLE IF NOT EXISTS family_members (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_id BIGINT NOT NULL,
  user_id BIGINT COMMENT '关联用户ID',
  name VARCHAR(50) NOT NULL COMMENT '姓名',
  gender VARCHAR(10) COMMENT '性别',
  birth_date DATE COMMENT '出生日期',
  death_date DATE COMMENT '逝世日期',
  generation VARCHAR(20) COMMENT '字辈',
  ranking INT COMMENT '排行',
  bio TEXT COMMENT '生平事迹',
  father_id BIGINT COMMENT '父亲ID',
  mother_id BIGINT COMMENT '母亲ID',
  spouse_id BIGINT COMMENT '配偶ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_family (family_id)
) COMMENT='家族成员表';

-- 相册表
CREATE TABLE IF NOT EXISTS albums (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL COMMENT '相册名称',
  description TEXT COMMENT '相册描述',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_family (family_id)
) COMMENT='相册表';

-- 照片表
CREATE TABLE IF NOT EXISTS photos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  album_id BIGINT NOT NULL,
  url VARCHAR(500) NOT NULL COMMENT '照片URL',
  description TEXT COMMENT '照片描述',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_album (album_id)
) COMMENT='照片表';

-- 家族关系表
CREATE TABLE IF NOT EXISTS family_relations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_id BIGINT NOT NULL COMMENT '家族ID',
  member_id BIGINT NOT NULL COMMENT '成员ID',
  related_member_id BIGINT NOT NULL COMMENT '关联成员ID',
  relation_type VARCHAR(20) NOT NULL COMMENT '关系类型：father/mother/spouse/child/sibling',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_member (member_id),
  INDEX idx_related (related_member_id),
  INDEX idx_family (family_id),
  UNIQUE KEY uk_relation (member_id, related_member_id, relation_type)
) COMMENT='家族关系表';

-- 软删除字段
ALTER TABLE families       ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE family_members ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE albums         ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE photos         ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0;
