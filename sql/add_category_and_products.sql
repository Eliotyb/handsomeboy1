USE community_groupon;

-- 1. 给product表添加category字段
ALTER TABLE product ADD COLUMN category VARCHAR(30) NOT NULL DEFAULT '其他' AFTER status;

-- 2. 更新现有22个商品的分类
UPDATE product SET category = '数码电子' WHERE name = '智能手表';
UPDATE product SET category = '数码电子' WHERE name = '便携音箱';
UPDATE product SET category = '数码电子' WHERE name = '无线耳机';
UPDATE product SET category = '数码电子' WHERE name = '平板电脑';
UPDATE product SET category = '数码电子' WHERE name = '笔记本电脑';
UPDATE product SET category = '数码电子' WHERE name = '智能手机';

UPDATE product SET category = '时尚美妆' WHERE name = '时尚口红套装';
UPDATE product SET category = '时尚美妆' WHERE name = '保湿精华液';

UPDATE product SET category = '服装鞋包' WHERE name = '休闲运动鞋';
UPDATE product SET category = '服装鞋包' WHERE name = '真皮单肩包';

UPDATE product SET category = '食品生鲜' WHERE name = '有机坚果礼盒';
UPDATE product SET category = '食品生鲜' WHERE name = '进口红酒';

UPDATE product SET category = '家居家装' WHERE name = '北欧风格台灯';
UPDATE product SET category = '家居家装' WHERE name = '记忆棉枕头';

UPDATE product SET category = '运动户外' WHERE name = '专业瑜伽垫';
UPDATE product SET category = '运动户外' WHERE name = '户外登山包';

UPDATE product SET category = '图书音像' WHERE name = '畅销小说套装';

UPDATE product SET category = '母婴用品' WHERE name = '婴儿纸尿裤';
UPDATE product SET category = '母婴用品' WHERE name = '儿童益智积木';

UPDATE product SET category = '汽车用品' WHERE name = '车载空气净化器';
UPDATE product SET category = '汽车用品' WHERE name = '汽车座椅靠垫';

UPDATE product SET category = '其他' WHERE name = '创意陶瓷摆件';

-- 3. 补充商品数据 - 让每个分类都有4-6个商品

