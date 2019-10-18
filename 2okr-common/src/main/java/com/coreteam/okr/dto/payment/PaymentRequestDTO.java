package com.coreteam.okr.dto.payment;

import com.coreteam.okr.constant.GatewayTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: PaymentRequestDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/28 11:06
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequestDTO {
    @NotNull
    @Min(value = 0)
    private Long subscriptionId;

    @NotNull
    private GatewayTypeEnum gatewayType;
}
