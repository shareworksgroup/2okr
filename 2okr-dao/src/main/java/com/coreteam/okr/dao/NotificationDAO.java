package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * NotificationDAO继承基类
 */
@Mapper
public interface NotificationDAO extends MyBatisBaseDao<Notification, Long> {
    Integer countNotifycationByUser(Long userId);

    void markRead(Long id);

    List<Notification> listNotifycationByUser(Long userId);

    void markAllReadByUser(Long id);
}