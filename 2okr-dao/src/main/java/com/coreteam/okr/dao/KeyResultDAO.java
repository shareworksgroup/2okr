package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.objective.ObjectiveKeyResultDTO;
import com.coreteam.okr.entity.KeyResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * KeyResultDAO继承基类
 */
@Mapper
public interface KeyResultDAO extends MyBatisBaseDao<KeyResult, Long> {
    /**
     * list objective的key-result
     * @param objectiveId
     * @return
     */
    List<KeyResult> listObjectiveKeyResultByObjectiveId(Long objectiveId);

    /**
     * 更新key-result的owner name
     * @param userId
     * @param userName
     */
    void updateOwnerName(@Param("userId") Long userId, @Param("userName") String userName);

    /**
     * list objective的key-result 绑定评论信息
     * @param objectiveId
     * @return
     */
    List<ObjectiveKeyResultDTO> listObjectiveKeyResultCombineCommentNumByObjectiveId(Long objectiveId);

    /**
     * 获取所有open objective 的key-result
     * @return
     */
    List<KeyResult> listKeyResultForOpenObjective();
}