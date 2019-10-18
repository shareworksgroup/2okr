package com.coreteam.okr.dto.subscribe;

import com.coreteam.okr.constant.SubscrideStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: SubscriptionDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 14:44
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionDTO {
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

    private Long createdUser;

    private String createdName;

    private PricePolicyDTO pricePolicy;
}
