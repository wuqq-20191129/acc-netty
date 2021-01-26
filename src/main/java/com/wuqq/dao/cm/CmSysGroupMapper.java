package com.wuqq.dao.cm;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname SysGroupMapper
 * @Description Mapper
 * @Date 2020/12/25 15:38
 * @Created by mh
 */
@Mapper
public interface CmSysGroupMapper {

    public List<Map> findAll();

}


