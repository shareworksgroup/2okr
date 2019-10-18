package com.coreteam.okr.web.task;

import com.coreteam.okr.service.KeyResultService;
import com.coreteam.okr.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @ClassName: KeyResultWeeklyProgressRecordTask
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/23 10:28
 * @Version 1.0.0
 */
@Service
@Slf4j
public class KeyResultWeeklyProgressRecordTask {

    @Autowired
    private KeyResultService keyResultService;

    @Autowired
    private LockService lockService;

    @Scheduled(cron="0 4 0 ? * MON")
    public void recordKeyResultLaskWeekProgress(){
        log.info("KeyResultWeeklyProgressRecordTask:recordKeyResultLaskWeekProgress 记录上周的key-result的progress开始");
        String methodName="recordKeyResultLaskWeekProgress";
        if(lockService.tryLock(methodName)){
            try{
                keyResultService.recordKeyResultLastProgress();
            }catch (Exception e){

            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("KeyResultWeeklyProgressRecordTask:recordKeyResultLaskWeekProgress 记录上周的key-result的progress结束");
    }

}
