package com.wuqq.task;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Classname TccTask
 * @Description 转换轮询线程
 * @Date 2021/1/6 14:40
 * @Created by mh
 */
public class TccTask {

    private static Logger logger = Logger.getLogger(TccTask.class);

    @Scheduled(fixedRate = 1000)
    public void getTccFile(){
        //logger.info("test for aotu!");
    }
}
