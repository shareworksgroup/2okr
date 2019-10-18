package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Invite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * InviteDAO继承基类
 */
@Mapper
public interface InviteDAO extends MyBatisBaseDao<Invite, Long> {

    /**
     * 获取未发送join邀请的invite
     * @param email
     * @return
     */
    List<Invite> listRegisterInvitedEmail(@Param("email") String email);
}