package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dao.query.BussinessLogPageQuery;
import com.coreteam.okr.entity.BussinessLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BussinessLogDAO继承基类
 */
@Mapper
public interface BussinessLogDAO extends MyBatisBaseDao<BussinessLog, Long> {
    List<BussinessLog> pageListLogs(BussinessLogPageQuery query);
}