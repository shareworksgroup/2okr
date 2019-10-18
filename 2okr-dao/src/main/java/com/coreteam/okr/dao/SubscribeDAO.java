package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.entity.Subscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SubscribeDAO继承基类
 */
public interface SubscribeDAO extends MyBatisBaseDao<Subscribe, Long> {

    Subscribe getActiveSubscription(Long origanizationId);

    Subscribe getLastTerminationSubscription(Long origanizationId);

    Subscribe getLastUnPaidSubscription(Subscribe lastSubscription);

    List<Subscribe> listActiveSubscriptionsPaidType();

    void deleteUnpaidSubscriptionBefore(@Param("organizationId") Long organizationId,@Param("id")  Long id);
}