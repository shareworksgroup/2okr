package com.coreteam.okr.web.listener;

import com.coreteam.okr.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: RabbitMqReciver
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 12:37
 * @Version 1.0.0
 */
@Component
public class RabbitMqReciver {

    @Autowired
    private InviteRegisterHandler inviteRegisterHandler;

    @Autowired
    private UserInfoUpdateHandler userInfoUpdateHandler;

    @RabbitListener(queues = RabbitMQConfig.SSO_QUEUE)
    public void handleSSOMessage(String rawData){
        inviteRegisterHandler.handle(rawData);
        userInfoUpdateHandler.handle(rawData);
    }
    
}
