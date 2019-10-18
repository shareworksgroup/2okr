package com.coreteam.okr.manager;

import com.coreteam.okr.dto.Notify.NotificationDTO;

import java.util.List;

/**
 * @ClassName: NotifyManager
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 9:03
 * @Version 1.0.0
 */
public interface NotifyManager {

    void sendNotify(Notify noify);

    List<NotificationDTO> listSystemNotify(Long userId);

    void markSystemNotifyRead(Long id);
    
    void markAllSystemNotifyReadByUser(Long id);

    Integer countSystemNotiry(Long userId);

}
