package com.coreteam.okr.dto.log;

import com.coreteam.core.dto.PageBaseDTO;
import com.coreteam.okr.constant.BussinessLogEntityEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @ClassName: GetPagedBussinessLogDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 14:35
 * @Version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPagedBussinessLogDTO extends PageBaseDTO {
    public BussinessLogEntityEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(BussinessLogEntityEnum entityType) {
        this.entityType = entityType;
    }

    public Long getRefEntityId() {
        return refEntityId;
    }

    public void setRefEntityId(Long refEntityId) {
        this.refEntityId = refEntityId;
    }

    private BussinessLogEntityEnum entityType;
    private Long refEntityId;
}
