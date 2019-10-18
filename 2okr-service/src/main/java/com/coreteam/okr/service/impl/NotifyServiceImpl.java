package com.coreteam.okr.service.impl;

import com.alibaba.fastjson.JSON;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dao.MessageDAO;
import com.coreteam.okr.dao.NotificationDAO;
import com.coreteam.okr.dto.Notify.MessageDTO;
import com.coreteam.okr.dto.Notify.NotificationDTO;
import com.coreteam.okr.entity.Message;
import com.coreteam.okr.entity.Notification;
import com.coreteam.okr.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: NotifyServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 9:04
 * @Version 1.0.0
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private NotificationDAO notifycationDao;

    @Autowired
    private MessageDAO messageDAO;

    @Override
    @Transactional
    public void saveNotification(NotificationDTO notifycationdto) {
        MessageDTO messageDTO=notifycationdto.getMessage();
        Message message=new Message(messageDTO.getTitle(),messageDTO.getMessage().toJSONString());
        message.initializeForInsert();
        this.messageDAO.insertSelective(message);
        Notification notification=new Notification(message.getId(),notifycationdto.getUserId(),notifycationdto.getState(),notifycationdto.getType());
        notification.initializeForInsert();
        this.notifycationDao.insertSelective(notification);
   }

    @Override
    public List<NotificationDTO> listNotifyicationByUser(Long userId) {
        List<NotificationDTO> dtoList=new ArrayList<>();
        List<Notification> list= this.notifycationDao.listNotifycationByUser(userId);
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(notification -> {
                Message message = this.messageDAO.selectByPrimaryKey(notification.getMessageId());
                MessageDTO dto=new MessageDTO();
                BeanConvertUtils.copyEntityProperties(message,dto);
                dto.setMessage(JSON.parseObject(message.getMessage()));
                NotificationDTO notificationDTO=new NotificationDTO();
                BeanConvertUtils.copyEntityProperties(notification,notificationDTO);
                notificationDTO.setMessage(dto);
                dtoList.add(notificationDTO);
            });
        }
        return dtoList;
    }

    @Override
    public void markRead(Long id) {
        this.notifycationDao.markRead(id);
    }

    @Override
    public Integer countNotifycationByUser(Long userId) {
        return this.notifycationDao.countNotifycationByUser(userId);
    }

    @Override
    public void markAllNotificationReadByUser(Long id) {
        this.notifycationDao.markAllReadByUser(id);
    }


}
