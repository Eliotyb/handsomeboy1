-- 秒杀活动数据
USE community_groupon;

-- 插入秒杀活动
INSERT INTO `seckill_activity` (`name`, `start_time`, `end_time`, `status`) VALUES
('午夜低音革命', '2026-10-31 22:00:00', '2026-11-01 02:00:00', 1),
('电子音乐节', '2026-11-15 18:00:00', '2026-11-16 00:00:00', 0),
('校园演唱会', '2026-12-01 19:00:00', '2026-12-01 22:00:00', 0);

-- 插入活动商品
INSERT INTO `product` (`name`, `description`, `price`, `stock`, `image`, `status`) VALUES
('午夜低音革命门票', '电子音乐节门票，包含音响系统体验和灯光艺术表演', 299.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=electronic%20music%20festival%20stage%20with%20neon%20lights%20and%20crowd%2C%20concert%20scene%2C%20purple%20and%20blue%20lighting&image_size=portrait_16_9', 1),
('电子音乐节VIP票', 'VIP体验，包含专属区域和周边礼品', 499.00, 50, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=VIP%20area%20at%20music%20festival%2C%20exclusive%20lounge%2C%20premium%20experience&image_size=portrait_16_9', 1),
('校园演唱会门票', '校园乐队现场表演，摇滚之夜', 199.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20concert%20with%20students%2C%20band%20on%20stage%2C%20energetic%20crowd&image_size=portrait_16_9', 1);

-- 插入秒杀商品
INSERT INTO `seckill_product` (`activity_id`, `product_id`, `seckill_price`, `seckill_stock`, `sold_count`) VALUES
(1, 1, 99.00, 50, 38),  -- 午夜低音革命：99元秒杀
(2, 2, 299.00, 20, 0),  -- 电子音乐节：299元秒杀
(3, 3, 99.00, 100, 0);   -- 校园演唱会：99元秒杀

-- 验证数据
SELECT a.id as activity_id, a.name as activity_name, a.start_time, a.end_time, a.status,
       p.id as product_id, p.name as product_name, p.price as original_price,
       sp.seckill_price, sp.seckill_stock, sp.sold_count
FROM `seckill_activity` a
JOIN `seckill_product` sp ON a.id = sp.activity_id
JOIN `product` p ON sp.product_id = p.id;
