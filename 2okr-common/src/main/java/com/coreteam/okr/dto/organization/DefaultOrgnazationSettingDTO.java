package com.coreteam.okr.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: DefaultOrgnazationSettingDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/21 17:01
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultOrgnazationSettingDTO {
    @NotNull
    private Long organizationId;

}