-- 时尚美妆 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('防晒隔离霜', 'SPF50+高倍防晒，清爽不油腻，防水防汗', 89.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sunscreen%20cream%20bottle%2C%20cosmetic%20product&image_size=square', 1, '时尚美妆', NOW(), NOW()),
('香水礼盒', '经典花香调淡香水，持久留香，送礼首选', 359.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=perfume%20bottle%20gift%20set%2C%20elegant&image_size=square', 1, '时尚美妆', NOW(), NOW()),
('眼影盘套装', '12色大地色眼影盘，粉质细腻易上色', 129.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=eyeshadow%20palette%2C%20makeup%20cosmetics&image_size=square', 1, '时尚美妆', NOW(), NOW()),
('洁面乳', '氨基酸温和洁面乳，深层清洁不紧绷', 69.00, 250, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=facial%20cleanser%20tube%2C%20skincare&image_size=square', 1, '时尚美妆', NOW(), NOW());

-- 服装鞋包 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('牛仔外套', '经典牛仔外套，宽松版型，春秋必备', 299.00, 120, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=denim%20jacket%2C%20fashion%20clothing&image_size=square', 1, '服装鞋包', NOW(), NOW()),
('针织毛衣', '柔软亲肤针织毛衣，多色可选', 199.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=knit%20sweater%2C%20casual%20clothing&image_size=square', 1, '服装鞋包', NOW(), NOW()),
('双肩背包', '大容量双肩包，防泼水面料，学生通勤适用', 159.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=backpack%20bag%2C%20school%20bag&image_size=square', 1, '服装鞋包', NOW(), NOW()),
('帆布鞋', '经典低帮帆布鞋，舒适透气，百搭款式', 129.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=canvas%20shoes%2C%20casual%20footwear&image_size=square', 1, '服装鞋包', NOW(), NOW());

-- 食品生鲜 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('进口车厘子', '智利进口车厘子，JJJ级大果，新鲜空运', 158.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20cherries%20in%20box%2C%20red%20fruit&image_size=square', 1, '食品生鲜', NOW(), NOW()),
('有机大米', '东北五常有机大米，10斤装，香甜软糯', 79.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=rice%20bag%2C%20organic%20grain&image_size=square', 1, '食品生鲜', NOW(), NOW()),
('精品咖啡豆', '哥伦比亚阿拉比卡咖啡豆，中度烘焙', 128.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=coffee%20beans%20bag%2C%20premium&image_size=square', 1, '食品生鲜', NOW(), NOW()),
('手工巧克力礼盒', '比利时工艺手工巧克力，多种口味', 168.00, 120, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chocolate%20gift%20box%2C%20gourmet%20sweets&image_size=square', 1, '食品生鲜', NOW(), NOW());

-- 家居家装 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('四件套床品', '纯棉四件套，亲肤透气，多款花型可选', 299.00, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cotton%20bedding%20set%2C%20home%20textile&image_size=square', 1, '家居家装', NOW(), NOW()),
('收纳整理箱', '三件套塑料收纳箱，透明可视，加厚耐用', 59.00, 250, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=storage%20boxes%20set%2C%20home%20organization&image_size=square', 1, '家居家装', NOW(), NOW()),
('香薰蜡烛套装', '天然大豆蜡香薰蜡烛，三种香型', 89.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=scented%20candles%20set%2C%20home%20decor&image_size=square', 1, '家居家装', NOW(), NOW()),
('懒人沙发', '单人懒人沙发榻榻米，可折叠收纳', 399.00, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lazy%20sofa%20bean%20bag%2C%20living%20room&image_size=square', 1, '家居家装', NOW(), NOW());

-- 运动户外 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('跑步运动手环', '多功能运动手环，心率监测，50米防水', 249.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fitness%20tracker%20band%2C%20sports%20gadget&image_size=square', 1, '运动户外', NOW(), NOW()),
('速干运动T恤', '透气速干面料，反光条设计，夜跑安全', 99.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20t-shirt%2C%20athletic%20wear&image_size=square', 1, '运动户外', NOW(), NOW()),
('露营帐篷', '3-4人全自动帐篷，防暴雨设计', 459.00, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=camping%20tent%2C%20outdoor%20gear&image_size=square', 1, '运动户外', NOW(), NOW()),
('健身弹力带', '五件套阻力带，居家健身必备', 49.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=resistance%20bands%20set%2C%20fitness%20equipment&image_size=square', 1, '运动户外', NOW(), NOW());

-- 图书音像 (已有1个，补充5个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('编程入门教程', '零基础编程入门，Python实战指南', 69.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=programming%20book%2C%20tech%20education&image_size=square', 1, '图书音像', NOW(), NOW()),
('蓝牙音箱', '迷你便携蓝牙音箱，重低音效', 179.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mini%20bluetooth%20speaker%2C%20audio%20device&image_size=square', 1, '图书音像', NOW(), NOW()),
('历史人文书籍', '中国通史图文版，了解五千年文明', 88.00, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=history%20book%2C%20hardcover%20edition&image_size=square', 1, '图书音像', NOW(), NOW()),
('儿童绘本套装', '经典童话绘本合集，启蒙认知', 128.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=children%20picture%20books%2C%20colorful&image_size=square', 1, '图书音像', NOW(), NOW()),
('机械键盘', '青轴机械键盘，RGB背光，游戏办公两用', 299.00, 120, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mechanical%20keyboard%2C%20RGB%20backlit&image_size=square', 1, '图书音像', NOW(), NOW());

-- 母婴用品 (已有2个，补充4个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('婴儿推车', '轻便可折叠婴儿推车，一键收车', 899.00, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20stroller%2C%20lightweight%20foldable&image_size=square', 1, '母婴用品', NOW(), NOW()),
('婴幼儿奶粉', '进口配方奶粉，3段800g装', 298.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20formula%20milk%20can%2C%20infant&image_size=square', 1, '母婴用品', NOW(), NOW()),
('儿童安全座椅', '9个月-12岁儿童安全座椅，ISOFIX接口', 1299.00, 40, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=child%20car%20seat%2C%20safety%20product&image_size=square', 1, '母婴用品', NOW(), NOW()),
('婴儿洗衣液', '无荧光剂婴儿专用洗衣液，2L装', 39.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20laundry%20detergent%20bottle&image_size=square', 1, '母婴用品', NOW(), NOW());

-- 汽车用品 (已有2个，补充3个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('车载手机支架', '磁吸式出风口手机支架，稳固不掉落', 39.00, 250, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20phone%20mount%2C%20magnetic%20holder&image_size=square', 1, '汽车用品', NOW(), NOW()),
('汽车脚垫', '全包围TPE汽车脚垫，专车专用', 299.00, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20floor%20mats%2C%20auto%20accessory&image_size=square', 1, '汽车用品', NOW(), NOW()),
('应急启动电源', '大容量汽车应急启动电源，搭电神器', 459.00, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20jump%20starter%2C%20emergency%20battery&image_size=square', 1, '汽车用品', NOW(), NOW());

-- 其他 (已有1个，补充5个)
INSERT INTO product (name, description, price, stock, image, status, category, create_time, update_time) VALUES
('绿植盆栽', '多肉植物组合盆栽，含花盆', 49.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=succulent%20plants%20pot%2C%20green%20decor&image_size=square', 1, '其他', NOW(), NOW()),
('保温杯', '316不锈钢真空保温杯，500ml', 89.00, 250, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=thermos%20cup%2C%20stainless%20steel&image_size=square', 1, '其他', NOW(), NOW()),
('雨伞', '全自动折叠晴雨伞，抗风加固', 59.00, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=folding%20umbrella%2C%20rain%20gear&image_size=square', 1, '其他', NOW(), NOW()),
('充电宝', '20000mAh快充充电宝，轻薄便携', 129.00, 180, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=power%20bank%2C%20portable%20charger&image_size=square', 1, '其他', NOW(), NOW()),
('桌面台历', '2026年创意桌面台历，精美插画', 29.00, 300, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=desk%20calendar%202026%2C%20office%20supply&image_size=square', 1, '其他', NOW(), NOW());
