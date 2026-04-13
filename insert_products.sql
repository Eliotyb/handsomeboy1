-- 商品表插入语句
-- 创建时间: 2026-04-11

USE community_groupon;

INSERT INTO product (name, description, price, stock, image, status, create_time, update_time) VALUES
('智能手表', '高端智能手表，支持心率监测、运动追踪、消息提醒等功能', 1299.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smart%20watch%20on%20wrist%2C%20modern%20design%2C%20black%20color&image_size=square', 1, NOW(), NOW()),
('便携音箱', '小巧便携蓝牙音箱，音质清晰，防水设计', 599.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=portable%20bluetooth%20speaker%2C%20compact%20design&image_size=square', 1, NOW(), NOW()),
('无线耳机', '主动降噪无线耳机，续航30小时，音质出众', 399.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20earbuds%20case%2C%20premium%20white%20design&image_size=square', 1, NOW(), NOW()),
('平板电脑', '10.1英寸高清屏幕，高性能处理器，学习娱乐两用', 2999.00, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tablet%20computer%2C%20modern%20slim%20design&image_size=square', 1, NOW(), NOW()),
('笔记本电脑', '14英寸轻薄本，全金属机身，超长续航', 5999.00, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=laptop%20computer%2C%20modern%20slim%20design&image_size=square', 1, NOW(), NOW()),
('智能手机', '6.5英寸全面屏，5G网络，旗舰配置', 4999.00, 120, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20modern%20design%2C%20front%20view&image_size=square', 1, NOW(), NOW()),
('时尚口红套装', '经典色号套装，持久显色，滋润不拔干', 299.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lipstick%20set%2C%20cosmetic%20product%2C%20elegant%20design&image_size=square', 1, NOW(), NOW()),
('保湿精华液', '玻尿酸深层补水精华液，敏感肌适用', 199.00, 250, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=skincare%20serum%20bottle%2C%20beauty%20product&image_size=square', 1, NOW(), NOW()),
('休闲运动鞋', '透气网面运动鞋，缓震舒适，适合日常穿搭', 399.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=casual%20sneakers%2C%20sport%20shoes%2C%20white%20color&image_size=square', 1, NOW(), NOW()),
('真皮单肩包', '头层牛皮单肩包，商务休闲两用', 899.00, 90, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=leather%20shoulder%20bag%2C%20fashion%20accessory&image_size=square', 1, NOW(), NOW()),
('有机坚果礼盒', '精选混合坚果，营养健康，送礼佳品', 168.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=premium%20nuts%20gift%20box%2C%20healthy%20snacks&image_size=square', 1, NOW(), NOW()),
('进口红酒', '法国原装进口红酒，口感醇厚，果香浓郁', 388.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=red%20wine%20bottle%2C%20elegant%20packaging&image_size=square', 1, NOW(), NOW()),
('北欧风格台灯', '简约设计护眼台灯，三档亮度调节', 129.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20table%20lamp%2C%20nordic%20style&image_size=square', 1, NOW(), NOW()),
('记忆棉枕头', '慢回弹记忆棉枕头，舒适护颈，助睡眠', 199.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=memory%20foam%20pillow%2C%20comfortable%20bedding&image_size=square', 1, NOW(), NOW()),
('专业瑜伽垫', 'TPE环保材质瑜伽垫，防滑耐磨，加厚设计', 159.00, 160, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=yoga%20mat%2C%20fitness%20equipment&image_size=square', 1, NOW(), NOW()),
('户外登山包', '大容量防水登山包，多口袋设计，背负舒适', 459.00, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=outdoor%20backpack%2C%20hiking%20gear&image_size=square', 1, NOW(), NOW()),
('畅销小说套装', '年度畅销小说合集，精装典藏版', 268.00, 120, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=book%20set%2C%20bestseller%20novels%2C%20hardcover%20edition&image_size=square', 1, NOW(), NOW()),
('婴儿纸尿裤', '超薄透气纸尿裤，柔软亲肤，大包装', 128.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20diapers%2C%20infant%20care%20product&image_size=square', 1, NOW(), NOW()),
('儿童益智积木', '环保木质积木，多种造型，开发智力', 99.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=children%20blocks%2C%20educational%20toys%2C%20colorful&image_size=square', 1, NOW(), NOW()),
('车载空气净化器', '高效除甲醛车载净化器，静音设计', 299.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20air%20purifier%2C%20auto%20accessory&image_size=square', 1, NOW(), NOW()),
('汽车座椅靠垫', '记忆棉汽车靠垫，护腰护颈，长途必备', 159.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20seat%20cushion%2C%20memory%20foam&image_size=square', 1, NOW(), NOW()),
('创意陶瓷摆件', '手工制作陶瓷摆件，新中式风格', 239.00, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20figurine%2C%20home%20decor%20craft&image_size=square', 1, NOW(), NOW());
