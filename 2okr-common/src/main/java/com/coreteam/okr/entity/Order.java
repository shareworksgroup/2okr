package com.coreteam.okr.entity;

import com.coreteam.okr.constant.OrderStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * order
 *
 * @author
 */
@Data
public class Order extends BaseEntity {
    /**
     * primary key
     */
    private Long id;

    private Long organizationId;

    private Long subscribeId;

    private Long orderNo;

    private BigDecimal orderAmount;

    private OrderStatusEnum orderStatus;

    private Date orderDate;

    private Date paymentAt;

    private Long createdUser;

    private String createdName;


    private static final long serialVersionUID = 1L;

}