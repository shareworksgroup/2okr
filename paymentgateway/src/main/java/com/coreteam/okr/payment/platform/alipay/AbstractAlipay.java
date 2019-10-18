package com.coreteam.okr.payment.platform.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * Created by liang on 08/08/2017.
 */
public abstract class AbstractAlipay {
    protected AlipayConfig alipayConfig;
    protected AlipayClient alipayClient;

    public AbstractAlipay(AlipayConfig alipayConfig) {
        if(alipayConfig == null) {
            throw new IllegalArgumentException("the input parameter alipayConfig can not be null");
        }

        this.alipayConfig = alipayConfig;
        this.alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(),
                alipayConfig.getAppId(), alipayConfig.getAppPrivateKey(), alipayConfig.getFormat(),
                alipayConfig.getCharset(), alipayConfig.getAlipayPublicKey(), alipayConfig.getSignType());
    }
}
