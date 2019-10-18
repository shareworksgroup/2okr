package com.coreteam.okr.service;

import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentRequest;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentResponse;

import java.util.Map;

/**
 * @ClassName: PaymentGateWayService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/29 10:50
 * @Version 1.0.0
 */
public interface PaymentGateWayService {

    AlipayPaymentResponse requestForPayment(AlipayPaymentRequest request);

    Boolean rsaCheckV1(Map<String, String> param);


}
