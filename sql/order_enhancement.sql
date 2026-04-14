USE community_groupon;

-- =============================================
-- 1. 增强订单表字段
-- =============================================
ALTER TABLE `order` ADD COLUMN `address_id` BIGINT(20) NULL COMMENT '收货地址ID' AFTER `seckill_product_id`;
ALTER TABLE `order` ADD COLUMN `receiver_name` VARCHAR(50) NULL COMMENT '收货人' AFTER `address_id`;
ALTER TABLE `order` ADD COLUMN `receiver_phone` VARCHAR(20) NULL COMMENT '收货电话' AFTER `receiver_name`;
ALTER TABLE `order` ADD COLUMN `receiver_address` VARCHAR(255) NULL COMMENT '收货详细地址' AFTER `receiver_phone`;
ALTER TABLE `order` ADD COLUMN `pay_method` VARCHAR(20) NULL DEFAULT 'wechat' COMMENT '支付方式: wechat/alipay/balance' AFTER `total_price`;
ALTER TABLE `order` ADD COLUMN `pay_time` DATETIME NULL COMMENT '支付时间' AFTER `status`;
ALTER TABLE `order` ADD COLUMN `ship_time` DATETIME NULL COMMENT '发货时间' AFTER `pay_time`;
ALTER TABLE `order` ADD COLUMN `receive_time` DATETIME NULL COMMENT '确认收货时间' AFTER `ship_time`;
ALTER TABLE `order` ADD COLUMN `finish_time` DATETIME NULL COMMENT '完成时间' AFTER `receive_time`;
ALTER TABLE `order` ADD COLUMN `cancel_reason` VARCHAR(255) NULL COMMENT '取消原因' AFTER `finish_time`;
ALTER TABLE `order` ADD COLUMN `logistics_company` VARCHAR(50) NULL COMMENT '物流公司' AFTER `cancel_reason`;
ALTER TABLE `order` ADD COLUMN `logistics_no` VARCHAR(100) NULL COMMENT '物流单号' AFTER `logistics_company`;
ALTER TABLE `order` MODIFY COLUMN `status` INT(11) NOT NULL DEFAULT 0 COMMENT '0:待支付 1:已支付/待发货 2:已发货/待收货 3:已完成 4:已取消 5:退款中 6:已退款 7:退款拒绝';

-- =============================================
-- 2. 物流信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `order_logistics` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT(20) NOT NULL COMMENT '关联订单ID',
  `logistics_company` VARCHAR(50) NOT NULL COMMENT '物流公司名称',
  `logistics_no` VARCHAR(100) NOT NULL COMMENT '物流单号',
  `status` INT(11) NOT NULL DEFAULT 0 COMMENT '0:待揽收 1:已揽收 2:运输中 3:派送中 4:已签收 5:异常',
  `current_location` VARCHAR(255) NULL COMMENT '当前位置',
  `remark` VARCHAR(500) NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 3. 退换货/售后表
-- =============================================
CREATE TABLE IF NOT EXISTS `order_refund` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT(20) NOT NULL COMMENT '关联订单ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '申请用户ID',
  `refund_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '退款单号',
  `refund_type` INT(11) NOT NULL DEFAULT 1 COMMENT '1:仅退款 2:退货退款',
  `reason` VARCHAR(200) NOT NULL COMMENT '申请原因',
  `description` TEXT NULL COMMENT '详细说明',
  `images` VARCHAR(1000) NULL COMMENT '凭证图片(JSON数组)',
  `refund_amount` DOUBLE(10,2) NOT NULL COMMENT '退款金额',
  `status` INT(11) NOT NULL DEFAULT 0 COMMENT '0:待审核 1:审核通过/待寄回 2:商家已收货/处理中 3:退款成功 4:退款关闭/拒绝',
  `audit_user_id` BIGINT(20) NULL COMMENT '审核人ID',
  `audit_remark` VARCHAR(255) NULL COMMENT '审核备注',
  `audit_time` DATETIME NULL COMMENT '审核时间',
  `refund_time` DATETIME NULL COMMENT '退款完成时间',
  `reject_reason` VARCHAR(255) NULL COMMENT '拒绝原因',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 4. 评价表
-- =============================================
CREATE TABLE IF NOT EXISTS `order_review` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT(20) NOT NULL COMMENT '关联订单ID`,
  `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '评价用户ID',
  `rating` INT(11) NOT NULL DEFAULT 5 COMMENT '评分 1-5',
  `content` TEXT NULL COMMENT '评价内容',
  `images` VARCHAR(1000) NULL COMMENT '评价图片(JSON数组)',
  `is_anonymous` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否匿名 0:否 1:是',
  `reply_content` TEXT NULL COMMENT '商家回复',
  `reply_time` DATETIME NULL COMMENT '回复时间',
  `status` INT(11) NOT NULL DEFAULT 1 COMMENT '0:隐藏 1:显示',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_product_id` (`product_id`),
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 5. 插入示例数据
-- =============================================

-- 物流示例数据
INSERT INTO `order_logistics` (`order_id`, `logistics_company`, `logistics_no`, `status`, `current_location`, `remark`) VALUES
(1, '顺丰速运', 'SF1234567890123', 3, '上海市浦东新区某路888号 A1座1502室 派送中', '包裹已到达上海分拨中心'),
(2, '圆通快递', 'YT9876543210987', 2, '上海市闵行区转运中心', '运输途中'),
(3, '韵达快递', 'YD4567890123456', 4, '已签收', '本人签收');

-- 退换货示例数据
INSERT INTO `order_refund` (`order_id`, `user_id`, `refund_no`, `refund_type`, `reason`, `description`, `refund_amount`, `status`) VALUES
(5, 1, 'REF-20260413001', 1, '不想要了', '拍错了，颜色不对', 299.00, 0),
(6, 1, 'REF-20260413002', 2, '质量问题', '耳机左耳有杂音，无法正常使用，要求退货退款', 1299.00, 1),
(7, 1, 'REF-20260413003', 1, '尺寸不合', '手表表带太短，戴不上', 899.00, 3);

-- 评价示例数据
INSERT INTO `order_review` (`order_id`, `product_id`, `user_id`, `rating`, `content`, `is_anonymous`, `status`) VALUES
(3, 1, 1, 5, '鞋子非常舒服！颜色也很好看，秒杀价格超值，下次还来买！', 0, 1),
(4, 2, 1, 4, '手表颜值很高，就是包装可以再精致一点。总体满意。', 1, 1);

-- 更新现有订单的示例数据
UPDATE `order` SET 
  receiver_name = '张小野',
  receiver_phone = '138****9012',
  receiver_address = '上海市浦东新区 某某路 888号 A1座 1502室',
  pay_method = 'wechat',
  pay_time = '2026-04-10 14:30:00'
WHERE id IN (1, 2, 3);

UPDATE `order` SET status = 2, ship_time = '2026-04-11 09:00:00', logistics_company = '顺丰速运', logistics_no = 'SF1234567890123' WHERE id = 1;
UPDATE `order` SET status = 1 WHERE id = 2;
UPDATE `order` SET status = 3, receive_time = '2026-04-12 16:20:00', finish_time = '2026-04-12 16:20:00' WHERE id = 3;
UPDATE `order` SET status = 4, cancel_reason = '用户主动取消' WHERE id = 4;
