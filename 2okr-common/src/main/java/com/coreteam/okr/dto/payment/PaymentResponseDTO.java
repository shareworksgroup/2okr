package com.coreteam.okr.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: PaymentResponseDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/28 11:06
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponseDTO {
    /**
     * Max length:64
     * Alipay trade number
     */
    private String tradeNo;

    /**
     * Max length:64
     * Merchant order number
     */
    private String outTradeNo;

    /**
     * Range:0.01-100000000
     * Payment amount
     */
    private BigDecimal totalAmount;

    /**
     * The amount merchant received
     */
    private BigDecimal receiptAmount;

    /**
     * Max length:256
     * Order title
     */
    private String subject;

    /**
     * Max length:128
     * Description
     */
    private String body;

    /**
     * Response data
     * Web pay and Wap pay will return a html form
     */
    private String htmlForm;
}
