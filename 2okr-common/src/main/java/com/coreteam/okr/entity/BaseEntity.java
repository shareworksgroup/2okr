package com.coreteam.okr.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: BaseEntity
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 10:10
 * @Version 1.0.0
 */
@Data
public abstract class BaseEntity {
    /**
     * created datetime
     */
    private Date createdAt;

    /**
     * last updated datetime
     */
    private Date updatedAt;

    /**
     * deleted datetime
     */
    private Date deletedAt;


    private Byte deleted;

    public void initializeForInsert() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.deletedAt = null;
        this.deleted = (byte) 0;
    }

    public void initializeForUpdate(){
        this.createdAt = null;
        this.updatedAt = new Date();
        this.deletedAt = null;
    }

    public void initializeForDelete(){
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = new Date();
        this.deleted = (byte)1;
    }
}
