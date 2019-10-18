package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.PlanObjectiveLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Mapper
public interface PlanObjectiveLinkDAO extends MyBatisBaseDao<PlanObjectiveLink, Long> {
    void deleteByPlanIdAndObjectiveId(@Param("planId") Long planId, @Param("objectiveId") Long objectiveId);
    void deleteAllObjectiveLinked(@Param("planId") Long id);
}