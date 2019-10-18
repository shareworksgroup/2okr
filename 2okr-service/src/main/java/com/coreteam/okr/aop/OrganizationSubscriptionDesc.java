package com.coreteam.okr.aop;

import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import lombok.Data;

/**
 * @ClassName: OrganizationSubscriptionDesc
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/09 15:07
 * @Version 1.0.0
 */
@Data
public class OrganizationSubscriptionDesc {
    /**
     * primary key
     */
    private Long id;

    /**
     * the name of organization
     */
    private String name;

    /**
     * the email of organization
     */
    private String email;


    private SubscriptionDTO subscribe;

    private Long expiredTime;
}
