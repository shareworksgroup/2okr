package com.coreteam.okr.web.listener;

import com.alibaba.fastjson.JSONObject;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName: UserInfoUpdateHandler
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/16 14:47
 * @Version 1.0.0
 */
@Component
@Slf4j
public class UserInfoUpdateHandler implements Handler {
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private KeyResultDAO keyResultDAO;
    @Autowired
    private ObjectiveDAO objectiveDAO;
    @Autowired
    private PlanDAO planDAO;
    @Autowired
    private WeeklyPlanDAO weeklyPlanDAO;
    @Autowired
    private WeeklyPlanItemDAO weeklyPlanItemDAO;

    @Override
    public void handle(String rawData) {
        try{
            log.info("UserInfoUpdateHandler:handle message from sso:"+rawData);
            JSONObject obj=JSONObject.parseObject(rawData);
            if("USERHASCHANGED".equalsIgnoreCase(obj.getString("type"))){
                JSONObject userInfo=obj.getJSONObject("data");
                if(userInfo!=null){
                    Long userId=userInfo.getLong("userId");
                    String userName=userInfo.getString("userName");
                    try{
                        log.info("update member user name");
                        memberDAO.updateMemberUserName(userId,userName);
                        log.info("update member user name success !");
                    }catch (Exception e){

                    }
                    try{
                        log.info("update key result owner name");
                        keyResultDAO.updateOwnerName(userId,userName);
                        log.info("update key result owner name success");
                    }catch (Exception e){

                    }
                    try{
                        log.info("update objective owner name");
                        objectiveDAO.updateOwnerName(userId,userName);
                        log.info("update objective owner name success");
                    }catch (Exception e){

                    }
                    try{
                        log.info("update plan owner name");
                        planDAO.updateOwnerName(userId,userName);
                        log.info("update plan owner name success");
                    }catch (Exception e){

                    }
                    try{
                        log.info("update weekly plan owner name");
                        weeklyPlanDAO.updateOwnerName(userId,userName);
                        log.info("update weekly plan owner name");
                    }catch (Exception e){

                    }
                    try{
                        log.info("update weekly plan item owner name");
                        weeklyPlanItemDAO.updateOwnerName(userId,userName);
                        log.info("update weekly plan item owner name");
                    }catch (Exception e){

                    }

                }
            }
        }catch (Exception e){
            log.error(ExceptionUtil.stackTraceToString(e));
        }
    }
}
