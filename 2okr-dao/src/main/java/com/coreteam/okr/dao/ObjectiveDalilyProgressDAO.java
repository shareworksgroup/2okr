package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.ObjectiveDalilyProgress;
import org.apache.ibatis.annotations.Mapper;

/**
 * ObjectiveDalilyProgressDAO继承基类
 */
@Mapper
public interface ObjectiveDalilyProgressDAO extends MyBatisBaseDao<ObjectiveDalilyProgress, Long> {
}