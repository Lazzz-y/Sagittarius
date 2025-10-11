package io.github.lazzz.sagittarius.auth.config;


import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;

/**
 * 验证码资源配置
 * 
 * @author Lazzz 
 * @date 2025/09/29 11:50
**/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CaptchaResourceConfiguration {

    private final ResourceStore resourceStore;

    @PostConstruct
    public void initResourceStore() throws Exception {
        var bgd = "captcha/background";
        var bgList = new ArrayList<String>();
        String pattern = "classpath*:".concat(bgd).concat("/**");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(pattern);
        for (org.springframework.core.io.Resource resource : resources) {
            String path = resource.getURL().getPath();
            if (!path.endsWith("/")){
                String background = path.substring(path.lastIndexOf(bgd));
                log.info("找到背景图片: {}", background);
                bgList.add(background);
            }
        }
        bgList.forEach(bg -> {
            Resource resource = new Resource("classpath", bg);
            resourceStore.addResource(CaptchaTypeConstant.CONCAT, resource);
            resourceStore.addResource(CaptchaTypeConstant.SLIDER, resource);
            resourceStore.addResource(CaptchaTypeConstant.ROTATE, resource);
            resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, resource);
            log.info("加载背景图片: {}", bg);
        });
    }
    
//    @PostConstruct
//    public void init() {
//        ResourceMap template1 = new ResourceMap("sagittarius", 4);
//        template1.put("active.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("rotate_1/active.png")));
//        template1.put("fixed.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("rotate_1/fixed.png")));
//        ResourceMap template2 = new ResourceMap("sagittarius", 4);
//        template2.put("active.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("slider_1/active.png")));
//        template2.put("fixed.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("slider_1/fixed.png")));
//        ResourceMap template3 = new ResourceMap("sagittarius", 4);
//        template3.put("active.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("slider_2/active.png")));
//        template3.put("fixed.png", new Resource(
//                ClassPathResourceProvider.NAME, SystemConstants.CAPTCHA_TEMPLATE_DIR.concat("slider_2/fixed.png")));
//
//        resourceStore.addTemplate(CaptchaTypeConstant.ROTATE, template1);
//        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template2);
//        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template3);
//
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("1.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("a.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("b.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("c.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("d.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("e.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("f.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("g.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("h.jpg"), "sagittarius"));
//        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", SystemConstants.CAPTCHA_BACKGROUND_DIR.concat("c.jpg"), "sagittarius"));
//        System.out.println("验证码资源初始化完毕...");
//    }
}

