package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.OrderStatusEnum;
import com.coreteam.okr.dao.OrderDAO;
import com.coreteam.okr.dto.order.OrderDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Order;
import com.coreteam.okr.entity.Subscribe;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayNotify;
import com.coreteam.okr.service.OrderNoGenerater;
import com.coreteam.okr.service.OrderService;
import com.coreteam.okr.service.SubscribeService;
import com.coreteam.okr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: OrderServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 11:55
 * @Version 1.0.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderNoGenerater orderNoGenerater;

    @Autowired
    private UserService userService;


    @Override
    public OrderDTO getOrderForSubscription(Long subscriptionId) {
        Subscribe subscribeNeedPay = subscribeService.getSubscriptionForOrder(subscriptionId);
        Order order = this.orderDAO.getOrderBySubscribeId(subscribeNeedPay.getId());
        if (order != null) {
            updateOrderAmount(order, subscribeNeedPay);
        } else {
            order = createOrder(subscribeNeedPay);
        }
        return BeanConvertUtils.transfrom(OrderDTO.class, order);
    }

    @Override
    public Order getOrderByOrderNo(Long no) {
        return this.orderDAO.getOrderByOrderNo(no);
    }

    @Override
    public void updateOrderStatusForPaid(Order order, AlipayNotify notify) {
        order.setOrderStatus(OrderStatusEnum.PAID);
        order.setPaymentAt(notify.getGmtPayment());
        order.initializeForUpdate();
        this.orderDAO.updateByPrimaryKeySelective(order);
    }

    public void updateOrderAmount(Order order, Subscribe subscribeNeedPay) {
        if (order.getOrderStatus() != OrderStatusEnum.UNPAID) {
            throw new CustomerException("order is paied");
        }
        order.setOrderAmount(subscribeNeedPay.getSubscribePayableAmount());
        this.orderDAO.updateByPrimaryKeySelective(order);
    }

    public Order createOrder(Subscribe subscribeNeedPay) {
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        Order order = new Order();
        order.setOrganizationId(subscribeNeedPay.getOrganizationId());
        order.setSubscribeId(subscribeNeedPay.getId());
        order.setOrderNo(orderNoGenerater.generateOrderNo());
        order.setOrderAmount(subscribeNeedPay.getSubscribePayableAmount());
        order.setOrderStatus(OrderStatusEnum.UNPAID);
        order.setOrderDate(new Date());
        order.setCreatedUser(currentUser.getId());
        order.setCreatedName(currentUser.getName());
        order.initializeForInsert();
        this.orderDAO.insertSelective(order);
        return order;
    }

}
