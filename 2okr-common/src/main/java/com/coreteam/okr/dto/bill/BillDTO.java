package com.coreteam.okr.dto.bill;

import com.coreteam.okr.constant.BillStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: BillDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 15:51
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillDTO {

    private Long id;

    private Long organizationId;

    private Long subscribeId;

    private String desc;

    private Date billingTime;

    private BigDecimal amount;

    private BillStatusEnum billStatus;

    private Long createdUser;

    private String createdName;

}
