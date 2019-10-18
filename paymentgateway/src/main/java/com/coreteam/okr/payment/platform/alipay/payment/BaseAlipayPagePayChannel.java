package com.coreteam.okr.payment.platform.alipay.payment;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.coreteam.okr.payment.exception.ErrorCode;
import com.coreteam.okr.payment.exception.ErrorMsg;
import com.coreteam.okr.payment.exception.PaymentGatewayException;
import com.coreteam.okr.payment.gateway.GatewayTypeEnum;
import com.coreteam.okr.payment.platform.alipay.AbstractAlipay;
import com.coreteam.okr.payment.platform.alipay.AlipayConfig;
import com.coreteam.okr.payment.platform.alipay.AlipayDataValidator;

public abstract class BaseAlipayPagePayChannel extends AbstractAlipay {

    public BaseAlipayPagePayChannel(AlipayConfig alipayConfig) {
        super(alipayConfig);
    }

    public AlipayPaymentResponse requestForPayment(AlipayPaymentRequest request) throws PaymentGatewayException {
        validateRequest(request);
        AlipayRequest payRequest = createAlipayRequest();
        AlipayObject payModel = createAlipayTradePayModel(request);
        payRequest.setBizModel(payModel);
        payRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        payRequest.setReturnUrl(alipayConfig.getReturnUrl());
        String htmlForm = "";
        try {
            switch (getPaymentChannelType()) {
                case ALIPAY_WAP:
                case ALIPAY_WEB:
                    AlipayResponse response = alipayClient.pageExecute(payRequest);
                    htmlForm = response.getBody();
                    break;
                case ALIPAY_SCAN:
                    htmlForm = alipayClient.execute(payRequest).getBody();
                    break;
            }

        } catch (AlipayApiException e) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_WAP_PAY_FAILED,
                    ErrorMsg.ALIPAY_WAP_PAY_FAILED, e);
        }

        return createAlipayPaymentResponse(request, htmlForm);
    }

    protected void validateRequest(AlipayPaymentRequest request) throws PaymentGatewayException {
        AlipayDataValidator.validateOutTradeNo(request.getOutTradeNo());
        AlipayDataValidator.validateTotalAmount(request.getTotalAmount());
        AlipayDataValidator.validateSubject(request.getSubject());
        AlipayDataValidator.validateBody(request.getBody());
    }

    protected abstract AlipayRequest createAlipayRequest();

    protected abstract AlipayObject createAlipayTradePayModel(AlipayPaymentRequest request);

    public abstract GatewayTypeEnum getPaymentChannelType();

    protected AlipayPaymentResponse createAlipayPaymentResponse(AlipayPaymentRequest request, String htmlForm) {
        AlipayPaymentResponse response = new AlipayPaymentResponse();
        response.setSubject(request.getSubject());
        response.setBody(request.getBody());
        response.setOutTradeNo(request.getOutTradeNo());
        response.setTotalAmount(request.getTotalAmount());
        response.setHtmlForm(htmlForm);
        response.setPaymentChannelType(getPaymentChannelType());
        return response;
    }
}
