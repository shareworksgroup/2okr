package com.coreteam.okr.payment.platform.alipay.payment;

import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.coreteam.okr.payment.gateway.GatewayTypeEnum;
import com.coreteam.okr.payment.platform.alipay.AlipayConfig;

public class AlipayWapPayChannel extends BaseAlipayPagePayChannel {

    private static final String PRODUCT_CODE = "QUICK_WAP_WAY";

    public AlipayWapPayChannel(AlipayConfig alipayConfig) {
        super(alipayConfig);
    }

    @Override
    protected AlipayRequest createAlipayRequest() {
        return new AlipayTradeWapPayRequest();
    }

    @Override
    protected AlipayObject createAlipayTradePayModel(AlipayPaymentRequest request) {
        AlipayTradeWapPayModel payModel = new AlipayTradeWapPayModel();
        payModel.setOutTradeNo(request.getOutTradeNo());
        payModel.setTotalAmount(request.getTotalAmount().toString());
        payModel.setSubject(request.getSubject());
        payModel.setBody(request.getBody());
        payModel.setProductCode(PRODUCT_CODE);
        return payModel;
    }

    @Override
    public GatewayTypeEnum getPaymentChannelType() {
        return GatewayTypeEnum.ALIPAY_WAP;
    }
}
