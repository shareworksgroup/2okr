package com.coreteam.okr.dto.Authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ObjectivePrivilege
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 11:04
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectivePrivilege extends Privilege {
    private Boolean keyResultAddable;
}
