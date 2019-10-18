package com.coreteam.okr.payment.exception;

public class ErrorMsg {
    //Common
    public static final String UNSUPPORTED_PAYMENT_PLATFORM = "unsupported payment platform.";

    //Alipay
    public static final String ALIPAY_UNSUPPORTED_PAYMENT_CHANNEL = "unsupported alipay payment channel.";
    public static final String ALIPAY_WAP_PAY_FAILED = "alipay wap pay failed.";
    public static final String ALIPAY_WEB_PAY_FAILED = "alipay web pay failed.";
    public static final String ALIPAY_REFUND_FAILED = "alipay refund failed.";
    public static final String ALIPAY_NOT_SUPPORT_REFUND = "alipay does not support refund.";
    public static final String ALIPAY_PAYMENT_QUERY_FAILED = "alipay payment query failed";
    public static final String ALIPAY_NOT_SUPPORT_PAYMENT_QUERY = "alipay does not support payment query.";

    public static final String ALIPAY_OUT_TRADE_NO_REQUIRED = "outTradeNo is required.";
    public static final String ALIPAY_OUT_TRADE_NO_LEN = "the length of outTradeNo can't exceed 64.";
    public static final String ALIPAY_TOTAL_AMOUNT_REQUIRED = "totalAmount is required.";
    public static final String ALIPAY_TOTAL_AMOUNT_RANGE = "the range of totalAmount must be 0.01 - 100000000.";
    public static final String ALIPAY_SUBJECT_REQUIRED = "subject is required.";
    public static final String ALIPAY_SUBJECT_LEN = "the length of subject can't exceed 256.";
    public static final String ALIPAY_BODY_LEN = "the length of body can't exceed 128.";

    //Wxpay
    public static final String WXPAY_PLATFORM_MISMATCH = "the platform type does not match wxpay";
    public static final String ALIPAY_NOT_SUPPORT_DOWNLOAD_STATEMENT="alipay does not support download statement";
    //public static final String WXPAY_UNSUPPORTED_PAYMENT_CHANNEL = "PGWX002";
    //public static final String WXPAY_OUT_TRADE_NO_REQUIRED = "PGWX003";
    //public static final String WXPAY_OUT_TRADE_NO_LEN = "PGWX004";

}
