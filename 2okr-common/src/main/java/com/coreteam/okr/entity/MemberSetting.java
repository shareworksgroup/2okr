package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * member_setting
 * @author 
 */
@Data
public class MemberSetting extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private Long userId;

    private String settingType;

    private String settingKey;

    private String value;

    private String remark;

    private static final long serialVersionUID = 1L;

}