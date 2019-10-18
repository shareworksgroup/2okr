package com.coreteam.okr.entity;

import com.coreteam.okr.constant.SubscrideStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * subscribe
 * @author 
 */
@Data
public class Subscribe extends BaseEntity {

    private Long id;

    private String desc;

    private Long organizationId;

    private Long pricePolicyId;

    private BigDecimal unitPrice;

    private Integer maxUserAmount;

    private Integer reminderUserAmount;

    private Integer lifeMonth;

    private Integer consumerTimes;

    private Date beginDate;

    private Date endDate;

    private Date subscribedDate;

    private BigDecimal totalAmount;

    private BigDecimal reminderAmount;

    private BigDecimal subscribePayableAmount;

    private BigDecimal carryOverPayableAmount;

    private SubscrideStatusEnum subscribeStatus;

    private Long originalSubscribeId;

    private Byte isFree;

    private Long createdUser;

    private String createdName;

    private static final long serialVersionUID = 1L;

}