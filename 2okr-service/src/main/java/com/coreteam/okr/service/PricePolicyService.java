package com.coreteam.okr.service;

import com.coreteam.okr.dto.subscribe.PricePolicyDTO;

import java.util.List;

/**
 * @ClassName: PricePolicyService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 14:55
 * @Version 1.0.0
 */
public interface PricePolicyService {
    List<PricePolicyDTO> listPricePolicies();

    PricePolicyDTO getSuitablePricePolicy(Integer size, Integer maintenancePeriod);
}
