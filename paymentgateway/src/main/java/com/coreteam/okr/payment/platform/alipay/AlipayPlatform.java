package com.coreteam.okr.payment.platform.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.coreteam.okr.payment.exception.ErrorCode;
import com.coreteam.okr.payment.exception.ErrorMsg;
import com.coreteam.okr.payment.exception.PaymentGatewayException;
import com.coreteam.okr.payment.gateway.GatewayTypeEnum;
import com.coreteam.okr.payment.platform.alipay.payment.*;

import java.util.Map;

public class AlipayPlatform {

    private AlipayWebPayChannel webPayChannel = null;
    private AlipayWapPayChannel wapPayChannel = null;
    private AlipayScanPayChannel scanPayChannel = null;
    private AlipayConfig alipayConfig;


    public AlipayPlatform(AlipayConfig alipayConfig) {
        this.webPayChannel = new AlipayWebPayChannel(alipayConfig);
        this.wapPayChannel = new AlipayWapPayChannel(alipayConfig);
        this.scanPayChannel = new AlipayScanPayChannel(alipayConfig);
        this.alipayConfig = alipayConfig;
    }

    public AlipayPaymentResponse requestForPayment(AlipayPaymentRequest request) throws PaymentGatewayException {
        if (request == null) {
            throw new IllegalArgumentException("the input parameter request can not be null");
        }

        GatewayTypeEnum paymentChannelType = request.getPaymentChannelType();
        switch (paymentChannelType) {
            case ALIPAY_WEB:
                return webPayChannel.requestForPayment(request);
            case ALIPAY_WAP:
                return wapPayChannel.requestForPayment(request);
            case ALIPAY_SCAN:
                return scanPayChannel.requestForPayment(request);
            default:
                throw new PaymentGatewayException(ErrorCode.ALIPAY_UNSUPPORTED_PAYMENT_CHANNEL, ErrorMsg.ALIPAY_UNSUPPORTED_PAYMENT_CHANNEL);
        }
    }


    public Boolean rsaCheckV1(Map<String, String> param) {
        try {
            return AlipaySignature.rsaCheckV1(param, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }


}
