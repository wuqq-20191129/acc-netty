//package com.wuqq.config;
//
//import com.wuqq.mapper.st.SysGroupMapper;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//
///**
// * @Classname TestConfigration
// * @Description @Configration 初始化容器
// * @Date 2020/12/23 16:09
// * @Created by mh
// */
//
//@Configuration
//public class TestConfigration {
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driver;
//
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Resource
//    public SysGroupMapper sysGroupMapper;
//
//    private static Logger logger = Logger.getLogger(TestConfigration.class);
//
//    public TestConfigration(){
//        logger.info("configration 容器初始化成功");
//    }
//
//    @Bean
//    public TestJdbc testJdbc(){
//        //ArrayList<Map> arrayList =(ArrayList<Map>) sysGroupMapper.findAll();
//        //logger.info(arrayList.toString());
//        return  new TestJdbc();
//    }
//}
