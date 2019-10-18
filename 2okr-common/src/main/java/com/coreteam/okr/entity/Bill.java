package com.coreteam.okr.entity;

import com.coreteam.okr.constant.BillStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Bill extends BaseEntity {

    private Long id;

    private Long organizationId;

    private Long subscribeId;

    private String desc;

    private Date billingTime;

    private BigDecimal amount;

    private BillStatusEnum billStatus;

    private Long createdUser;

    private String createdName;

    private static final long serialVersionUID = 1L;

}