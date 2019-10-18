package com.coreteam.okr.dto.order;

import com.coreteam.okr.constant.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: OrderDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/28 14:35
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
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
}
