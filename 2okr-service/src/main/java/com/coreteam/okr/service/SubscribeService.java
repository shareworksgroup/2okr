package com.coreteam.okr.service;

import com.coreteam.okr.dto.subscribe.SubscriptionCreateRequestDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.entity.Subscribe;

/**
 * @ClassName: SubscribeService
 * @Description 用户订阅
 * @Author sean.deng
 * @Date 2019/08/26 11:34
 * @Version 1.0.0
 */
public interface SubscribeService {

    /**
     * 添加一个订阅
     * @param requestDTO
     */
    void createSubscription(SubscriptionCreateRequestDTO requestDTO);

    /**
     * 获取在使用状态的订阅
     * @param organizationId
     * @return
     */
    SubscriptionDTO getActiveSubscription(Long organizationId);

    /**
     * 获取最后尚未支付的订阅
     * @param organizationId
     * @return
     */
    SubscriptionDTO getLastUnPaidSubscription(Long organizationId);


    Subscribe getSubscriptionForOrder(Long subscriptionId);

    /**
     * 激活当前订阅
     * @param id
     */
    void activeSubscription(Long id);

    /**
     * 删除未支付的
     * @param id
     */
    void deleteUnpaidSubscriptionBefore(Long id);

    void deleteSubscription(Long id);

    /**
     *  task 任务，每个月最后1天计费下个月的消费
     */
    void consumerSubscriptionPerMonthAtLastDay();


    /**
     * 更新subscription的状态
     */
    void updateSubscriptionStatus();

    void updateReminderUserAmount(Long organizationId);
}
