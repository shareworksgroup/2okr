package com.coreteam.okr.web.listener;

import com.alibaba.fastjson.JSONObject;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Invite;
import com.coreteam.okr.service.InviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: InviteRegisterHandler
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 12:45
 * @Version 1.0.0
 */
@Component
@Slf4j
public class InviteRegisterHandler implements Handler{

    @Autowired
    private InviteService inviteService;

    @Override
    public void handle(String rawData) {
        try{
            log.info("InviteRegisterHandler:handle message from sso:"+rawData);
            JSONObject obj=JSONObject.parseObject(rawData);
            if("REGISTER".equalsIgnoreCase(obj.getString("type"))){
                JSONObject data=obj.getJSONObject("data");
                String email=data.getString("email");
                UserInfoDTO userInfo = new UserInfoDTO();
                userInfo.setId(data.getLong("userId"));
                userInfo.setName(data.getString("userName"));
                userInfo.setEmail(email);
                List<Invite> registerInviteds=this.inviteService.listRegisterInvitedEmail(email);
                if(!CollectionUtils.isEmpty(registerInviteds)){
                    for (Invite invite:registerInviteds) {
                        inviteService.joinTheTeamAfterRegister(invite,userInfo);
                    }
                }

            }
        }catch (Exception e){
            log.error(ExceptionUtil.stackTraceToString(e));
        }

    }


}
