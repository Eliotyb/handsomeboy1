package com.community.groupon.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.groupon.entity.Product;
import com.community.groupon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        long count = productRepository.selectCount(new LambdaQueryWrapper<>());
        if (count > 0) {
            System.out.println("[DataInitializer] Database already has " + count + " products, skipping initialization.");
            return;
        }
        System.out.println("[DataInitializer] Initializing 64 products with categories...");

        Object[][] products = {
            {"智能手表", "高端智能手表，支持心率监测、运动追踪、消息提醒等功能", 1299.00, 100, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smart%20watch%20on%20wrist%2C%20modern%20design%2C%20black%20color&image_size=square", "数码电子"},
            {"便携音箱", "小巧便携蓝牙音箱，音质清晰，防水设计", 599.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=portable%20bluetooth%20speaker%2C%20compact%20design&image_size=square", "数码电子"},
            {"无线耳机", "主动降噪无线耳机，续航30小时，音质出众", 399.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20earbuds%20case%2C%20premium%20white%20design&image_size=square", "数码电子"},
            {"平板电脑", "10.1英寸高清屏幕，高性能处理器，学习娱乐两用", 2999.00, 80, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tablet%20computer%2C%20modern%20slim%20design&image_size=square", "数码电子"},
            {"笔记本电脑", "14英寸轻薄本，全金属机身，超长续航", 5999.00, 60, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=laptop%20computer%2C%20modern%20slim%20design&image_size=square", "数码电子"},
            {"智能手机", "6.5英寸全面屏，5G网络，旗舰配置", 4999.00, 120, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20modern%20design%2C%20front%20view&image_size=square", "数码电子"},
            {"时尚口红套装", "经典色号套装，持久显色，滋润不拔干", 299.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lipstick%20set%2C%20cosmetic%20product%2C%20elegant%20design&image_size=square", "时尚美妆"},
            {"保湿精华液", "玻尿酸深层补水精华液，敏感肌适用", 199.00, 250, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=skincare%20serum%20bottle%2C%20beauty%20product&image_size=square", "时尚美妆"},
            {"防晒隔离霜", "SPF50+高倍防晒，清爽不油腻，防水防汗", 89.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sunscreen%20cream%20bottle%2C%20cosmetic%20product&image_size=square", "时尚美妆"},
            {"香水礼盒", "经典花香调淡香水，持久留香，送礼首选", 359.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=perfume%20bottle%20gift%20set%2C%20elegant&image_size=square", "时尚美妆"},
            {"眼影盘套装", "12色大地色眼影盘，粉质细腻易上色", 129.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=eyeshadow%20palette%2C%20makeup%20cosmetics&image_size=square", "时尚美妆"},
            {"洁面乳", "氨基酸温和洁面乳，深层清洁不紧绷", 69.00, 250, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=facial%20cleanser%20tube%2C%20skincare&image_size=square", "时尚美妆"},
            {"休闲运动鞋", "透气网面运动鞋，缓震舒适，适合日常穿搭", 399.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=casual%20sneakers%2C%20sport%20shoes%2C%20white%20color&image_size=square", "服装鞋包"},
            {"真皮单肩包", "头层牛皮单肩包，商务休闲两用", 899.00, 90, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=leather%20shoulder%20bag%2C%20fashion%20accessory&image_size=square", "服装鞋包"},
            {"牛仔外套", "经典牛仔外套，宽松版型，春秋必备", 299.00, 120, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=denim%20jacket%2C%20fashion%20clothing&image_size=square", "服装鞋包"},
            {"针织毛衣", "柔软亲肤针织毛衣，多色可选", 199.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=knit%20sweater%2C%20casual%20clothing&image_size=square", "服装鞋包"},
            {"双肩背包", "大容量双肩包，防泼水面料，学生通勤适用", 159.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=backpack%20bag%2C%20school%20bag&image_size=square", "服装鞋包"},
            {"帆布鞋", "经典低帮帆布鞋，舒适透气，百搭款式", 129.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=canvas%20shoes%2C%20casual%20footwear&image_size=square", "服装鞋包"},
            {"有机坚果礼盒", "精选混合坚果，营养健康，送礼佳品", 168.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=premium%20nuts%20gift%20box%2C%20healthy%20snacks&image_size=square", "食品生鲜"},
            {"进口红酒", "法国原装进口红酒，口感醇厚，果香浓郁", 388.00, 100, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=red%20wine%20bottle%2C%20elegant%20packaging&image_size=square", "食品生鲜"},
            {"进口车厘子", "智利进口车厘子，JJJ级大果，新鲜空运", 158.00, 100, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20cherries%20in%20box%2C%20red%20fruit&image_size=square", "食品生鲜"},
            {"有机大米", "东北五常有机大米，10斤装，香甜软糯", 79.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=rice%20bag%2C%20organic%20grain&image_size=square", "食品生鲜"},
            {"精品咖啡豆", "哥伦比亚阿拉比卡咖啡豆，中度烘焙", 128.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=coffee%20beans%20bag%2C%20premium&image_size=square", "食品生鲜"},
            {"手工巧克力礼盒", "比利时工艺手工巧克力，多种口味", 168.00, 120, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chocolate%20gift%20box%2C%20gourmet%20sweets&image_size=square", "食品生鲜"},
            {"北欧风格台灯", "简约设计护眼台灯，三档亮度调节", 129.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20table%20lamp%2C%20nordic%20style&image_size=square", "家居家装"},
            {"记忆棉枕头", "慢回弹记忆棉枕头，舒适护颈，助睡眠", 199.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=memory%20foam%20pillow%2C%20comfortable%20bedding&image_size=square", "家居家装"},
            {"四件套床品", "纯棉四件套，亲肤透气，多款花型可选", 299.00, 100, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cotton%20bedding%20set%2C%20home%20textile&image_size=square", "家居家装"},
            {"收纳整理箱", "三件套塑料收纳箱，透明可视，加厚耐用", 59.00, 250, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=storage%20boxes%20set%2C%20home%20organization&image_size=square", "家居家装"},
            {"香薰蜡烛套装", "天然大豆蜡香薰蜡烛，三种香型", 89.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=scented%20candles%20set%2C%20home%20decor&image_size=square", "家居家装"},
            {"懒人沙发", "单人懒人沙发榻榻米，可折叠收纳", 399.00, 60, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lazy%20sofa%20bean%20bag%2C%20living%20room&image_size=square", "家居家装"},
            {"专业瑜伽垫", "TPE环保材质瑜伽垫，防滑耐磨，加厚设计", 159.00, 160, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=yoga%20mat%2C%20fitness%20equipment&image_size=square", "运动户外"},
            {"户外登山包", "大容量防水登山包，多口袋设计，背负舒适", 459.00, 80, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=outdoor%20backpack%2C%20hiking%20gear&image_size=square", "运动户外"},
            {"跑步运动手环", "多功能运动手环，心率监测，50米防水", 249.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fitness%20tracker%20band%2C%20sports%20gadget&image_size=square", "运动户外"},
            {"速干运动T恤", "透气速干面料，反光条设计，夜跑安全", 99.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20t-shirt%2C%20athletic%20wear&image_size=square", "运动户外"},
            {"露营帐篷", "3-4人全自动帐篷，防暴雨设计", 459.00, 60, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=camping%20tent%2C%20outdoor%20gear&image_size=square", "运动户外"},
            {"健身弹力带", "五件套阻力带，居家健身必备", 49.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=resistance%20bands%20set%2C%20fitness%20equipment&image_size=square", "运动户外"},
            {"畅销小说套装", "年度畅销小说合集，精装典藏版", 268.00, 120, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=book%20set%2C%20bestseller%20novels%2C%20hardcover%20edition&image_size=square", "图书音像"},
            {"编程入门教程", "零基础编程入门，Python实战指南", 69.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=programming%20book%2C%20tech%20education&image_size=square", "图书音像"},
            {"蓝牙音箱", "迷你便携蓝牙音箱，重低音效", 179.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mini%20bluetooth%20speaker%2C%20audio%20device&image_size=square", "图书音像"},
            {"历史人文书籍", "中国通史图文版，了解五千年文明", 88.00, 150, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=history%20book%2C%20hardcover%20edition&image_size=square", "图书音像"},
            {"儿童绘本套装", "经典童话绘本合集，启蒙认知", 128.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=children%20picture%20books%2C%20colorful&image_size=square", "图书音像"},
            {"机械键盘", "青轴机械键盘，RGB背光，游戏办公两用", 299.00, 120, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mechanical%20keyboard%2C%20RGB%20backlit&image_size=square", "图书音像"},
            {"婴儿纸尿裤", "超薄透气纸尿裤，柔软亲肤，大包装", 128.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20diapers%2C%20infant%20care%20product&image_size=square", "母婴用品"},
            {"儿童益智积木", "环保木质积木，多种造型，开发智力", 99.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=children%20blocks%2C%20educational%20toys%2C%20colorful&image_size=square", "母婴用品"},
            {"婴儿推车", "轻便可折叠婴儿推车，一键收车", 899.00, 60, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20stroller%2C%20lightweight%20foldable&image_size=square", "母婴用品"},
            {"婴幼儿奶粉", "进口配方奶粉，3段800g装", 298.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20formula%20milk%20can%2C%20infant&image_size=square", "母婴用品"},
            {"儿童安全座椅", "9个月-12岁儿童安全座椅，ISOFIX接口", 1299.00, 40, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=child%20car%20seat%2C%20safety%20product&image_size=square", "母婴用品"},
            {"婴儿洗衣液", "无荧光剂婴儿专用洗衣液，2L装", 39.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=baby%20laundry%20detergent%20bottle&image_size=square", "母婴用品"},
            {"车载空气净化器", "高效除甲醛车载净化器，静音设计", 299.00, 100, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20air%20purifier%2C%20auto%20accessory&image_size=square", "汽车用品"},
            {"汽车座椅靠垫", "记忆棉汽车靠垫，护腰护颈，长途必备", 159.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20seat%20cushion%2C%20memory%20foam&image_size=square", "汽车用品"},
            {"车载手机支架", "磁吸式出风口手机支架，稳固不掉落", 39.00, 250, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20phone%20mount%2C%20magnetic%20holder&image_size=square", "汽车用品"},
            {"汽车脚垫", "全包围TPE汽车脚垫，专车专用", 299.00, 80, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20floor%20mats%2C%20auto%20accessory&image_size=square", "汽车用品"},
            {"应急启动电源", "大容量汽车应急启动电源，搭电神器", 459.00, 60, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20jump%20starter%2C%20emergency%20battery&image_size=square", "汽车用品"},
            {"创意陶瓷摆件", "手工制作陶瓷摆件，新中式风格", 239.00, 80, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20figurine%2C%20home%20decor%20craft&image_size=square", "其他"},
            {"绿植盆栽", "多肉植物组合盆栽，含花盆", 49.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=succulent%20plants%20pot%2C%20green%20decor&image_size=square", "其他"},
            {"保温杯", "316不锈钢真空保温杯，500ml", 89.00, 250, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=thermos%20cup%2C%20stainless%20steel&image_size=square", "其他"},
            {"雨伞", "全自动折叠晴雨伞，抗风加固", 59.00, 200, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=folding%20umbrella%2C%20rain%20gear&image_size=square", "其他"},
            {"充电宝", "20000mAh快充充电宝，轻薄便携", 129.00, 180, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=power%20bank%2C%20portable%20charger&image_size=square", "其他"},
            {"桌面台历", "2026年创意桌面台历，精美插画", 29.00, 300, "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=desk%20calendar%202026%2C%20office%20supply&image_size=square", "其他"}
        };

        for (Object[] p : products) {
            Product product = new Product();
            product.setName((String) p[0]);
            product.setDescription((String) p[1]);
            product.setPrice(((Double) p[2]).doubleValue());
            product.setStock((Integer) p[3]);
            product.setImage((String) p[4]);
            product.setStatus(1);
            product.setCategory((String) p[5]);
            productRepository.insert(product);
        }

        System.out.println("[DataInitializer] Successfully initialized " + products.length + " products!");
    }
}
