package com.coreteam.okr.service;

import com.coreteam.okr.dto.Notify.NotificationDTO;

import java.util.List;

/**
 * @ClassName: NotifyService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/11 16:09
 * @Version 1.0.0
 */
public interface NotifyService {

    void saveNotification(NotificationDTO notifycation);

    List<NotificationDTO> listNotifyicationByUser(Long userId);

    void markRead(Long id);

    Integer countNotifycationByUser(Long userId);

    void markAllNotificationReadByUser(Long id);
}
