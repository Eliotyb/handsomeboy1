-- 数据库示例数据
-- 使用数据库
USE community_groupon;

-- 插入用户数据
INSERT INTO `user` (`username`, `password`, `name`, `phone`, `address`, `role`) VALUES
('admin', '123456', '管理员', '13800138000', '北京市朝阳区', 1),
('user1', '123456', '张三', '13900139001', '上海市浦东新区', 0),
('user2', '123456', '李四', '13900139002', '广州市天河区', 0),
('user3', '123456', '王五', '13900139003', '深圳市南山区', 0);

-- 插入商品数据
INSERT INTO `product` (`name`, `description`, `price`, `stock`, `image`, `status`) VALUES
('iPhone 14 Pro', 'Apple iPhone 14 Pro 256GB 深空黑色', 8999.00, 100, 'https://example.com/iphone14pro.jpg', 1),
('AirPods Pro 2', 'Apple AirPods Pro 第二代', 1899.00, 200, 'https://example.com/airpods.jpg', 1),
('MacBook Air M2', 'Apple MacBook Air M2 13英寸', 7999.00, 50, 'https://example.com/macbook.jpg', 1),
('iPad Pro 12.9', 'Apple iPad Pro 12.9英寸', 6799.00, 80, 'https://example.com/ipad.jpg', 1),
('Apple Watch Series 8', 'Apple Watch Series 8 GPS', 2999.00, 150, 'https://example.com/applewatch.jpg', 1);

-- 插入秒杀活动数据
INSERT INTO `seckill_activity` (`name`, `start_time`, `end_time`, `status`) VALUES
('618秒杀活动', '2026-06-18 00:00:00', '2026-06-18 23:59:59', 1),
('双11秒杀活动', '2026-11-11 00:00:00', '2026-11-11 23:59:59', 0),
('周年庆秒杀活动', '2026-07-01 00:00:00', '2026-07-01 23:59:59', 0);

-- 插入秒杀商品数据
INSERT INTO `seckill_product` (`activity_id`, `product_id`, `seckill_price`, `seckill_stock`, `sold_count`) VALUES
(1, 1, 7999.00, 10, 0),  -- 618活动：iPhone 14 Pro
(1, 2, 1499.00, 20, 0),  -- 618活动：AirPods Pro 2
(1, 3, 7499.00, 5, 0),   -- 618活动：MacBook Air M2
(2, 1, 7599.00, 8, 0),   -- 双11活动：iPhone 14 Pro
(2, 4, 6299.00, 15, 0),  -- 双11活动：iPad Pro 12.9
(3, 5, 2499.00, 30, 0);  -- 周年庆活动：Apple Watch Series 8

-- 插入订单数据
INSERT INTO `order` (`order_no`, `user_id`, `product_id`, `seckill_product_id`, `price`, `quantity`, `total_price`, `status`) VALUES
('ORDER20260618001', 2, 1, 1, 7999.00, 1, 7999.00, 1),  -- 用户2购买iPhone 14 Pro
('ORDER20260618002', 3, 2, 2, 1499.00, 2, 2998.00, 2),  -- 用户3购买2个AirPods Pro 2
('ORDER20260618003', 2, 3, 3, 7499.00, 1, 7499.00, 0);  -- 用户2购买MacBook Air M2（待支付）
