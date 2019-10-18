package com.coreteam.okr.dto.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ListBillRequestDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 15:52
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListBillRequestDTO {
    private Long organizationId;
    private Integer pageNumber;
    private Integer pageSize;
}
