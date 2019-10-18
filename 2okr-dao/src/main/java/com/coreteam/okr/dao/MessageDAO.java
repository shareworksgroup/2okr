package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * MessageDAO继承基类
 */
@Mapper
public interface MessageDAO extends MyBatisBaseDao<Message, Long> {
}