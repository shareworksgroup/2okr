package com.coreteam.okr.aop;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.SubscrideStatusEnum;
import com.coreteam.okr.dao.OrganizationDAO;
import com.coreteam.okr.dao.SubscribeDAO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.entity.Subscribe;
import com.coreteam.okr.service.OrganizationService;
import com.coreteam.okr.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: OrganizationSubscriptionManager
 * @Description 对组织的订阅校验和管理
 * @Author sean.deng
 * @Date 2019/09/09 15:28
 * @Version 1.0.0
 */
@Component
public class OrganizationSubscriptionManager {

    @Value("${organization.subscription.expired:600000}")
    private String expiredTime;

    @Value("${organization.subscription.check}")
    private Boolean check=false;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SubscribeService subscribeService;

    private Map<Long, OrganizationSubscriptionDesc> cacheMap = new HashMap<Long, OrganizationSubscriptionDesc>();

    public void checkActived(Long organizationId) {
        if(!check){
            return;
        }
        Long now=System.currentTimeMillis();
        OrganizationSubscriptionDesc organizationSubscriptionDesc = cacheMap.get(organizationId);
        if(organizationSubscriptionDesc==null||organizationSubscriptionDesc.getExpiredTime()<now){
            //查询数据库，并且添加到map列表
            OrganizationDTO organization = organizationService.getOrganizationById(organizationId);
            if(organization==null||organization.getId()==null){
                throw new CustomerException("organization not exsist");
            }
            SubscriptionDTO subscriptionDTO=subscribeService.getActiveSubscription(organization.getId());
            OrganizationSubscriptionDesc desc=new OrganizationSubscriptionDesc();
            BeanConvertUtils.copyEntityProperties(subscriptionDTO,desc);
            desc.setSubscribe(subscriptionDTO);
            desc.setExpiredTime(now+Long.valueOf(expiredTime));
            cacheMap.put(subscriptionDTO.getOrganizationId(),desc);
            organizationSubscriptionDesc=desc;
        }
        if(organizationSubscriptionDesc.getSubscribe()==null||
                organizationSubscriptionDesc.getSubscribe().getSubscribeStatus()!= SubscrideStatusEnum.ACTIVE){
            throw new CustomerException(" the organization has not subscription,please create subscription in setting");
        }



    }


}
