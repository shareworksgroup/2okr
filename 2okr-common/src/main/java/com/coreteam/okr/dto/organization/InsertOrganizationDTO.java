package com.coreteam.okr.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: InsertOrganizationDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 14:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertOrganizationDTO {

    /**
     * the name of organization
     */
    @NotNull
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
     * 邀请成员
     */
    private List<String> invited;
}
