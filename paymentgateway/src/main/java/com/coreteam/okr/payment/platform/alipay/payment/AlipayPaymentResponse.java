package com.coreteam.okr.payment.platform.alipay.payment;


import com.coreteam.okr.payment.gateway.GatewayTypeEnum;

import java.math.BigDecimal;

public class AlipayPaymentResponse {

    private GatewayTypeEnum paymentChannelType;

    /**
     * Max length:64
     * Alipay trade number
     */
    private String tradeNo;

    /**
     * Max length:64
     * Merchant order number
     */
    private String outTradeNo;

    /**
     * Range:0.01-100000000
     * Payment amount
     */
    private BigDecimal totalAmount;

    /**
     * The amount merchant received
     */
    private BigDecimal receiptAmount;

    /**
     * Max length:256
     * Order title
     */
    private String subject;

    /**
     * Max length:128
     * Description
     */
    private String body;

    /**
     * Response data
     * Web pay and Wap pay will return a html form
     */
    private String htmlForm;

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

    public String getHtmlForm() {
        return htmlForm;
    }

    public void setHtmlForm(String htmlForm) {
        this.htmlForm = htmlForm;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }
}
