package com.icode.api.common.listener;

import com.icode.api.common.constant.CustomFinal;
import com.icode.api.common.constant.SystemFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Title: ApplicationInitListener监听器<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/2/28 17:17<br>
 */
public class ApplicationInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationInitListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();

        LOGGER.info("Application Init Ready");

        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();

        LOGGER.info("profiles..................{}", profiles);

        DictionaryClientHandle dictionaryClientHandle = applicationContext.getBean(DictionaryClientHandle.class);

        if (profiles != null && profiles.length > 0) {
            CustomFinal.CUSTOM_ENVIRONMENT_PROFILES_KEY_MD5_PREFIX = String.format(CustomFinal.CUSTOM_ENVIRONMENT_PROFILES_KEY_MD5_PREFIX, profiles[0], SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON);

            dictionaryClientHandle.loadLocalData(applicationContext, profiles[0]);
        } else {
            LOGGER.error("Spring Boot Application Load Profiles Error!");
        }
    }
}
