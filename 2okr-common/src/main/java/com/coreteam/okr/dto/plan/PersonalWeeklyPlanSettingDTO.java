package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: PersonalWeeklyPlanSettingDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/08 10:30
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalWeeklyPlanSettingDTO {
    @NotBlank
    private String title;
}
