package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.WeeklyPlanTemplateItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WeeklyPlanTemplateItemDAO继承基类
 */
@Mapper
public interface WeeklyPlanTemplateItemDAO extends MyBatisBaseDao<WeeklyPlanTemplateItem, Long> {
    /**
     * 根据模板id,获取item信息
     * @param templateId
     * @return
     */
    List<WeeklyPlanTemplateItem> selectByTemplateId(@Param("templateId") Long templateId);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<WeeklyPlanTemplateItem> list);
}
