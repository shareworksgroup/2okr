package com.coreteam.okr.web.task;

import com.coreteam.okr.service.LockService;
import com.coreteam.okr.service.OkrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ObjectiveTask
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 11:14
 * @Version 1.0.0
 */
@Service
@Slf4j
public class ObjectiveTask {

    @Autowired
    private OkrService okrService;

    @Autowired
    private LockService lockService;

    @Scheduled(cron="0 1 0 * * ?")
    public void updateObjectiveDailyProgress(){
        log.info("ObjectiveTask:updateObjectiveDailyProgress 更新前一天objective的progress 开始");
        String methodName="updateObjectiveDailyProgress";
        if(lockService.tryLock(methodName)){
            try{
                okrService.updateObjectivePorgressDaily();
            }catch (Exception e){

            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("ObjectiveTask:updateObjectiveDailyProgress 更新前一天objective的progress 结束");
    }

    //每周提醒off-tract的objective
    @Scheduled(cron="0 02 0 ? * MON")
    public void remindToUpdateOffTractObjective(){
        log.info("ObjectiveTask:remindToUpdateOffTractObjective 添加off-tract objective提醒开始");
        String methodName="remindToUpdateOffTractObjective";
        if(lockService.tryLock(methodName)){
            try{
                okrService.recordOffTractObjectiveUpdateRemind();
            }catch (Exception e){

            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("ObjectiveTask:remindToUpdateOffTractObjective 添加off-tract objective提醒结束");
    }

    //记录每周Objective的progress 进度
    @Scheduled(cron="0 03 0 ? * MON")
    public void recordObjectiveLaskWeekProgress(){
        log.info("ObjectiveTask:recordObjectiveLaskWeekProgress 记录objective lastweek progresss 开始");
        String methodName="recordObjectiveLaskWeekProgress";
        if(lockService.tryLock(methodName)){
            try{
                okrService.recordObjectiveLaskWeekProgress();
            }catch (Exception e){

            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("ObjectiveTask:recordObjectiveLaskWeekProgress 记录objective lastweek progresss 结束");
    }
}
