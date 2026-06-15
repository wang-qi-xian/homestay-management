-- 民宿预定管理平台 - 数据库初始化脚本
-- 数据库: homestay-management

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `rooms`;
DROP TABLE IF EXISTS `users`;

-- 用户表
CREATE TABLE `users` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  `nickname` VARCHAR(100),
  `phone` VARCHAR(20),
  `created_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 房间表
CREATE TABLE `rooms` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `address` VARCHAR(500),
  `price` DECIMAL(10,2) NOT NULL,
  `type` VARCHAR(50),
  `capacity` INT,
  `image_url` VARCHAR(500),
  `host_id` BIGINT NOT NULL,
  `available` BOOLEAN DEFAULT TRUE,
  `created_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE `orders` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `guest_id` BIGINT NOT NULL,
  `room_id` BIGINT NOT NULL,
  `host_id` BIGINT NOT NULL,
  `check_in` DATE NOT NULL,
  `check_out` DATE NOT NULL,
  `total_price` DECIMAL(10,2) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `created_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==================== 测试数据 ====================

-- 测试用户（密码为明文存储，仅用于演示）
INSERT INTO `users` (`username`, `password`, `role`, `nickname`, `phone`, `created_at`) VALUES
('admin', '123456', 'ADMIN', '系统管理员', '13800000001', NOW()),
('host', '123456', 'HOST', '张房东', '13800000002', NOW()),
('guest', '123456', 'GUEST', '李游客', '13800000003', NOW()),
('host2', '123456', 'HOST', '王房东', '13800000004', NOW()),
('guest2', '123456', 'GUEST', '赵游客', '13800000005', NOW());

-- 测试房间
INSERT INTO `rooms` (`title`, `description`, `address`, `price`, `type`, `capacity`, `image_url`, `host_id`, `available`, `created_at`) VALUES
('山景小屋', '坐落于青山绿水间，远离城市喧嚣，享受宁静的田园生活。配备独立厨房和观景阳台。', '杭州市西湖区龙井路88号', 388.00, '小屋', 2, 'https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=600', 2, TRUE, NOW()),
('海景公寓', '面朝大海的精品公寓，落地窗尽享无敌海景，步行5分钟即达沙滩。', '厦门市思明区环岛路168号', 528.00, '公寓', 3, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600', 2, TRUE, NOW()),
('古镇民宿', '百年老宅改造，保留传统建筑风格，体验江南水乡的独特韵味。', '苏州市吴中区甪直镇东市街22号', 268.00, '民宿', 4, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600', 2, TRUE, NOW()),
('城市精品公寓', '位于市中心商圈，交通便利，设施齐全，适合商务出行。', '上海市黄浦区南京东路100号', 458.00, '公寓', 2, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600', 4, TRUE, NOW()),
('田园别墅', '独栋别墅带私家花园，适合家庭聚会和团建活动，可烧烤。', '成都市都江堰市青城山路56号', 888.00, '别墅', 8, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600', 4, TRUE, NOW()),
('森林木屋', '隐藏在原始森林中的木屋，与自然零距离接触，适合亲子度假。', '张家界市武陵源区金鞭路12号', 358.00, '木屋', 3, 'https://images.unsplash.com/photo-1540541338287-41700207dee6?w=600', 2, FALSE, NOW());

-- 测试订单
INSERT INTO `orders` (`guest_id`, `room_id`, `host_id`, `check_in`, `check_out`, `total_price`, `status`, `created_at`) VALUES
(3, 1, 2, '2025-07-01', '2025-07-03', 776.00, 'CONFIRMED', NOW()),
(3, 2, 2, '2025-07-10', '2025-07-12', 1056.00, 'PENDING', NOW()),
(5, 3, 2, '2025-07-05', '2025-07-08', 804.00, 'COMPLETED', NOW()),
(3, 4, 4, '2025-07-15', '2025-07-17', 916.00, 'PENDING', NOW()),
(5, 5, 4, '2025-07-20', '2025-07-22', 1776.00, 'CANCELLED', NOW());
