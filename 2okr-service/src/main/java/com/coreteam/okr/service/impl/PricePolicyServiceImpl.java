package com.coreteam.okr.service.impl;

import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dao.PricePolicyDAO;
import com.coreteam.okr.dto.subscribe.PricePolicyDTO;
import com.coreteam.okr.entity.PricePolicy;
import com.coreteam.okr.service.PricePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PricePolicyServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 14:56
 * @Version 1.0.0
 */
@Service
public class PricePolicyServiceImpl implements PricePolicyService {

    @Autowired
    private PricePolicyDAO pricePolicyDAO;

    @Override
    public List<PricePolicyDTO> listPricePolicies() {
        List<PricePolicy> list = pricePolicyDAO.listPricePolices();
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return BeanConvertUtils.batchTransform(PricePolicyDTO.class, pricePolicyDAO.listPricePolices());
    }

    @Override
    public PricePolicyDTO getSuitablePricePolicy(Integer size, Integer maintenancePeriod) {
        PricePolicy free=this.pricePolicyDAO.getFreePricePolicy();
        if(free.getMaxUserAmount()>size){
            return BeanConvertUtils.transfrom(PricePolicyDTO.class,free);
        }
        PricePolicy pricePolicy = this.pricePolicyDAO.getSuitablePricePolicy(size, maintenancePeriod);
        if(pricePolicy==null){
            return new PricePolicyDTO();
        }
        return BeanConvertUtils.transfrom(PricePolicyDTO.class,this.pricePolicyDAO.getSuitablePricePolicy(size,maintenancePeriod));
    }
}
