package com.coreteam.okr.payment.exception;

/**
 * Format: PG + Category + Number
 * Category: Two characters
 * Category:
 *     CN - COMMON
 *     AL - Alipay
 *     WX - Wxpay
 *     UN - Unionpay
 *     BL - 99Bill
 * Number: Three digits
 * Example: PGCN001
 */
public class ErrorCode {
    //Common
    public static final String UNSUPPORTED_PAYMENT_PLATFORM = "PGCN001";

    //Alipay
    public static final String ALIPAY_UNSUPPORTED_PAYMENT_CHANNEL = "PGAL001";
    public static final String ALIPAY_WAP_PAY_FAILED = "PGAL002";
    public static final String ALIPAY_WEB_PAY_FAILED = "PGAL003";
    public static final String ALIPAY_REFUND_FAILED = "PGAL004";

    public static final String ALIPAY_OUT_TRADE_NO_REQUIRED = "PGAL101";
    public static final String ALIPAY_OUT_TRADE_NO_LEN = "PGAL102";
    public static final String ALIPAY_TOTAL_AMOUNT_REQUIRED = "PGAL103";
    public static final String ALIPAY_TOTAL_AMOUNT_RANGE = "PGAL104";
    public static final String ALIPAY_SUBJECT_REQUIRED = "PGAL105";
    public static final String ALIPAY_SUBJECT_LEN = "PGAL106";
    public static final String ALIPAY_BODY_LEN = "PGAL107";

    //Wxpay
    public static final String WXPAY_PLATFORM_MISMATCH = "PGWX001";
    public static final String WXPAY_UNSUPPORTED_PAYMENT_CHANNEL = "PGWX002";
    public static final String WXPAY_OUT_TRADE_NO_REQUIRED = "PGWX003";
    public static final String WXPAY_OUT_TRADE_NO_LEN = "PGWX004";

}
