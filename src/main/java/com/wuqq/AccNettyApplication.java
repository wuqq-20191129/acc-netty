package com.wuqq;

import com.wuqq.dao.st.StLccIp;
import com.wuqq.server.NettySever;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.Map;

//修改nettyServer 启动方式
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableScheduling
public class AccNettyApplication {
    //
    //private static Logger logger = Logger.getLogger(AccNettyApplication.class);
    public static void main(String[] args) {

        //logger.debug("This is debug message.");
        //// 记录info级别的信息
        //logger.info("This is info message.");
        //// 记录error级别的信息
        //logger.error("This is error message.");
        //上下文获取bean 启动
        ApplicationContext context = SpringApplication.run(AccNettyApplication.class, args);
        //context.getBeanDefinitionCount();
        //String[] beanNames=context.getBeanDefinitionNames();
        NettySever nettySever = context.getBean(NettySever.class);
        //StLccIp stLccIp = context.getBean(StLccIp.class);
        //ArrayList<Map> arrayList =(ArrayList<Map>) stLccIp.getLccIp();
        nettySever.run();
    }

}
