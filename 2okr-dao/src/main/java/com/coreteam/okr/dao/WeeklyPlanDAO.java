package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.plan.WeeklyPlanReportSummaryDTO;
import com.coreteam.okr.entity.WeeklyPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: WeeklyPlanDAO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Mapper
public interface WeeklyPlanDAO extends MyBatisBaseDao<WeeklyPlan, Long> {
    WeeklyPlan getUserWeeklyPlanByOrg(@Param("userId") Long userId, @Param("week") Integer week, @Param("organizationId") Long orgId, @Param("templateId") Long templateId);

    List<WeeklyPlan> listOrgWeeklyPlanReported(@Param("organizationId") Long organizationId, @Param("userId") Long id, @Param("week") Integer week, @Param("templateId") Long templateId);

    List<WeeklyPlanReportSummaryDTO> listTeamWeeklyPlanReportSummary(@Param("organizationId") Long id, @Param("userId") Long userId, @Param("week") Integer week, @Param("templateId") Long templateId);

    void updateOwnerName(@Param("userId") Long userId, @Param("userName") String userName);
    /**
     * 获取用户weeklyplan的上周
     * @param userId
     * @param week
     * @param organizationId
     * @param templateId
     * @return
     */
    Integer getLaskWeekForUserWeeklyPlan(@Param("userId") Long userId, @Param("week") Integer week, @Param("organizationId") Long organizationId, @Param("templateId") Long templateId);
}