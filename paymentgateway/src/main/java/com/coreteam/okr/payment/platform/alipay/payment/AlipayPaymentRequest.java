package com.coreteam.okr.payment.platform.alipay.payment;


import com.coreteam.okr.payment.gateway.GatewayTypeEnum;

import java.math.BigDecimal;

public class AlipayPaymentRequest implements PaymentRequest{
    /**
     * Required
     */
    private GatewayTypeEnum paymentChannelType;

    /**
     * Max length:64
     * Merchant order number
     * Required
     */
    private String outTradeNo;

    /**
     * Range:0.01-100000000
     * Payment amount
     * Required
     */
    private BigDecimal totalAmount;

    /**
     * Max length:256
     * Order title
     * Required
     */
    private String subject;

    /**
     * Max length:128
     * Description
     * Optional
     */
    private String body;

    @Override
    public GatewayTypeEnum getPaymentChannelType() {
        return paymentChannelType;
    }

    public void setPaymentChannelType(GatewayTypeEnum paymentChannelType) {
        this.paymentChannelType = paymentChannelType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
