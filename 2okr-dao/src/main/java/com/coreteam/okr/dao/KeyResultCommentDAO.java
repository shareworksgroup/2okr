package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.KeyResultComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * KeyResultCommentDAO继承基类
 */
@Mapper
public interface KeyResultCommentDAO extends MyBatisBaseDao<KeyResultComment, Long> {
    void deleteByKeyResultId(Long keyResultId);

    List<KeyResultComment> listKeyResultCommentsByKeyResultId(Long keyResultId);
}