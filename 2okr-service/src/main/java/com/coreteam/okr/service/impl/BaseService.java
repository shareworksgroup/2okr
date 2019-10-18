package com.coreteam.okr.service.impl;

import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.BussinessLogEntityEnum;
import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;
import com.coreteam.okr.dto.log.BussinessLogDTO;
import com.coreteam.okr.dto.log.GetPagedBussinessLogDTO;
import com.coreteam.okr.dto.log.InsertBussinessLogDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.manager.BussinessLogManager;
import com.coreteam.okr.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: BaseService
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 11:24
 * @Version 1.0.0
 */
@Slf4j
public abstract class BaseService {

    protected void insertLog(Long entityId, Long organizationId, BussinessLogEntityEnum entityEnum, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        InsertBussinessLogDTO log = new InsertBussinessLogDTO();
        log.setRefEntityId(entityId);
        log.setOrganizationId(organizationId);
        log.setOperateType(operateTypeEnum);
        log.setDesc(desc);
        log.setEntityType(entityEnum);
        log.setOperatorId(getCurrentUser().getId());

        this.logService.insertLog(log);
    }

    protected PageInfo<BussinessLogDTO> listLog(GetPagedBussinessLogDTO getPagedBussinessLogDTO){
        return logService.pageListLogs(getPagedBussinessLogDTO);
    }

    protected UserInfoDTO getCurrentUser(){
        UserInfoDTO currentUser=null;
        try{
            currentUser    = userService.getCurrentUserInfo();
        }catch (Exception e){
            log.error("user service getCurrentUser error ! "+ ExceptionUtil.stackTraceToString(e));
        }
        if(currentUser==null){
            currentUser=new UserInfoDTO();
            currentUser.setId(-1L);
            currentUser.setName("unknow");
        }
        return currentUser;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private BussinessLogManager logService;

}
