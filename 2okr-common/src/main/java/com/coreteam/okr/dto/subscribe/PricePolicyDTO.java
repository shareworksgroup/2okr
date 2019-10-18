package com.coreteam.okr.dto.subscribe;

import com.coreteam.okr.constant.PolicyTypeEnum;
import com.coreteam.okr.constant.PricePlocyStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: PricePolicyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 14:44
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricePolicyDTO {
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
}
