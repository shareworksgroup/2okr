package com.coreteam.okr.payment.platform.alipay;

/**
 * @ClassName: AlipayConfig
 * @Description ali pay 配置
 * @Author sean.deng
 * @Date 2019/08/29 9:44
 * @Version 1.0.0
 */
public class AlipayConfig {
    private String charset = AlipayConfigConstants.CHARSET_UTF8;
    private String format = AlipayConfigConstants.FORMAT_JSON;
    private String signType = AlipayConfigConstants.SIGNTYPE_RSA2;
    private String notifyUrl;
    private String returnUrl;
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String requestTimeoutExpress;
    private boolean useSandbox = false;

    public String getUrl() {
        return useSandbox ? AlipayConfigConstants.DEV_URL : AlipayConfigConstants.URL;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getRequestTimeoutExpress() {
        return requestTimeoutExpress;
    }

    public void setRequestTimeoutExpress(String requestTimeoutExpress) {
        this.requestTimeoutExpress = requestTimeoutExpress;
    }

    public boolean isUseSandbox() {
        return useSandbox;
    }

    public void setUseSandbox(boolean useSandbox) {
        this.useSandbox = useSandbox;
    }
}
