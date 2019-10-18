package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.plan.PlanWithCommentNumDTO;
import com.coreteam.okr.entity.Plan;
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
public interface PlanDAO extends MyBatisBaseDao<Plan, Long> {
    void updateOwnerName(@Param("userId") Long userId, @Param("userName") String userName);

    PlanWithCommentNumDTO getPlanConbineComemntNumById(Long planId);
}