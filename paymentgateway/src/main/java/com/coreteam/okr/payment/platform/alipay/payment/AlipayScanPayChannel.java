package com.coreteam.okr.payment.platform.alipay.payment;

import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.coreteam.okr.payment.gateway.GatewayTypeEnum;
import com.coreteam.okr.payment.platform.alipay.AlipayConfig;

/**
 * @ClassName: AlipayScanPayChannel
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/30 14:37
 * @Version 1.0.0
 */
public class AlipayScanPayChannel extends BaseAlipayPagePayChannel  {


    public AlipayScanPayChannel(AlipayConfig alipayConfig) {
        super(alipayConfig);
    }

    @Override
    protected AlipayRequest createAlipayRequest() {
        return new AlipayTradePrecreateRequest();
    }

    @Override
    protected AlipayObject createAlipayTradePayModel(AlipayPaymentRequest request) {
        AlipayTradePagePayModel payModel = new AlipayTradePagePayModel();
        payModel.setOutTradeNo(request.getOutTradeNo());
        payModel.setTotalAmount(request.getTotalAmount().toString());
        payModel.setSubject(request.getSubject());
        payModel.setBody(request.getBody());
        return payModel;
    }

    @Override
    public GatewayTypeEnum getPaymentChannelType() {
        return GatewayTypeEnum.ALIPAY_SCAN;
    }
}
