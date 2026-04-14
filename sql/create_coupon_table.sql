-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '优惠券ID',
  `title` VARCHAR(100) NOT NULL COMMENT '优惠券标题',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '优惠券描述',
  `coupon_type` TINYINT NOT NULL DEFAULT 0 COMMENT '优惠券类型：0-满减券，1-折扣券，2-无门槛券',
  `value` DECIMAL(10,2) NOT NULL COMMENT '优惠值（金额或折扣比例）',
  `min_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '最低消费金额',
  `max_discount` DECIMAL(10,2) DEFAULT NULL COMMENT '最大优惠金额',
  `total_count` INT NOT NULL DEFAULT 100 COMMENT '发放总量',
  `used_count` INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
  `start_time` DATETIME NOT NULL COMMENT '有效期开始时间',
  `end_time` DATETIME NOT NULL COMMENT '有效期结束时间',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID（NULL表示通用券）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_time_range` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

-- 用户优惠券关联表（记录用户领取和使用情况）
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `order_id` BIGINT DEFAULT NULL COMMENT '使用的订单ID',
  `receive_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_time` DATETIME DEFAULT NULL COMMENT '使用时间',
  UNIQUE KEY `uk_user_coupon` (`user_id`, `coupon_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券关联表';

-- 插入示例优惠券数据
INSERT INTO `coupon` (`title`, `description`, `coupon_type`, `value`, `min_amount`, `max_discount`, `total_count`, `used_count`, `status`, `start_time`, `end_time`, `user_id`) VALUES
('新人专享券', '欢迎加入PULSE VELOCITY！新人注册即可领取', 0, 20.00, 100.00, NULL, 9999, 56, 1, '2026-04-01 00:00:00', '2026-05-31 23:59:59', NULL),
('限时秒杀券', '秒杀活动专属优惠券，限时限量', 0, 50.00, 299.00, NULL, 500, 123, 1, '2026-04-10 00:00:00', '2026-04-20 23:59:59', NULL),
('满减优惠券', '全场通用满减券，购物更实惠', 0, 10.00, 50.00, NULL, 2000, 892, 1, '2026-03-01 00:00:00', '2026-03-31 23:59:59', NULL),
('会员专享券', 'VIP会员专属大额优惠券', 0, 30.00, 199.00, NULL, 300, 45, 1, '2026-03-15 00:00:00', '2026-04-15 23:59:59', NULL),
('周末特惠券', '仅限周末使用，享受超值优惠', 0, 15.00, 88.00, NULL, 800, 267, 1, '2026-04-01 00:00:00', '2026-06-30 23:59:59', NULL),
('无门槛券', '无需满足任何条件即可使用', 2, 5.00, 0.00, NULL, 1000, 445, 1, '2026-04-13 00:00:00', '2026-05-13 23:59:59', NULL);

INSERT INTO `user_coupon` (`user_id`, `coupon_id`, `status`) VALUES
(1, 1, 0),
(1, 2, 0),
(1, 3, 1),
(1, 6, 0);
