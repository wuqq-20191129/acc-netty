package com.wuqq.dao.cm;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * @Classname CmDbConfig
 * @Description TODO
 * @Date 2021/1/4 14:55
 * @Created by mh
 */
@Mapper
public interface CmDbConfig {

    public List<Map> getConfig();
}
