package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.PricePolicy;

import java.util.List;

/**
 * PricePolicyDAO继承基类
 */
public interface PricePolicyDAO extends MyBatisBaseDao<PricePolicy, Long> {

    PricePolicy getFreePricePolicy();

    List<PricePolicy> listPricePolices();

    PricePolicy getSuitablePricePolicy(Integer size, Integer maintenancePeriod);
}