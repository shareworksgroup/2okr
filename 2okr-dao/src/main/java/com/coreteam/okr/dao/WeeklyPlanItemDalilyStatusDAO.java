package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.WeeklyPlanItemDalilyStatus;
import org.apache.ibatis.annotations.Mapper;

/**
 * WeeklyPlanItemDalilyStatusDAO继承基类
 */
@Mapper
public interface WeeklyPlanItemDalilyStatusDAO extends MyBatisBaseDao<WeeklyPlanItemDalilyStatus, Long> {
}