package com.coreteam.okr.service;

import com.coreteam.okr.dto.payment.PaymentRequestDTO;
import com.coreteam.okr.dto.payment.PaymentResponseDTO;

import java.util.Map;

/**
 * @ClassName: PaymentService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 13:42
 * @Version 1.0.0
 */
public interface PaymentService {

    /**
     * 创建支付请求
     *
     * @param request
     * @return
     */
    PaymentResponseDTO requestForPayment(PaymentRequestDTO request);

    /**
     * 处理支付宝支付回调
     *
     * @param params
     */
    void handleAlipayCallBack(Map<String, String> params);

}
