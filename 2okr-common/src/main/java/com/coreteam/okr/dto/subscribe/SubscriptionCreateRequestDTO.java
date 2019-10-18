package com.coreteam.okr.dto.subscribe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: SubscriptionCreateRequestDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 14:41
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionCreateRequestDTO {
    @NotNull
    @Min(value = 0)
    private Long organizationId;
    @NotNull
    @Min(value = 0)
    private Long pricePolicyId;
    @NotNull
    @Min(value = 0)
    private Integer size;
}
