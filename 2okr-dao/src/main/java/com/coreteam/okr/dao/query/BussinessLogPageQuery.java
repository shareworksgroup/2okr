package com.coreteam.okr.dao.query;

import com.coreteam.core.dto.PageBaseDTO;
import com.coreteam.okr.constant.BussinessLogEntityEnum;
import lombok.Data;

/**
 * @ClassName: BussinessLogPageQuery
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 17:24
 * @Version 1.0.0
 */
@Data
public class BussinessLogPageQuery extends PageBaseDTO {
    private BussinessLogEntityEnum entityType;
    private Long refEntityId;
}
