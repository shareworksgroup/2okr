package com.coreteam.okr.web.task;

import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.service.LockService;
import com.coreteam.okr.service.WeeklyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WeeklyPlanDalilyStatusRecordTask
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/10 16:31
 * @Version 1.0.0
 */
@Service
@Slf4j
public class WeeklyPlanDalilyStatusRecordTask {
    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @Autowired
    private LockService lockService;

    @Scheduled(cron="0 55 23 * * ?")
    public void recordWeeklyPlanDalilyStatus(){
        log.info("WeeklyPlanDalilyStatusRecordTask:recordWeeklyPlanDalilyStatus 记录当天weekly plan的状态 开始");
        String methodName="recordWeeklyPlanDalilyStatus";
        if(lockService.tryLock(methodName)){
            try{
                weeklyPlanService.recordWeeklyPlanDalilyStatus();
            }catch (Exception e){
                log.error(ExceptionUtil.stackTraceToString(e));
            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("WeeklyPlanDalilyStatusRecordTask:recordWeeklyPlanDalilyStatus 记录当天weekly plan的状态 结束");
    }
}
