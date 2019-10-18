package com.coreteam.okr.entity;

import com.coreteam.okr.constant.PlanLeveEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class Plan  extends BaseEntity implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 描述
     */
    private String desc;

    /**
     * level，org，team，member
     */
    private PlanLeveEnum level;

    /**
     * org id
     */
    private Long organizationId;

    /**
     * team id
     */
    private Long teamId;

    /**
     * owner id
     */
    private Long ownerId;

    /**
     * owner name
     */
    private String ownerName;

    /**
     * 是否私有
     */
    private Byte isPrivated;

    /**
     * 截止时间
     */
    private Date dueDate;


    private static final long serialVersionUID = 1L;

    public Plan() {
    }

    public Plan(String desc, PlanLeveEnum level, Long ownerId, String ownerName, Byte isPrivated, Date dueDate) {
        this.desc = desc;
        this.level = level;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.isPrivated = isPrivated;
        this.dueDate = dueDate;
    }
}