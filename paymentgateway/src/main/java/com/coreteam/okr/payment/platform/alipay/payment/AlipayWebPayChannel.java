package com.coreteam.okr.payment.platform.alipay.payment;

import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.coreteam.okr.payment.gateway.GatewayTypeEnum;
import com.coreteam.okr.payment.platform.alipay.AlipayConfig;

public class AlipayWebPayChannel extends BaseAlipayPagePayChannel {
	
	private static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";
  
    public AlipayWebPayChannel(AlipayConfig alipayConfig) {
        super(alipayConfig);
    }

    @Override
    protected  AlipayTradePagePayModel createAlipayTradePayModel(AlipayPaymentRequest request) {
		AlipayTradePagePayModel payModel = new AlipayTradePagePayModel();
        payModel.setOutTradeNo(request.getOutTradeNo());
        payModel.setTotalAmount(request.getTotalAmount().toString());
        payModel.setSubject(request.getSubject());
        payModel.setBody(request.getBody());
        payModel.setProductCode(PRODUCT_CODE);
		return payModel;
	}
    
    @Override
    protected  AlipayRequest createAlipayRequest() {
		AlipayRequest payRequest = new AlipayTradePagePayRequest();
		return payRequest;
	}

    @Override
    public GatewayTypeEnum getPaymentChannelType() {
        return GatewayTypeEnum.ALIPAY_WEB;
    }
}
