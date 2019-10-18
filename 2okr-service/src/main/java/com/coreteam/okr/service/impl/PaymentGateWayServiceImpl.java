package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.okr.payment.exception.PaymentGatewayException;
import com.coreteam.okr.payment.platform.alipay.AlipayConfig;
import com.coreteam.okr.payment.platform.alipay.AlipayPlatform;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentRequest;
import com.coreteam.okr.payment.platform.alipay.payment.AlipayPaymentResponse;
import com.coreteam.okr.service.PaymentGateWayService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: PaymentGateWayServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/29 11:06
 * @Version 1.0.0
 */
@Service
public class PaymentGateWayServiceImpl implements PaymentGateWayService, InitializingBean {

    @Value("${alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${alipay.returnUrl}")
    private String returnUrl;
    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.alipayPrivateKey}")
    private String alipayPrivateKey;
    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;
    @Value("${alipay.requestTimeOut}")
    private String requestTimeoutExpress;
    @Value("${alipay.useSandbox}")
    private Boolean useSandbox;

    private AlipayPlatform alipayPlatform;

    @Override
    public AlipayPaymentResponse requestForPayment(AlipayPaymentRequest request) {
        try {
            return alipayPlatform.requestForPayment(request);
        } catch (PaymentGatewayException e) {
            throw new CustomerException(e.getErrCode(), e.getErrMsg());
        }
    }

    @Override
    public Boolean rsaCheckV1(Map<String, String> param) {
        return alipayPlatform.rsaCheckV1(param);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        AlipayConfig config = new AlipayConfig();
        config.setAppId(appId);
        config.setAlipayPublicKey(alipayPublicKey);
        config.setAppPrivateKey(alipayPrivateKey);
        config.setRequestTimeoutExpress(requestTimeoutExpress);
        config.setNotifyUrl(notifyUrl);
        config.setReturnUrl(returnUrl);
        config.setUseSandbox(useSandbox);
        alipayPlatform = new AlipayPlatform(config);
    }
}
