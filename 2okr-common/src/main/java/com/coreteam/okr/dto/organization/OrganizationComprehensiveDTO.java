package com.coreteam.okr.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: OrganizationComprehensiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 8:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationComprehensiveDTO {

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

    private String owner;


    private Integer okrNum;

    private Integer memberNum;



}
