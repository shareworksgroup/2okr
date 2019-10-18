package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.WeeklyPlanItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: WeeklyPlanItemDAO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Mapper
public interface WeeklyPlanItemDAO extends MyBatisBaseDao<WeeklyPlanItem, Long> {

    /**
     * list 未完成的weeklyplan
     *
     * @param userId
     * @param week
     * @param organizationId
     * @param templateId
     * @param needCarryStatus
     * @return
     */
    List<WeeklyPlanItem> listUndoneWeeklyPlanItemByUser(@Param("userId") Long userId, @Param("week") Integer week, @Param("organizationId") Long organizationId, @Param("templateId") Long templateId, @Param("needCarryCategorid") List<Long> needCarryStatus);



    /**
     * list 指定weekly的所有plan
     *
     * @param weeklyPlanId
     * @return
     */
    List<WeeklyPlanItem> listUserWeeklyPlanItemByWeeklyPlanId(@Param("weeklyPlanId") Long weeklyPlanId);


    /**
     * 获取本周所有的weeklyplan item
     *
     * @param week
     * @return
     */
    List<WeeklyPlanItem> listWeeklyPlanItem(Integer week);

    void updateOwnerName(@Param("userId") Long userId, @Param("userName") String userName);

}