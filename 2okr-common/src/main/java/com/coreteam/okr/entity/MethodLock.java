package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: KeyResultController
 * @Description * 基于mysql实现分布式方法锁表
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class MethodLock  extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 锁定的方法名
     */
    private String methodName;


    private static final long serialVersionUID = 1L;


}