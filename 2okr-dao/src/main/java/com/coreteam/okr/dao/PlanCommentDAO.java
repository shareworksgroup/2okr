package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.PlanComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Mapper
public interface PlanCommentDAO extends MyBatisBaseDao<PlanComment, Long> {
    List<PlanComment> listCommentByPlanId(Long planId);
}