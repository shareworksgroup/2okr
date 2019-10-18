package com.coreteam.okr.payment.platform.alipay;

import com.coreteam.okr.payment.exception.ErrorCode;
import com.coreteam.okr.payment.exception.ErrorMsg;
import com.coreteam.okr.payment.exception.PaymentGatewayException;

import java.math.BigDecimal;

public class AlipayDataValidator {
    private static final int OUT_TRADE_NO_MAX_LEN = 64;
    private static final double MIN_TOTAL_AMOUNT = 0.01;
    private static final double MAX_TOTAL_AMOUNT = 100000000;
    private static final int SUBJECT_MAX_LEN = 256;
    private static final int BODY_MAX_LEN = 128;

    public static void validateOutTradeNo(String outTradeNo) throws PaymentGatewayException {
        if(outTradeNo == null || outTradeNo.isEmpty()) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_OUT_TRADE_NO_REQUIRED,
                    ErrorMsg.ALIPAY_OUT_TRADE_NO_REQUIRED);
        }

        if(outTradeNo.length() > OUT_TRADE_NO_MAX_LEN) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_OUT_TRADE_NO_LEN,
                    ErrorMsg.ALIPAY_OUT_TRADE_NO_LEN);
        }
    }

    public static void validateTotalAmount(BigDecimal totalAmount) throws PaymentGatewayException {
        if(totalAmount == null) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_TOTAL_AMOUNT_REQUIRED,
                    ErrorMsg.ALIPAY_TOTAL_AMOUNT_REQUIRED);
        }

        if(totalAmount.compareTo(BigDecimal.valueOf(MIN_TOTAL_AMOUNT)) < 0
                || totalAmount.compareTo(BigDecimal.valueOf(MAX_TOTAL_AMOUNT)) > 0) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_TOTAL_AMOUNT_RANGE,
                    ErrorMsg.ALIPAY_TOTAL_AMOUNT_RANGE);
        }
    }

    public static void validateSubject(String subject) throws PaymentGatewayException {
        if(subject == null || subject.isEmpty()) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_SUBJECT_REQUIRED,
                    ErrorMsg.ALIPAY_SUBJECT_REQUIRED);
        }

        if(subject.length() > SUBJECT_MAX_LEN) {
            throw new PaymentGatewayException(ErrorCode.ALIPAY_SUBJECT_LEN,
                    ErrorMsg.ALIPAY_SUBJECT_LEN);
        }
    }

    public static void validateBody(String body) throws PaymentGatewayException {
        if(body != null) {
            if(body.length() > BODY_MAX_LEN) {
                throw new PaymentGatewayException(ErrorCode.ALIPAY_BODY_LEN,
                        ErrorMsg.ALIPAY_BODY_LEN);
            }
        }
    }
}
