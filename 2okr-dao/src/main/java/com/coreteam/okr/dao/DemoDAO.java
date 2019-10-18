package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Demo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * DemoDAO继承基类
 */
@Mapper
public interface DemoDAO extends MyBatisBaseDao<Demo, Integer> {
    List<Demo> selectList();
}
