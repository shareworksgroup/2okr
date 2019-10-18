package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.KeyResultCommentReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * KeyResultCommentReplyDAO继承基类
 */
public interface KeyResultCommentReplyDAO extends MyBatisBaseDao<KeyResultCommentReply, Long> {

    List<KeyResultCommentReply> listRepaysByCommentId(@Param("commentId") Long commentId);
}