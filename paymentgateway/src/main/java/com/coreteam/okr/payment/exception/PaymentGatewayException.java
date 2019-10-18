package com.coreteam.okr.payment.exception;

public class PaymentGatewayException extends Exception {
    private String errCode;
    private String errMsg;

    public PaymentGatewayException(String errCode, String errMsg, String detailMsg) {
        super(errCode + ":" + errMsg + detailMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public PaymentGatewayException(String errCode, String errMsg, Throwable cause) {
        super(errCode + ":" + errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public PaymentGatewayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
