package com.coreteam.okr.web.controller;

import com.coreteam.okr.dto.subscribe.PricePolicyDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionCreateRequestDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.service.PricePolicyService;
import com.coreteam.okr.service.SubscribeService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: SubscribeController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 13:43
 * @Version 1.0.0
 */

@RestController
@RequestMapping("subscribe")
@AuditLogAnnotation(value = "subscribe接口")
@Slf4j
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private PricePolicyService pricePolicyService;

    @GetMapping("/price_policy")
    @ApiOperation("获取所有的价格套餐信息")
    public List<PricePolicyDTO> listPricePolicies() {
        return pricePolicyService.listPricePolicies();
    }

    @GetMapping("/price_policy/suitable")
    @ApiOperation("根据人数获取合适的价格套餐信息")
    public PricePolicyDTO getSuitablePricePolicy(@RequestParam(required = true) Integer size,@RequestParam(required = false)Integer maintenancePeriod) {
        if(maintenancePeriod==null){
            maintenancePeriod=3;
        }
        return pricePolicyService.getSuitablePricePolicy(size,maintenancePeriod);
    }

    @PostMapping
    @ApiOperation("订阅服务")
    public void createSubscription(@RequestBody @Valid SubscriptionCreateRequestDTO request) {
        subscribeService.createSubscription(request);
    }

    @GetMapping("/unpaid")
    @ApiOperation("获取最新的需要待支付的订阅")
    public SubscriptionDTO getUnpaidSubscription(Long organizationId) {
        return subscribeService.getLastUnPaidSubscription(organizationId);

    }

    @GetMapping("/active")
    @ApiOperation("获取正在使用的订阅服务")
    public SubscriptionDTO getActiveSubscription(Long organizationId) {
        return subscribeService.getActiveSubscription(organizationId);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除未支付的订阅服务")
    public void deleteUnpaidSubscription(@PathVariable("id") @NotNull Long id){
        this.subscribeService.deleteSubscription(id);
    }

}
