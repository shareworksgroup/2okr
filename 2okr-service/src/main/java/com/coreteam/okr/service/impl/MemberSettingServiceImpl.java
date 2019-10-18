package com.coreteam.okr.service.impl;

import com.coreteam.okr.dao.MemberSettingDAO;
import com.coreteam.okr.entity.MemberSetting;
import com.coreteam.okr.service.MemberSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: MemberSettingServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/21 16:34
 * @Version 1.0.0
 */
@Service
public class MemberSettingServiceImpl implements MemberSettingService {


    @Autowired
    private MemberSettingDAO memberSettingDAO;

    @Override
    public MemberSetting getOrganizationCreateGuide(Long userId) {
        MemberSetting setting = this.memberSettingDAO.getSettingByKey(userId, "GUIDE", "ORGANIZATION_GUIDE");
        return setting;
    }

    @Override
    public void setOrganizationCreateGuidIgnore(Long userId) {
        MemberSetting setting = this.memberSettingDAO.getSettingByKey(userId, "GUIDE", "ORGANIZATION_GUIDE");
        if(setting==null){
            setting=new MemberSetting();
            setting.setUserId(userId);
            setting.setSettingType("GUIDE");
            setting.setSettingKey("ORGANIZATION_GUIDE");
            setting.setValue("IGNORE");
            setting.initializeForInsert();
            this.memberSettingDAO.insertSelective(setting);
        }
    }

    @Override
    public MemberSetting getDefaultOrganization(Long userId) {
        return this.memberSettingDAO.getSettingByKey(userId,"ORGANIZATION_DEFAULT","ORGANIZATION_DEFAULT");
    }

    @Override
    public void setDefaultOrganization(Long userId, Long orgId) {
        MemberSetting setting=this.memberSettingDAO.getSettingByKey(userId,"ORGANIZATION_DEFAULT","ORGANIZATION_DEFAULT");
        if(setting==null){
            setting=new MemberSetting();
            setting.setUserId(userId);
            setting.setSettingType("ORGANIZATION_DEFAULT");
            setting.setSettingKey("ORGANIZATION_DEFAULT");
            setting.setValue(String.valueOf(orgId));
            setting.initializeForInsert();
            this.memberSettingDAO.insertSelective(setting);
        }else{
            setting.setValue(String.valueOf(orgId));
            setting.initializeForUpdate();
            this.memberSettingDAO.updateByPrimaryKeySelective(setting);
        }

    }

    @Override
    public MemberSetting getUserThemeSetting(Long id) {
         return this.memberSettingDAO.getSettingByKey(id,"THEAM_DEFAULT","THEAM_DEFAULT");
    }

    @Override
    public MemberSetting getUserLangSetting(Long id) {
        return this.memberSettingDAO.getSettingByKey(id,"LANG_DEFAULT","LANG_DEFAULT");
    }

    @Override
    public void updateUserThemeSetting(Long id,String value) {
        //查询user theme是否存在
        MemberSetting setting=this.memberSettingDAO.getSettingByKey(id,"THEAM_DEFAULT","THEAM_DEFAULT");
        if(setting==null){
            setting=new MemberSetting();
            setting.setUserId(id);
            setting.setSettingType("THEAM_DEFAULT");
            setting.setSettingKey("THEAM_DEFAULT");
            setting.setValue(value);
            setting.initializeForInsert();
            this.memberSettingDAO.insertSelective(setting);
        }else{
            setting.setValue(String.valueOf(value));
            setting.initializeForUpdate();
            this.memberSettingDAO.updateByPrimaryKeySelective(setting);
        }
    }

    @Override
    public void updateUserLangSetting(Long id,String value) {
        //查询user的language是否存在
        MemberSetting setting=this.memberSettingDAO.getSettingByKey(id,"LANG_DEFAULT","LANG_DEFAULT");
        if(setting==null){
            setting=new MemberSetting();
            setting.setUserId(id);
            setting.setSettingType("LANG_DEFAULT");
            setting.setSettingKey("LANG_DEFAULT");
            setting.setValue(value);
            setting.initializeForInsert();
            this.memberSettingDAO.insertSelective(setting);
        }else{
            setting.setValue(String.valueOf(value));
            setting.initializeForUpdate();
            this.memberSettingDAO.updateByPrimaryKeySelective(setting);
        }

    }

    @Override
    public void setUsualWeeklyPlanReportReciver(Long id, Long organizationId, List<Long> reciverIds) {
        String type="WEEKLYPLAN_USUAL_RECIVER_"+organizationId;
        String key="RECIVER_USER_ID_";
        //先删除之前的配置
        this.memberSettingDAO.deleteByType(id,type);
        Set<Long> reciverSet=new HashSet<>();
        reciverSet.addAll(reciverIds);
        if(!CollectionUtils.isEmpty(reciverSet)){
            reciverSet.forEach(revicerId->{
                MemberSetting setting=new MemberSetting();
                setting.setUserId(id);
                setting.setSettingType(type);
                setting.setSettingKey(key+revicerId);
                setting.setValue(String.valueOf(revicerId));
                setting.initializeForInsert();
                this.memberSettingDAO.insertSelective(setting);
            });

        }


    }

    @Override
    public List<Long> listUsualWeeklyPlanReportReciver(Long organizationId, Long id) {
        List<Long> recivers=new ArrayList<>();
        String type="WEEKLYPLAN_USUAL_RECIVER_"+organizationId;
        String key="RECIVER_USER_ID_";
        List<MemberSetting> settings = this.memberSettingDAO.listSettingByType(id, type);
        if(!CollectionUtils.isEmpty(settings)){
            settings.forEach(setting->{
                recivers.add(Long.valueOf(setting.getValue()));
            });
        }
        return recivers;
    }

    @Override
    public void setPersonalWeeklyPlanTitle(Long organizationId, Long id, String title) {
        String type="PERSONAL_WEEKLY_PLAN_TITLE";
        String key="ORGANIZATION_ID_"+organizationId;
        MemberSetting setting=this.memberSettingDAO.getSettingByKey(id,type,key);
        if(setting==null){
            setting=new MemberSetting();
            setting.setUserId(id);
            setting.setSettingType(type);
            setting.setSettingKey(key);
            setting.setValue(title);
            setting.initializeForInsert();
            this.memberSettingDAO.insertSelective(setting);
        }else{
            setting.setValue(title);
            setting.initializeForUpdate();
            this.memberSettingDAO.updateByPrimaryKeySelective(setting);
        }

    }

    @Override
    public MemberSetting getPersonalWeeklyPlanTitle(Long organizationId, Long id) {
        String type="PERSONAL_WEEKLY_PLAN_TITLE";
        String key="ORGANIZATION_ID_"+organizationId;
        return this.memberSettingDAO.getSettingByKey(id,type,key);
    }


}
