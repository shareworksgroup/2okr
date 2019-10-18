package com.coreteam.okr.dto.organization;

import com.coreteam.okr.dto.Authorization.OrganizationPrivilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: OrganizationDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 14:39
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationDTO {
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

    /**
     * 是否为默认的组织
     */
    private Boolean isDefault;

    private OrganizationPrivilege privilege;
}
