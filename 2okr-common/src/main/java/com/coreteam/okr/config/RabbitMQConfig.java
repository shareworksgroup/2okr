package com.coreteam.okr.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RabbitMQConfig
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 9:12
 * @Version 1.0.0
 */
@Configuration
public class RabbitMQConfig {

    public static final String OKR_QUEUE="okr_queue";

    public static final String SSO_QUEUE="sso_queue";

    public static final String MSG_QUEUE="msg_queue";

    public static final String OKR_ROUTE_KEY="okr_queue";

    public static final String MSG_ROUTE_KEY="msg_queue";

    public static final String EXCAHNGE="okrExchange";


    /**
     * 定义sso的发送队列
     * @return
     */
    @Bean("okr")
    Queue queueOKR(){
        return new Queue(OKR_QUEUE,true);
    }

    /**
     * 定义msg的发送队列
     * @return
     */
    @Bean("msg")
    Queue queueMSG(){
        return new Queue(MSG_QUEUE,true);
    }


    /**
     * 定义exchange
     */
    @Bean
    TopicExchange exchange(){
        return new TopicExchange(EXCAHNGE);
    }

    /**
     * 绑定队列sso
     */

    @Bean
    Binding bindingOKR(@Qualifier("okr") Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(OKR_ROUTE_KEY);
    }

    @Bean
    Binding bindingMSG(@Qualifier("msg") Queue queue,TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MSG_ROUTE_KEY);
    }



}
