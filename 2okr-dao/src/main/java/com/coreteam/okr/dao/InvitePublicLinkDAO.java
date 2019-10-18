package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.InvitePublicLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * PulbicLinkInviteDAO继承基类
 */
@Mapper
public interface InvitePublicLinkDAO extends MyBatisBaseDao<InvitePublicLink, Long> {

}