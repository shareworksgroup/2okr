package com.coreteam.okr.service;

import com.coreteam.okr.dto.order.OrderDTO;
import com.coreteam.okr.entity.Order;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayNotify;

/**
 * @ClassName: OrderService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 11:54
 * @Version 1.0.0
 */
public interface OrderService {
    OrderDTO getOrderForSubscription(Long subscriptionId);

    Order getOrderByOrderNo(Long id);

    void updateOrderStatusForPaid(Order order , AlipayNotify notify);
}
