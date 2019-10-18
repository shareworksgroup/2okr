package com.coreteam.okr.dto.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: BillCreateRequestDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 15:52
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillCreateRequestDTO {

    private Long organizationId;

    private Long subscribeId;

    private String desc;

    private Date billingTime;

    private BigDecimal amount;

}
