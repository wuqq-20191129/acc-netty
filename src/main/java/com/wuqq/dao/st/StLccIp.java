package com.wuqq.dao.st;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @Classname StLccIp
 * @Description TODO
 * @Date 2021/1/5 14:08
 * @Created by mh
 */
@Mapper
@Repository
public interface StLccIp {

    public List<Hashtable<String,String >> getLccIp();

    public List<Hashtable<String,String >> getBomIp();
}
