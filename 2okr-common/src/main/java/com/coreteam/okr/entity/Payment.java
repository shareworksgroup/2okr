package com.coreteam.okr.entity;

import com.coreteam.okr.constant.GatewayTypeEnum;
import com.coreteam.okr.constant.PaymentStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * payment
 * @author 
 */
@Data
public class Payment extends BaseEntity{
    /**
     * primary key
     */
    private Long id;

    private Long tradeNo;

    private String channelTradeNo;

    private Long orderNo;

    private BigDecimal orderAmount;

    private BigDecimal userPayAmount;

    private BigDecimal receiptAmount;

    private GatewayTypeEnum gatewayType;

    private PaymentStatusEnum paymentStatus;

    private Date paymentAt;

    private Long createdUser;

    private String createdName;


    private static final long serialVersionUID = 1L;

}