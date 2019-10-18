package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.MemberInvitationJoinRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * MemberInvitationJoinRecordDAO继承基类
 */
@Mapper
public interface MemberInvitationJoinRecordDAO extends MyBatisBaseDao<MemberInvitationJoinRecord, Long> {
}