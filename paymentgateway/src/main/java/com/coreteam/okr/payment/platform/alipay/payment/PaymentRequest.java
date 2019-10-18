package com.coreteam.okr.payment.platform.alipay.payment;


import com.coreteam.okr.payment.gateway.GatewayTypeEnum;

public interface PaymentRequest {

    GatewayTypeEnum getPaymentChannelType();

}