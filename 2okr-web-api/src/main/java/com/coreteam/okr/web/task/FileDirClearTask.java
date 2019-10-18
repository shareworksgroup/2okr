package com.coreteam.okr.web.task;

import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.common.util.PDFUtil;
import com.coreteam.okr.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FileDirClearTask
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/27 17:40
 * @Version 1.0.0
 */
@Service
@Slf4j
public class FileDirClearTask {
    @Autowired
    private LockService lockService;

    @Scheduled(cron="0 10 0 * * ?")
    public void clearFileDir(){
        log.info("FileDirClearTask 开始清理前一天的目录文件夹");
        String methodName="recordWeeklyPlanDalilyStatus";
        if(lockService.tryLock(methodName)){
            try{
                PDFUtil.clearYestadayDefaultDir();
            }catch (Exception e){
                log.error(ExceptionUtil.stackTraceToString(e));
            }finally {
                lockService.unLock(methodName);
            }
        }
        log.info("FileDirClearTask 开始清理前一天的目录文件夹");
    }
}
