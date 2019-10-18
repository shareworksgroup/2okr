package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.WeeklyPlanTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * WeeklyPlanTemplateDAO继承基类
 */
@Mapper
public interface WeeklyPlanTemplateDAO extends MyBatisBaseDao<WeeklyPlanTemplate, Long> {
    /**
     * 根据类型和类型隐射的数据主键id,返回唯一的week plan tempalte数据
     * @param type
     * @param entityId
     * @return
     */
    WeeklyPlanTemplate selectByTypeAndEntityId(@Param("type") String type, @Param("entityId") Long entityId);
}
