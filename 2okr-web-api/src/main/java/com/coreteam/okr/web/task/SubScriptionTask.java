package com.coreteam.okr.web.task;

import com.coreteam.okr.service.LockService;
import com.coreteam.okr.service.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @ClassName: SubScriptionTask
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/27 11:00
 * @Version 1.0.0
 */

@Service
@Slf4j
public class SubScriptionTask {
    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private LockService lockService;

    //每个月最后一天执行，spring sheduled 不支持 L W C表示，后续改成quartz
    @Scheduled(cron="0 59 23 28-31 * ?")
    public void consumerSubscriptionPerMonthAtLastDay(){
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            log.info("SubScriptionTask:consumerSubscriptionPerMonthAtLastDay 每个月的最后一天开始计费下个月的消费");
            String methodName="consumerSubscriptionPerMonthAtLastDay";
            if(lockService.tryLock(methodName)){
                try{
                    subscribeService.consumerSubscriptionPerMonthAtLastDay();
                }catch (Exception e){

                }finally {
                    lockService.unLock(methodName);
                }
            }
            log.info("SubScriptionTask:consumerSubscriptionPerMonthAtLastDay 每个月的最后一天开始计费下个月的消费 结束");
        }
    }

    @Scheduled(cron="0 5 0 * * ?")
    public void updateSubscriptionStatus(){
        log.info("SubScriptionTask:updateSubscriptionStatus 更新active的subscription的状态开始");
        String methodName="updateSubscriptionStatus";
        if(lockService.tryLock(methodName)){
            try{
                subscribeService.updateSubscriptionStatus();
            }catch (Exception e){

            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("SubScriptionTask:updateSubscriptionStatus 更新active的subscription的状态开始");
    }


}
