package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.common.util.DateUtil;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.PolicyTypeEnum;
import com.coreteam.okr.constant.SubscrideStatusEnum;
import com.coreteam.okr.dao.MemberDAO;
import com.coreteam.okr.dao.PricePolicyDAO;
import com.coreteam.okr.dao.SubscribeDAO;
import com.coreteam.okr.dto.bill.BillCreateRequestDTO;
import com.coreteam.okr.dto.subscribe.PricePolicyDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionCreateRequestDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.PricePolicy;
import com.coreteam.okr.entity.Subscribe;
import com.coreteam.okr.service.BillService;
import com.coreteam.okr.service.SubscribeService;
import com.coreteam.okr.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SubscribeServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 11:49
 * @Version 1.0.0
 */
@Service
@Slf4j
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private SubscribeDAO subscribeDAO;
    @Autowired
    private PricePolicyDAO pricePolicyDAO;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private BillService billService;

    @Override
    @Transactional
    public void createSubscription(SubscriptionCreateRequestDTO requestDTO) {
        Subscribe subscribe = subscribeDAO.getActiveSubscription(requestDTO.getOrganizationId());
        if (subscribe != null) {
            upgradeSubscription(subscribe, requestDTO);
        } else {
            createNewSubscription(requestDTO);
        }
    }

    public void upgradeSubscription(Subscribe old, SubscriptionCreateRequestDTO requestDTO) {
        SubscriptionDTO dto = BeanConvertUtils.transfrom(SubscriptionDTO.class, old);
        combinePricePolicy(dto);
        checkUpgradePricePolicyChose(dto, requestDTO);
        Subscribe newSubscribe = buildSubscribe(requestDTO);
        newSubscribe.setOriginalSubscribeId(old.getId());
        newSubscribe.setCarryOverPayableAmount(old.getReminderAmount());
        newSubscribe.setSubscribePayableAmount(newSubscribe.getTotalAmount().subtract(newSubscribe.getCarryOverPayableAmount()));
        subscribeDAO.insertSelective(newSubscribe);

    }


    public void createNewSubscription(SubscriptionCreateRequestDTO requestDTO) {
        checkNewPricePolicyChose(requestDTO);
        Subscribe subscribe = buildSubscribe(requestDTO);
        subscribe.setSubscribePayableAmount(subscribe.getTotalAmount());
        subscribe.setCarryOverPayableAmount(new BigDecimal(0.00));
        subscribeDAO.insertSelective(subscribe);
    }


    @Override
    @Transactional
    public SubscriptionDTO getActiveSubscription(Long organizationId) {
        Subscribe subscribe = subscribeDAO.getActiveSubscription(organizationId);
        if (subscribe == null) {
            synchronized (this){
                subscribe = subscribeDAO.getActiveSubscription(organizationId);
                if(subscribe==null){
                    if (subscribeDAO.getLastTerminationSubscription(organizationId) == null) {
                        subscribe = initFreeSubscription(organizationId);
                        subscribeDAO.insertSelective(subscribe);
                    }
                }
            }

        }
        if (subscribe == null) {
            return new SubscriptionDTO();
        }
        SubscriptionDTO subscribeDTO = new SubscriptionDTO();
        BeanUtils.copyProperties(subscribe, subscribeDTO);
        combinePricePolicy(subscribeDTO);
        return subscribeDTO;
    }


    @Override
    @Transactional
    public SubscriptionDTO getLastUnPaidSubscription(Long organizationId) {
        Subscribe lastSubscribe = subscribeDAO.getActiveSubscription(organizationId);
        if (lastSubscribe == null) {
            lastSubscribe = subscribeDAO.getLastTerminationSubscription(organizationId);
        }
        if (lastSubscribe == null) {
            lastSubscribe = new Subscribe();
            lastSubscribe.setOrganizationId(organizationId);
        }
        Subscribe subscribe = subscribeDAO.getLastUnPaidSubscription(lastSubscribe);
        if (subscribe == null) {
            return new SubscriptionDTO();
        } else {
            updateCarrayAndSunscribeAmount(subscribe);
            SubscriptionDTO dto = BeanConvertUtils.transfrom(SubscriptionDTO.class, subscribe);
            combinePricePolicy(dto);
            return dto;
        }
    }

    @Override
    public Subscribe getSubscriptionForOrder(Long subscriptionId) {
        Subscribe subscribeNeedPay = this.subscribeDAO.selectByPrimaryKey(subscriptionId);
        Subscribe subscribeLastUnpaid = BeanConvertUtils.transfrom(Subscribe.class, getLastUnPaidSubscription(subscribeNeedPay.getOrganizationId()));
        checkSubscriptionForOrder(subscribeNeedPay, subscribeLastUnpaid);
        subscribeNeedPay = subscribeLastUnpaid;
        return subscribeNeedPay;
    }

    @Override
    public void activeSubscription(Long id) {
        //todo 1.设置开始时间和结束时间，2 更新当前订阅剩余的人员数量 3,将当前的状态设置为active，4，如果存在结转的将结转的设置为终止
        Subscribe subscribe = subscribeDAO.selectByPrimaryKey(id);
        Date now = new Date();
        subscribe.setBeginDate(DateUtil.getFirstDayOfMonth(now));
        subscribe.setEndDate(DateUtil.getLastDayOfMonth(DateUtil.getDayAfterFewMonth(now, subscribe.getLifeMonth() - 1)));
        subscribe.setReminderAmount(subscribe.getTotalAmount());
        updateReminderUserAmount(subscribe, subscribe.getOrganizationId());
        subscribe.setSubscribeStatus(SubscrideStatusEnum.ACTIVE);
        this.subscribeDAO.updateByPrimaryKeySelective(subscribe);
        if(subscribe.getOriginalSubscribeId()!=null){
            Subscribe originalSubscription = subscribeDAO.selectByPrimaryKey(subscribe.getOriginalSubscribeId());
            if(originalSubscription!=null){
                originalSubscription.setSubscribeStatus(SubscrideStatusEnum.TERMINATION);
                originalSubscription.initializeForUpdate();
                this.subscribeDAO.updateByPrimaryKeySelective(originalSubscription);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUnpaidSubscriptionBefore(Long id) {
        Subscribe subscribe = this.subscribeDAO.selectByPrimaryKey(id);
        this.subscribeDAO.deleteUnpaidSubscriptionBefore(subscribe.getOrganizationId(), subscribe.getId());
    }

    @Override
    public void deleteSubscription(Long id) {
        Subscribe subscribe = this.subscribeDAO.selectByPrimaryKey(id);
        if (subscribe == null || !subscribe.getSubscribeStatus().equals(SubscrideStatusEnum.TO_BE_PAID)) {
            throw new CustomerException("illegal subscription status");
        }
        this.subscribeDAO.deleteByPrimaryKey(id);

    }

    @Override
    public void consumerSubscriptionPerMonthAtLastDay() {
        List<Subscribe> list = subscribeDAO.listActiveSubscriptionsPaidType();
        list.forEach(subscribe -> {
            try {
                consumerSubscriptionPerMonth(subscribe);
                subscribe.initializeForUpdate();
                this.subscribeDAO.updateByPrimaryKeySelective(subscribe);
            } catch (Exception e) {
                log.error(ExceptionUtil.stackTraceToString(e));
            }
        });
    }

    @Override
    public void updateSubscriptionStatus() {
        Date now = new Date();
        List<Subscribe> list = subscribeDAO.listActiveSubscriptionsPaidType();
        list.forEach(subscribe -> {
            try {
                if (subscribe.getEndDate().before(now)) {
                    subscribe.setSubscribeStatus(SubscrideStatusEnum.TERMINATION);
                    subscribe.initializeForUpdate();
                    this.subscribeDAO.updateByPrimaryKeySelective(subscribe);
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.stackTraceToString(e));
            }
        });
    }



    public void consumerSubscriptionPerMonth(Subscribe subscribe) {
        if (subscribe.getConsumerTimes() < subscribe.getLifeMonth()) {
            BigDecimal consumerAmount = null;
            BigDecimal consumerPerMonth = subscribe.getTotalAmount().divide(new BigDecimal(subscribe.getLifeMonth()), 2, BigDecimal.ROUND_HALF_DOWN);
            if (subscribe.getConsumerTimes() == 0) {
                consumerAmount = subscribe.getTotalAmount().subtract(consumerPerMonth.multiply(new BigDecimal(subscribe.getLifeMonth() - 1)));
                subscribe.setConsumerTimes(1);
                subscribe.setReminderAmount(subscribe.getReminderAmount().subtract(consumerAmount));
            } else {
                consumerAmount = consumerPerMonth;
                subscribe.setConsumerTimes(subscribe.getConsumerTimes() + 1);
                subscribe.setReminderAmount(subscribe.getReminderAmount().subtract(consumerAmount));
            }
            BillCreateRequestDTO request = new BillCreateRequestDTO();
            request.setOrganizationId(subscribe.getOrganizationId());
            request.setBillingTime(new Date());
            request.setSubscribeId(subscribe.getId());
            request.setAmount(consumerAmount);
            request.setDesc("2OKR subscription");
            billService.createBill(request);
        }
    }


    public void updateReminderUserAmount(Subscribe subscribe, Long organizationId) {
        Integer currentMemberNum = this.memberDAO.countOrganizationMember(organizationId);
        subscribe.setReminderUserAmount(subscribe.getMaxUserAmount() - currentMemberNum);
    }

    @Override
    public void updateReminderUserAmount(Long organizationId) {
        Subscribe subscribe = subscribeDAO.getActiveSubscription(organizationId);
        if(subscribe!=null){
            updateReminderUserAmount(subscribe,organizationId);
        }
        this.subscribeDAO.updateByPrimaryKeySelective(subscribe);

    }

    public void updateCarrayAndSunscribeAmount(Subscribe subscribe) {
        if (subscribe.getOriginalSubscribeId() != null) {
            Subscribe originalSubscribe = this.subscribeDAO.selectByPrimaryKey(subscribe.getOriginalSubscribeId());
            subscribe.setCarryOverPayableAmount(originalSubscribe.getReminderAmount());
            subscribe.setSubscribePayableAmount(subscribe.getTotalAmount().subtract(subscribe.getCarryOverPayableAmount()));
            subscribe.initializeForUpdate();
            this.subscribeDAO.updateByPrimaryKeySelective(subscribe);
        }
    }

    private void combinePricePolicy(SubscriptionDTO subscription) {
        PricePolicyDTO pricePolicy = new PricePolicyDTO();
        BeanUtils.copyProperties(pricePolicyDAO.selectByPrimaryKey(subscription.getPricePolicyId()), pricePolicy);
        subscription.setPricePolicy(pricePolicy);
    }


    private void checkUpgradePricePolicyChose(SubscriptionDTO old, SubscriptionCreateRequestDTO requestDTO) {
        // 1,校验当前的价格策略对应的人数上限是否合适 2,校验订阅的人数不能小于当前已经使用的实际的人数 3，校验订阅的定额不能够小于结转的金额

        PricePolicy pricePolicy = pricePolicyDAO.selectByPrimaryKey(requestDTO.getPricePolicyId());

        if (pricePolicy.getPolicyType() == PolicyTypeEnum.FREE) {
            throw new CustomerException("can not subscribe a free subscription");
        }

        if (requestDTO.getSize() <= pricePolicy.getMinUserAmount() || requestDTO.getSize() > pricePolicy.getMaxUserAmount()) {
            throw new CustomerException("the price policy chosed not suitable");
        }

        Integer currentMemberNum = this.memberDAO.countOrganizationMember(requestDTO.getOrganizationId());
        if (currentMemberNum > requestDTO.getSize()) {
            throw new CustomerException("the subscription size should be biger then organization member count");
        }
        //price * month * size
        BigDecimal newSubscriptionAmount = pricePolicy.getPrice().multiply(new BigDecimal(pricePolicy.getLifeMonth())).multiply(new BigDecimal(requestDTO.getSize())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        if (old.getReminderAmount().compareTo(newSubscriptionAmount) > 0) {
            throw new CustomerException("can not chose a small level subscription");
        }
        //相同的订阅
        if (old.getPricePolicy().equals(requestDTO.getPricePolicyId()) && old.getMaxUserAmount().equals(requestDTO.getSize())) {
            throw new CustomerException("the same subscription is active,you need subscribe again");
        }

    }


    private void checkNewPricePolicyChose(SubscriptionCreateRequestDTO requestDTO) {

        PricePolicy pricePolicy = pricePolicyDAO.selectByPrimaryKey(requestDTO.getPricePolicyId());
        if (pricePolicy.getPolicyType() == PolicyTypeEnum.FREE) {
            throw new CustomerException("can not subscribe a free subscription");
        }
        if (requestDTO.getSize() <= pricePolicy.getMinUserAmount() || requestDTO.getSize() > pricePolicy.getMaxUserAmount()) {
            throw new CustomerException("the price policy chosed not suitable");
        }

        Integer currentMemberNum = this.memberDAO.countOrganizationMember(requestDTO.getOrganizationId());
        if (currentMemberNum > requestDTO.getSize()) {
            throw new CustomerException("the subscription size should be biger then organization member count");
        }

    }

    private void checkSubscriptionForOrder(Subscribe order, Subscribe lastUnpaid) {
        if (order == null || lastUnpaid == null) {
            throw new CustomerException(" has not subscription need to pay");
        }
        if (!order.getId().equals(lastUnpaid.getId())) {
            throw new CustomerException(" is not the last unpaid subscription");
        }
        if (!order.getSubscribeStatus().equals(SubscrideStatusEnum.TO_BE_PAID)) {
            throw new CustomerException(" subscription status error");
        }
    }

    public Subscribe initFreeSubscription(Long organizationId) {
        PricePolicy freePricePolcy = pricePolicyDAO.getFreePricePolicy();
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        Subscribe subscribe = new Subscribe();
        subscribe.setDesc("2OKR服务订阅");
        subscribe.setOrganizationId(organizationId);
        subscribe.setPricePolicyId(freePricePolcy.getId());
        subscribe.setMaxUserAmount(freePricePolcy.getMaxUserAmount());
        subscribe.setUnitPrice(freePricePolcy.getPrice());
        subscribe.setLifeMonth(freePricePolcy.getLifeMonth());
        subscribe.setIsFree((byte) 0);
        subscribe.setConsumerTimes(0);
        subscribe.setBeginDate(new Date());
        subscribe.setEndDate(DateUtil.getDayAfterFewMonth(new Date(), freePricePolcy.getLifeMonth()));
        subscribe.setSubscribedDate(new Date());
        subscribe.setTotalAmount(new BigDecimal(0.00));
        subscribe.setReminderAmount(new BigDecimal(0.00));
        subscribe.setSubscribePayableAmount(new BigDecimal(0.00));
        subscribe.setCarryOverPayableAmount(new BigDecimal(0.00));
        subscribe.setSubscribeStatus(SubscrideStatusEnum.ACTIVE);
        subscribe.setCreatedUser(currentUser.getId());
        subscribe.setCreatedName(currentUser.getName());
        subscribe.initializeForInsert();
        updateReminderUserAmount(subscribe, organizationId);
        return subscribe;
    }

    private Subscribe buildSubscribe(SubscriptionCreateRequestDTO requestDTO) {
        PricePolicy pricePolicy = pricePolicyDAO.selectByPrimaryKey(requestDTO.getPricePolicyId());
        UserInfoDTO userInfo = userService.getCurrentUserInfo();
        Subscribe subscribe = new Subscribe();
        subscribe.setDesc("2OKR服务订阅");
        subscribe.setOrganizationId(requestDTO.getOrganizationId());
        subscribe.setPricePolicyId(pricePolicy.getId());
        subscribe.setMaxUserAmount(requestDTO.getSize());
        subscribe.setUnitPrice(pricePolicy.getPrice());
        subscribe.setLifeMonth(pricePolicy.getLifeMonth());
        BigDecimal subscriptionAmount = subscribe.getUnitPrice().multiply(new BigDecimal(subscribe.getMaxUserAmount())).multiply(new BigDecimal(subscribe.getLifeMonth())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        subscribe.setTotalAmount(subscriptionAmount);
        subscribe.setReminderAmount(new BigDecimal(0.00));
        subscribe.setConsumerTimes(0);
        subscribe.setIsFree((byte) 1);
        subscribe.setSubscribedDate(new Date());
        subscribe.setSubscribeStatus(SubscrideStatusEnum.TO_BE_PAID);
        subscribe.setCreatedName(userInfo.getName());
        subscribe.setCreatedUser(userInfo.getId());
        subscribe.initializeForInsert();
        return subscribe;
    }

}
