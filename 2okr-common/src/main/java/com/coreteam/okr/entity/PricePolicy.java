package com.coreteam.okr.entity;

import com.coreteam.okr.constant.PolicyTypeEnum;
import com.coreteam.okr.constant.PricePlocyStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * price_policy
 * @author 
 */
@Data
public class PricePolicy extends BaseEntity {

    private Long id;

    private String policyName;

    private Integer level;

    private Integer minUserAmount;

    private Integer maxUserAmount;

    private Integer lifeMonth;

    private String unit;

    private PolicyTypeEnum policyType;

    private BigDecimal price;

    private PricePlocyStatusEnum policyStatus;

    private Long createdUser;

    private String createdName;

    private static final long serialVersionUID = 1L;

}