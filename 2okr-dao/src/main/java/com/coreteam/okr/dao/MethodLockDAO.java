package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.MethodLock;
import org.apache.ibatis.annotations.Mapper;

/**
 * MethodLockDAO继承基类
 */
@Mapper
public interface MethodLockDAO extends MyBatisBaseDao<MethodLock, Long> {
    void deleteByMethodName(String key);
}