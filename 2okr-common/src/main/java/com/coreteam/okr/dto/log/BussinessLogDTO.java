package com.coreteam.okr.dto.log;

import com.coreteam.okr.constant.BussinessLogEntityEnum;
import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: BussinessLogDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 15:18
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BussinessLogDTO {
    private Long id;

    /**
     * the type of operate
     */
    private BussinessLogOperateTypeEnum operateType;

    /**
     * the operator user id
     */
    private Long operatorId;

    /**
     * description for operate
     */
    private String desc;


    private Long organizationId;

    private Long refEntityId;

    private BussinessLogEntityEnum entityType;

    private Date createdAt;
}
