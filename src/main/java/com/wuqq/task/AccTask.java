package com.wuqq.task;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Classname AccTask
 * @Description TODO
 * @Date 2021/1/6 14:35
 * @Created by mh
 */
@Configuration
public class AccTask {

    private static Logger logger  = Logger.getLogger(AccTask.class);

    @Bean
    public TccTask task(){
        logger.info("初始化 task");
        return new TccTask();
    }

}
