package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.MemberSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MemberSettingDAO继承基类
 */
@Mapper
public interface MemberSettingDAO extends MyBatisBaseDao<MemberSetting, Long> {

    MemberSetting getSettingByKey(@Param("userId") Long userId, @Param("type") String type, @Param("key") String key);


    List<MemberSetting> listSettingByType(@Param("userId") Long userId, @Param("type") String type);

    void deleteByType(@Param("userId") Long id, @Param("type") String type);
}