package com.coreteam.okr.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateOrganizationDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 14:38
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateOrganizationDTO {

    @NotNull
    private Long id;

    /**
     * the name of organization
     */
    private String name;

    /**
     * the email of organization
     */
    private String email;

    /**
     * organization description
     */
    private String desc;

    /**
     * organization show color
     */
    private String color;
}
