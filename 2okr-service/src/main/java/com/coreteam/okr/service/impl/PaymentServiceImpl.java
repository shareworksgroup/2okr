package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.PaymentStatusEnum;
import com.coreteam.okr.dao.OrderDAO;
import com.coreteam.okr.dao.PaymentDAO;
import com.coreteam.okr.dao.SubscribeDAO;
import com.coreteam.okr.dto.order.OrderDTO;
import com.coreteam.okr.dto.payment.PaymentRequestDTO;
import com.coreteam.okr.dto.payment.PaymentResponseDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Order;
import com.coreteam.okr.entity.Payment;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayNotify;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentRequest;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentResponse;
import com.coreteam.okr.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: PaymentServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 13:42
 * @Version 1.0.0
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${alipay.appId}")
    private String appId;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TradeNoGenerater tradeNoGenerater;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private SubscribeDAO subscribeDAO;

    @Autowired
    private PaymentGateWayService paymentGateWayService;

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    @Transactional
    public PaymentResponseDTO requestForPayment(PaymentRequestDTO request) {
        OrderDTO order = orderService.getOrderForSubscription(request.getSubscriptionId());
        Payment payment = createPayment(order,request);
        AlipayPaymentRequest requestForPayment = buildPaymentRequest(payment, request);
        AlipayPaymentResponse response = paymentGateWayService.requestForPayment(requestForPayment);
        return BeanConvertUtils.transfrom(PaymentResponseDTO.class, response);
    }

    @Override
    public void handleAlipayCallBack(Map<String, String> params) {
        //校验签名
        boolean signVerified = paymentGateWayService.rsaCheckV1(params);
        if (signVerified) {
            AlipayNotify notify = AlipayNotify.buildAlipayNotify(params);
            executorService.execute(() -> {
                handlePaymentCallback(notify);
            });
        } else {
            log.error("PaymentServiceImpl:handleAlipayCallBack signVerified failer ");
        }
    }

    @Transactional
    public void handlePaymentCallback(AlipayNotify notify) {
        try{
            checkPaymentStatus(notify);
            Payment payment = this.paymentDAO.getPaymentByTradeNo(Long.valueOf(notify.getOutTradeNo()));
            Order order= this.orderService.getOrderByOrderNo(payment.getOrderNo());
            updatePaymentStatusPaied(payment,notify);
            this.orderService.updateOrderStatusForPaid(order,notify);
            this.subscribeService.activeSubscription(order.getSubscribeId());
            this.subscribeService.deleteUnpaidSubscriptionBefore(order.getSubscribeId());
        }catch (Exception e){
            log.error("PaymentServiceImpl:handlePaymentCallback handler error"+ ExceptionUtil.stackTraceToString(e));
        }
    }

    public void updatePaymentStatusPaied(Payment payment,AlipayNotify notify){
        payment.setUserPayAmount(notify.getBuyerPayAmount());
        payment.setReceiptAmount(notify.getReceiptAmount());
        payment.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        payment.setPaymentAt(notify.getGmtPayment());
        payment.setChannelTradeNo(notify.getTradeNo());
        payment.initializeForUpdate();
        this.paymentDAO.updateByPrimaryKeySelective(payment);
    }


    private void checkPaymentStatus(AlipayNotify notify) {
        String outTradeNo = notify.getOutTradeNo();
        Payment payment = this.paymentDAO.getPaymentByTradeNo(Long.valueOf(outTradeNo));
        if (payment == null) {
            throw new CustomerException(" Trade No not exist");
        }
        BigDecimal totalAmount = notify.getTotalAmount();
        if (!totalAmount.equals(payment.getOrderAmount())) {
            throw new CustomerException(" error total_amount");
        }
        if (!appId.equals(notify.getAppId())) {
            throw new CustomerException("error app_id ");
        }
    }


    public Payment createPayment(OrderDTO order,PaymentRequestDTO request) {
        Payment payment = new Payment();
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        payment.setTradeNo(tradeNoGenerater.generateTradeNo());
        payment.setOrderNo(order.getOrderNo());
        payment.setOrderAmount(order.getOrderAmount());
        payment.setUserPayAmount(order.getOrderAmount());
        payment.setGatewayType(request.getGatewayType());
        payment.setPaymentStatus(PaymentStatusEnum.PROCEED);
        payment.setCreatedUser(currentUser.getId());
        payment.setCreatedName(currentUser.getName());
        payment.initializeForInsert();
        this.paymentDAO.insertSelective(payment);
        return payment;
    }

    public AlipayPaymentRequest buildPaymentRequest(Payment payment, PaymentRequestDTO requestDTO) {
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        request.setTotalAmount(payment.getOrderAmount());
        request.setOutTradeNo(String.valueOf(payment.getTradeNo()));
        request.setPaymentChannelType(com.coreteam.okr.payment.gateway.GatewayTypeEnum.valueOf(requestDTO.getGatewayType().name()));
        request.setSubject("2OKR subscription");
        request.setBody("2OKR subscription");
        return request;
    }
}
