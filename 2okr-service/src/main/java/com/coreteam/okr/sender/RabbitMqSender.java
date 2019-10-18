package com.coreteam.okr.sender;

import com.coreteam.okr.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: RabbitMqSender
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 9:54
 * @Version 1.0.0
 */
@Component
public class RabbitMqSender {

    @Autowired
    private RabbitTemplate amqpTemplate;

    private void send(String routeKey, Object message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCAHNGE, routeKey, message);
    }

    public void send2MSG(Object message){
        send(RabbitMQConfig.MSG_ROUTE_KEY,message);
    }

    public void send(Object message){
        send(RabbitMQConfig.OKR_ROUTE_KEY,message);
    }

}
