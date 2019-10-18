package com.coreteam.okr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.common.util.AESUtil;
import com.coreteam.okr.common.util.DateUtil;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.*;
import com.coreteam.okr.dto.Notify.KeyResultHasNewCommentSystemNotifyDTO;
import com.coreteam.okr.dto.Notify.MemberWeeklyPlanRegularReportEmailNotifyDTO;
import com.coreteam.okr.dto.Notify.PersonalWeeklyPlanReportEmailNotifyDTO;
import com.coreteam.okr.dto.Notify.WeeklyPlanHasNewCommmentSystemNotifyDTO;
import com.coreteam.okr.dto.log.BussinessLogDTO;
import com.coreteam.okr.dto.log.GetPagedBussinessLogDTO;
import com.coreteam.okr.dto.objective.GetOrgObjectiveRegularReportDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.objective.PlanLinkedObjectiveDTO;
import com.coreteam.okr.dto.plan.*;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.*;
import com.coreteam.okr.manager.Notify;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.*;
import com.coreteam.okr.util.WeeklyPlanViewRender;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: WeeklyPlanServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:26
 * @Version 1.0.0
 */
@Service
@Slf4j
public class WeeklyPlanServiceImpl extends BaseService implements WeeklyPlanService {
    private static Map<Integer,String> monthMap=new HashMap(){{
        put(0,"Jan");
        put(1,"Feb");
        put(2,"Mar");
        put(3,"Apr");
        put(4,"May");
        put(5,"Jun");
        put(6,"Jul");
        put(7,"Aug");
        put(8,"Sep");
        put(9,"Oct");
        put(10,"Nov");
        put(11,"Dec");

    }};

    @Value("${okr-service.weekly_report_path}")
    private String weeklyPlanReportPath;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public WeeklyPlanDTO listUserWeeklyPlanByOrg(ListWeeklyPlanDTO query) {
        Integer thisWeek = DateUtil.getCurrentWeek();
        if (query.getWeek() == null) {
            query.setWeek(thisWeek);
        } else if (query.getWeek() > thisWeek) {
            throw new CustomerException("week must not after this week " + thisWeek);
        }
        WeeklyPlanTemplateDTO template = getWeeklyTemplateByOrg(query.getOrganizationId());
        WeeklyPlan weeklyPlan = getUserWeeklyPlan(query.getUserId(), query.getWeek(), query.getOrganizationId(), template.getId());
        if (weeklyPlan == null) {
            //用户本周的weekly不存在，创建。
            try {
                UserInfoDTO user = getCurrentUser();
                weeklyPlan = new WeeklyPlan(user.getId(), user.getName(), query.getWeek(), 0, query.getOrganizationId(), template.getId());
                weeklyPlan.initializeForInsert();
                this.weeklyPlanDAO.insertSelective(weeklyPlan);
                carriedOverWeeklyPlanItem(query.getUserId(), query.getWeek(), weeklyPlan.getId(), query.getOrganizationId(), template);
                //创建完成后将weeklyplan 发送给需要接受的对象
                List<SimpleUserInfoDTO> reportReciver = memberService.listUsualWeeklyPlanReportReciver(query.getOrganizationId(), query.getUserId());
                if(!CollectionUtils.isEmpty(reportReciver)){
                    List<Long> reciverIds=new ArrayList<>();
                    for (SimpleUserInfoDTO reciver:reportReciver) {
                        reciverIds.add(reciver.getId());
                    }
                    autoSendThisWeeklyPlanReportToLeader(query.getOrganizationId(),query.getUserId(),reciverIds);
                }
            } catch (Exception e) {
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    //数据库设置了唯一锁，前端重复请求有可能造成weekly plan的重复创建
                    weeklyPlan = getUserWeeklyPlan(query.getUserId(), query.getWeek(), query.getOrganizationId(), template.getId());
                }
            }
        }
        WeeklyPlanDTO weeklyPlanDTO= bindItemsAndPlanForWeeklyPlan(weeklyPlan);
        weeklyPlanDTO.setTemplate(template);
        return weeklyPlanDTO;
    }

    @Override
    public void sendOrgWeeklyPlanReport(SendOrgWeeklyPlanReportDTO dto){
        //获取数据
        ObjectiveWeeklyPlanRegularEmailReportDTO okrAndWeeklyPlanList = getObjectiveWeeklyPlanRegularReportData(dto);
        dto.getEmail().forEach(email->{
            String greeting="Sir or Madam";
            try{
                UserInfoDTO userinfo = userService.getUserInfoByEmail(email);
                greeting=userinfo.getName();
            }catch (Exception e){
                log.error(ExceptionUtil.stackTraceToString(e));
                greeting="Sir or Madam";
            }

            String finalGreeting = greeting;
            notifyManager.sendNotify(new Notify<MemberWeeklyPlanRegularReportEmailNotifyDTO>() {
                @Override
                public NotifyTypeEnum type() {
                    return NotifyTypeEnum.EMAIL;
                }

                @Override
                public MemberWeeklyPlanRegularReportEmailNotifyDTO message() {
                    return new MemberWeeklyPlanRegularReportEmailNotifyDTO(email,okrAndWeeklyPlanList, finalGreeting);
                }

                @Override
                public NotifyBusinessType businessType() {
                    return NotifyBusinessType.EMAIL_ORG_REGULAR_REPORT;
                }

            });

        });
    }

    private ObjectiveWeeklyPlanRegularEmailReportDTO getObjectiveWeeklyPlanRegularReportData(SendOrgWeeklyPlanReportDTO dto) {
        WeekplayReportTranFormDTO rawData = listOrgWeeklyPlanReport(dto.getOrgid(), dto.getUserid(), dto.getWeek());
        if(CollectionUtils.isEmpty(rawData.getList())){
            throw new CustomerException("has no data to send email");
        }
        Set<Long> planLinkedOkrs=new HashSet<>();
        for(WeeklyPlanDTO planDTO:rawData.getList()){
            if(!CollectionUtils.isEmpty(planDTO.getItems())){
                for (WeeklyPlanItemDTO itemDTO:planDTO.getItems()){
                    if(itemDTO.getPlan()!=null&&itemDTO.getPlan().getLinkedObjectives()!=null){
                        for(ObjectiveDTO objectiveDTO:itemDTO.getPlan().getLinkedObjectives()){
                            planLinkedOkrs.add(objectiveDTO.getId());
                        }
                    }
                }
            }
        }

        WeeklyPlanReportObjectiveDTO data = WeeklyPlanViewRender.transformReportByObjective(rawData);
        GetOrgObjectiveRegularReportDTO okrQuery=new GetOrgObjectiveRegularReportDTO();
        okrQuery.setOrganizationId(dto.getOrgid());
        okrQuery.setUserId(dto.getUserid());
        okrQuery.setUserName(dto.getUserName());
        ObjectiveWeeklyPlanRegularEmailReportDTO okrAndWeeklyPlanList = okrService.prepareDataForOrgOKRRegularReport(okrQuery,planLinkedOkrs);
        List<WeeklyTemplateCategoryInfoDTO> categoryList = data.getCategorieList();
        Set<Long> categoryIdSet=new HashSet<>();
        categoryList.forEach(weeklyTemplateCategoryInfoDTO -> {
            categoryIdSet.add(weeklyTemplateCategoryInfoDTO.getId());
        });
        okrAndWeeklyPlanList.setCategorieList(categoryList);
        okrAndWeeklyPlanList.setUnLinkItemMap(data.getUnLinkItemMap());

        Map<Long, Map<String,List<WeeklyPlanItemViewDTO>>> okrWeeklyPlans=new HashMap<>();
        data.getOrgOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
            okrWeeklyPlans.put(objectiveConbineWeeklyPlanDTO.getId(),objectiveConbineWeeklyPlanDTO.getItemMap());
        });
        data.getTeamOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
            okrWeeklyPlans.put(objectiveConbineWeeklyPlanDTO.getId(),objectiveConbineWeeklyPlanDTO.getItemMap());
        });
        data.getMemberOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
            okrWeeklyPlans.put(objectiveConbineWeeklyPlanDTO.getId(),objectiveConbineWeeklyPlanDTO.getItemMap());
        });

        if(!CollectionUtils.isEmpty( okrAndWeeklyPlanList.getOrgOkrs().getOkrs())){
            okrAndWeeklyPlanList.getOrgOkrs().getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                objectiveConbineWeeklyPlanDTO.setItemMap(okrWeeklyPlans.get(objectiveConbineWeeklyPlanDTO.getId()));
            });
        }

        if(!CollectionUtils.isEmpty(okrAndWeeklyPlanList.getTeamOkrs())){
            okrAndWeeklyPlanList.getTeamOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
                if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                    memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                        objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                        objectiveConbineWeeklyPlanDTO.setItemMap(okrWeeklyPlans.get(objectiveConbineWeeklyPlanDTO.getId()));
                    });
                }
            });
        }

        if(!CollectionUtils.isEmpty(okrAndWeeklyPlanList.getMemberOkrs())){
            okrAndWeeklyPlanList.getMemberOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
                if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                    memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                        objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                        objectiveConbineWeeklyPlanDTO.setItemMap(okrWeeklyPlans.get(objectiveConbineWeeklyPlanDTO.getId()));
                    });
                }
            });
        }
        //构建member的weeklyplan的统计信息
        List<PersonalWeeklyPlanSimpleReportForRegularReportDTO> memberReportStatics=new ArrayList<>();
        rawData.getList().forEach(weeklyPlanDTO -> {
            PersonalWeeklyPlanSimpleReportForRegularReportDTO personWeeklyPlanSimpleReport=new PersonalWeeklyPlanSimpleReportForRegularReportDTO();
            personWeeklyPlanSimpleReport.setCategorieList(categoryList);
            personWeeklyPlanSimpleReport.setName(weeklyPlanDTO.getOwnerName());
            personWeeklyPlanSimpleReport.setWeeklyJobSatisfaction(weeklyPlanDTO.getWeeklyJobSatisfaction());
            Map<Long,Integer> planNum=new HashMap<>();
            weeklyPlanDTO.getItems().forEach(weeklyPlanItemDTO -> {
                Long categoryId = weeklyPlanItemDTO.getCategorieId();
                if(planNum.get(categoryId)==null){
                    if(categoryIdSet.contains(categoryId)){
                        planNum.put(categoryId,1);
                    }
                }else{
                    planNum.put(categoryId,planNum.get(categoryId)+1);
                }
            });
            personWeeklyPlanSimpleReport.setPlanNum(planNum);
            memberReportStatics.add(personWeeklyPlanSimpleReport);
        });
        okrAndWeeklyPlanList.setMemberReportStatics(memberReportStatics);
        JSONObject para=new JSONObject();
        para.put("orgId",dto.getOrgid());
        para.put("userId",dto.getUserid());
        para.put("week",dto.getWeek());
        String reportUrl= null;
        try {
            reportUrl = weeklyPlanReportPath+"?info="+ URLEncoder.encode(AESUtil.encryptInvitedInfo(para.toJSONString()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtil.stackTraceToString(e));
        }
        okrAndWeeklyPlanList.setReportUrl(reportUrl);
        return okrAndWeeklyPlanList;
    }

    @Override
    public ObjectiveWeeklyPlanRegularEmailReportDTO getObjectiveWeeklyPlanRegularReport(String info) {
        String jsonPara="";
        try {
            jsonPara=AESUtil.decryptInvitedInfo(URLDecoder.decode(info,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtil.stackTraceToString(e));
        }
        if(StringUtils.isEmpty(jsonPara)){
            throw new CustomerException("para illegal");
        }
        JSONObject para= JSON.parseObject(jsonPara);
        SendOrgWeeklyPlanReportDTO query=new SendOrgWeeklyPlanReportDTO();
        query.setOrgid(para.getLong("orgId"));
        query.setUserid(para.getLong("userId"));
        query.setWeek(para.getInteger("week"));
        return getObjectiveWeeklyPlanRegularReportData(query);
    }

    @Override
    @Transactional
    public void autoSendThisWeeklyPlanReportToLeader(Long organizationId, Long id, List<Long> reciverIds) {
        Integer thisWeek = DateUtil.getCurrentWeek();
        WeeklyPlanTemplateDTO template = getWeeklyTemplateByOrg(organizationId);
        WeeklyPlan weeklyPlan = getUserWeeklyPlan(id, thisWeek, organizationId, template.getId());
        if(weeklyPlan!=null){
            this.recordDAO.clearSendRecordByUser(id,thisWeek,weeklyPlan.getId());
            for (Long reciver:reciverIds) {
                WeeklyReportSendRecord record = new WeeklyReportSendRecord(weeklyPlan.getWeek(), reciver, weeklyPlan.getOwnerId(), weeklyPlan.getId());
                record.initializeForInsert();
                this.recordDAO.insertSelective(record);
            }
        }

    }


    private WeeklyPlanDTO bindItemsAndPlanForWeeklyPlan(WeeklyPlan weeklyPlan) {
        WeeklyPlanDTO weeklyPlanDTO = new WeeklyPlanDTO();
        BeanConvertUtils.copyEntityProperties(weeklyPlan, weeklyPlanDTO);
        List<WeeklyPlanItemDTO> weeklyPlanItems = BeanConvertUtils.batchTransform(WeeklyPlanItemDTO.class, this.weeklyPlanItemDAO.listUserWeeklyPlanItemByWeeklyPlanId(weeklyPlan.getId()));

        if (!CollectionUtils.isEmpty(weeklyPlanItems)) {
            for (WeeklyPlanItemDTO weeklyPlanItem : weeklyPlanItems) {
                PlanWithCommentNumDTO planWithCommentNum=this.planDAO.getPlanConbineComemntNumById(weeklyPlanItem.getPlanId());
                PlanDTO planDTO = new PlanDTO();
                BeanConvertUtils.copyEntityProperties(planWithCommentNum, planDTO);
                List<ObjectiveDTO> objectives = this.okrService.listObjectiveWhichWeeklyPlanLinked(planWithCommentNum.getId());
                planDTO.setLinkedObjectives(objectives);
                weeklyPlanItem.setPlan(planDTO);
                weeklyPlanItem.setCommentNum(planWithCommentNum.getCommentNums());
            }
        }
        weeklyPlanDTO.setItems(weeklyPlanItems);
        return weeklyPlanDTO;
    }

    @Override
    @Transactional
    public void insertWeeklyPlanItem(Long weeklyPlanId, InsertWeelyPlanItemDTO dto) {
        WeeklyPlan weeklyPlan = getWeeklyPlanById(weeklyPlanId);
        InsertWeekPlanItemAndPlanDTO insertDTO = new InsertWeekPlanItemAndPlanDTO(weeklyPlan.getId(), weeklyPlan.getWeek(), weeklyPlan.getOwnerId(), weeklyPlan.getOwnerName(), dto.getDesc(),
                PlanLeveEnum.MEMBER, weeklyPlan.getOrganizationId(), null, dto.getCategorieId());
        Long weeklyPlanItemId=insertWeeklyPlanItemAndPlan(insertDTO);
        if(dto.getObjectiveId()!=null){
            linkedPlanToObjective(weeklyPlanItemId,dto.getObjectiveId());
        }
    }

    @Override
    @Transactional
    public void updateWeeklyPlan(Long id, UpdateWeeklyPlanDTO updateWeeklyPlanDTO) {
        WeeklyPlan weeklyplan = getWeeklyPlanById(id);
        weeklyplan.setWeeklyJobSatisfaction(updateWeeklyPlanDTO.getWeeklyJobSatisfaction());
        weeklyplan.initializeForUpdate();
        this.weeklyPlanDAO.updateByPrimaryKeySelective(weeklyplan);
    }

    @Override
    @Transactional
    public void updateWeeklyPlanItemAndPlan(Long id, UpdateWeelyPlanItemDTO dto) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        WeeklyPlan weeklyPlan = getWeeklyPlanById(weeklyPlanItem.getWeeklyPlanId());
        if(weeklyPlan==null){
            throw new CustomerException(" Weekly plan not exist");
        }
        if (!StringUtils.isEmpty(dto.getColor())) {
            weeklyPlanItem.setColor(dto.getColor());
        }
        if (dto.getCategorieId() != null) {
            weeklyPlanItem.setCategorieId(dto.getCategorieId());
        }
        weeklyPlanItem.initializeForUpdate();
        this.weeklyPlanItemDAO.updateByPrimaryKeySelective(weeklyPlanItem);
        if (dto.getDueDate() != null || !StringUtils.isEmpty(dto.getDesc())) {
            Plan plan = this.planDAO.selectByPrimaryKey(weeklyPlanItem.getPlanId());
            if (dto.getDueDate() != null) {
                plan.setDueDate(dto.getDueDate());
            }
            if (!StringUtils.isEmpty(dto.getDesc())) {
                plan.setDesc(dto.getDesc());
            }
            plan.initializeForUpdate();
            this.planDAO.updateByPrimaryKeySelective(plan);
        }
        String desc = String.format("weekly plan is update, id is :%s ", weeklyPlanItem.getWeeklyPlanId());
        this.insertLog(weeklyPlanItem.getWeeklyPlanId(), BussinessLogOperateTypeEnum.UPDATE, desc);
    }

    @Override
    @Transactional
    public void deleteWeeklyPlanItemAndPlan(Long id, Boolean deletePlan) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        this.weeklyPlanItemDAO.deleteByPrimaryKey(weeklyPlanItem.getId());
        if (deletePlan) {
            this.planDAO.deleteByPrimaryKey(weeklyPlanItem.getPlanId());
        }
        String desc = String.format("weekly plan is delete, id is :%s ", weeklyPlanItem.getWeeklyPlanId());
        this.insertLog(weeklyPlanItem.getWeeklyPlanId(), BussinessLogOperateTypeEnum.DELETE, desc);
    }

    @Override
    public List<WeeklyPlanTimeLineDTO> listWeeklyPlanTimeLine(Long id) {
        return listLog(id);
    }

    @Override
    @Transactional
    public void insertPlanComment(Long id, String comment) {
        WeeklyPlanItem weeklyPlanItem = this.weeklyPlanItemDAO.selectByPrimaryKey(id);
        if (weeklyPlanItem == null) {
            throw new CustomerException("weeklyplan item is not exisit id:" + id);
        }
        UserInfoDTO userInfoDTO = getCurrentUser();
        PlanComment planComment = new PlanComment(weeklyPlanItem.getPlanId(), comment, userInfoDTO.getId(), userInfoDTO.getName());
        planComment.initializeForInsert();
        this.planCommentDAO.insertSelective(planComment);
        //评论不是weeklyplan的owner,则添加提醒
        WeeklyPlan weeklyPlan=this.weeklyPlanDAO.selectByPrimaryKey(weeklyPlanItem.getWeeklyPlanId());
        if(weeklyPlan!=null&&!userInfoDTO.getId().equals(weeklyPlan.getOwnerId())){
            sendKeyResultNewComentNotifycation(userInfoDTO.getName(),weeklyPlanItem.getOwnerName(),weeklyPlanItem.getOwnerId(),weeklyPlan.getOrganizationId());
        }
    }

    @Override
    public void updatePlanComment(Long id, String comment) {
        PlanComment planComment = this.planCommentDAO.selectByPrimaryKey(id);
        if (planComment == null) {
            throw new CustomerException("comment not exsist,id:" + id);
        }
        if (!getCurrentUser().getId().equals(planComment.getCommentUserId())) {
            throw new CustomerException(" can not update other comment");
        }
        planComment.setComment(comment);
        planComment.initializeForUpdate();
        this.planCommentDAO.updateByPrimaryKeySelective(planComment);
    }

    @Override
    public List<PlanCommentDTO> listPlanComments(Long id) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        return BeanConvertUtils.batchTransform(PlanCommentDTO.class, this.planCommentDAO.listCommentByPlanId(weeklyPlanItem.getPlanId()));
    }

    @Override
    public void deletePlanComment(Long id) {
        this.planCommentDAO.deleteByPrimaryKey(id);
    }

    @Override
    public void linkedPlanToObjective(Long id, Long objectiveId) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        PlanObjectiveLink linked = new PlanObjectiveLink();
        linked.initializeForInsert();
        linked.setObjectiveId(objectiveId);
        linked.setPlanId(weeklyPlanItem.getPlanId());
        this.planObjectiveLinkDAO.insertSelective(linked);
    }

    @Override
    public void updatePlanObjectiveLinks(Long id, PlanLinkedObjectiveDTO objectives) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        this.planObjectiveLinkDAO.deleteAllObjectiveLinked(weeklyPlanItem.getPlanId());
        if(!CollectionUtils.isEmpty(objectives.getObjectives())){
            objectives.getObjectives().forEach(objectiveId->{
                PlanObjectiveLink linked = new PlanObjectiveLink();
                linked.initializeForInsert();
                linked.setObjectiveId(objectiveId);
                linked.setPlanId(weeklyPlanItem.getPlanId());
                this.planObjectiveLinkDAO.insertSelective(linked);
            });
        }

    }

    @Override
    public void unlinkedPlanToObjective(Long id, Long objectiveId) {
        WeeklyPlanItem weeklyPlanItem = getWeeklyPlanItemById(id);
        this.planObjectiveLinkDAO.deleteByPlanIdAndObjectiveId(weeklyPlanItem.getPlanId(), objectiveId);
    }

    @Override
    @Transactional
    public void reportWeeklyPlan(Long id, WeeklyPlanReportReciverDTO reportReciverDTO) {
        //获取weekly plan
        WeeklyPlan weeklyPlan = getWeeklyPlanById(id);
        Long templateId=weeklyPlan.getTemplateId();
        WeeklyPlanTemplateDTO template = this.getWeeklyTemplateById(templateId);
        WeeklyPlanDTO weeklyPlanDTO= bindItemsAndPlanForWeeklyPlan(weeklyPlan);
        weeklyPlanDTO.setTemplate(template);
        WeeklyPlanCategoryDTO data = WeeklyPlanViewRender.transformCategoryView(weeklyPlanDTO);
        MemberSetting setting=this.memberSettingService.getPersonalWeeklyPlanTitle(weeklyPlan.getOrganizationId(),weeklyPlan.getOwnerId());
        String personalWeeklyPlanTitle="";
        if(setting!=null){
            personalWeeklyPlanTitle=setting.getValue();
        }else{
            personalWeeklyPlanTitle=getDefaultPersonalWeeklyPlanTitle(weeklyPlan.getOwnerName());
        }

        Set<Long> reciverList = new HashSet<>();
        if (reportReciverDTO.getMembers() != null) {
            reciverList.addAll(reportReciverDTO.getMembers());
        }
        if (!CollectionUtils.isEmpty(reportReciverDTO.getExternalReciver())) {
            reportReciverDTO.getExternalReciver().forEach(email -> {
                try {
                    UserInfoDTO userInfo = userService.getUserInfoByEmail(email);
                    if (userInfo != null && userInfo.getId() != null) {
                        reciverList.add(userInfo.getId());
                    } else {
                        log.error("reportWeeklyPlan the externalReciver not exsit :" + email);
                    }
                } catch (Exception e) {

                }
            });
        }
        if (!CollectionUtils.isEmpty(reciverList)) {
            String finalPersonalWeeklyPlanTitle = personalWeeklyPlanTitle;
            reciverList.forEach(reciver -> {
                WeeklyReportSendRecord record = new WeeklyReportSendRecord(weeklyPlan.getWeek(), reciver, weeklyPlan.getOwnerId(), id);
                record.initializeForInsert();
                this.recordDAO.insertSelective(record);
                UserInfoDTO reciverInfo = userService.getUserInfoById(reciver);
                notifyManager.sendNotify(new Notify<PersonalWeeklyPlanReportEmailNotifyDTO>() {
                    @Override
                    public NotifyTypeEnum type() {
                        return NotifyTypeEnum.EMAIL;
                    }
                    @Override
                    public PersonalWeeklyPlanReportEmailNotifyDTO message() {
                        return new PersonalWeeklyPlanReportEmailNotifyDTO(data,reciverInfo.getName(),reciverInfo.getEmail(),reportReciverDTO.getAttachPdf(), finalPersonalWeeklyPlanTitle);
                    }
                    @Override
                    public NotifyBusinessType businessType() {
                        return NotifyBusinessType.EMAIL_PERSONAL_WEEKLY_PLAN_REPORT;
                    }
                });
            });
        }

    }

    @Override
    @Transactional
    public void deletetWeeklyPlanReport(Long id, Long reciverId) {
        WeeklyPlan weeklyPlan = getWeeklyPlanById(id);
        this.recordDAO.deleteBySenderReciverAndWeek(weeklyPlan.getOwnerId(), reciverId, weeklyPlan.getWeek());

    }


    @Override
    public WeekplayReportTranFormDTO listOrgWeeklyPlanReport(Long entityId, Long userId, Integer week) {
        Integer thisWeek = DateUtil.getCurrentWeek();
        if (week == null) {
            week = thisWeek;
        } else if (week > thisWeek) {
            throw new CustomerException("week must not after this week " + thisWeek);
        }
        WeeklyPlanTemplateDTO template = getWeeklyTemplateByOrg(entityId);
        List<WeeklyPlan> weeklyPlansOfTeamReport = this.weeklyPlanDAO.listOrgWeeklyPlanReported(entityId, userId, week, template.getId());
        List<WeeklyPlanDTO> weeklyPlanDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(weeklyPlansOfTeamReport)) {
            weeklyPlansOfTeamReport.forEach(weeklyPlan -> {
                weeklyPlanDTOList.add(bindItemsAndPlanForWeeklyPlan(weeklyPlan));
            });
        }
        WeekplayReportTranFormDTO dto=new WeekplayReportTranFormDTO();
        dto.setList(weeklyPlanDTOList);
        dto.setTemplate(template);
        return dto;
    }

    @Override
    public List<WeeklyPlanReportSummaryDTO> listOrgWeeklyPlanReportSummary(Long id, Integer week) {
        Integer thisWeek = DateUtil.getCurrentWeek();
        if (week == null) {
            week = thisWeek;
        } else if (week > thisWeek) {
            throw new CustomerException("week must not after this week " + thisWeek);
        }
        UserInfoDTO user = getCurrentUser();
        WeeklyPlanTemplateDTO template = templateService.getWeeklyPlanTemplateByOrganization(id);
        return this.weeklyPlanDAO.listTeamWeeklyPlanReportSummary(id,user.getId(), week,template.getId());
    }

    @Override
    @Transactional
    public void recordWeeklyPlanDalilyStatus() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Map<Long, Integer> userJobSatisfaction = new HashMap<>();
        Map<Long, Long> weekOrgMap = new HashMap<>();
        Map<Long, Long> templateMap = new HashMap<>();
        Integer week = DateUtil.getCurrentWeek();
        List<WeeklyPlanItem> currentWeeklyPlanItem = this.weeklyPlanItemDAO.listWeeklyPlanItem(week);
        if (!CollectionUtils.isEmpty(currentWeeklyPlanItem)) {
            for (WeeklyPlanItem item : currentWeeklyPlanItem) {
                try {
                    WeeklyPlanItemDalilyStatus dalilyStatus = new WeeklyPlanItemDalilyStatus(item.getWeeklyPlanId(),item.getId(), date, item.getCategorieId(), item.getOwnerId(), item.getOwnerName(), item.getWeek());
                    Integer jobStatis = userJobSatisfaction.get(item.getWeeklyPlanId());
                    if (jobStatis == null) {
                        WeeklyPlan weeklyPlan = this.weeklyPlanDAO.selectByPrimaryKey(item.getWeeklyPlanId());
                        jobStatis = weeklyPlan.getWeeklyJobSatisfaction() == null ? 0 : weeklyPlan.getWeeklyJobSatisfaction();
                        userJobSatisfaction.put(item.getWeeklyPlanId(), jobStatis);
                        weekOrgMap.put(item.getWeeklyPlanId(), weeklyPlan.getOrganizationId());
                        templateMap.put(item.getWeeklyPlanId(),weeklyPlan.getTemplateId());
                    }
                    dalilyStatus.setJobStatisfaction(jobStatis);
                    dalilyStatus.setOrganizationId(weekOrgMap.get(item.getWeeklyPlanId()));
                    dalilyStatus.setTemplateId(templateMap.get(item.getWeeklyPlanId()));
                    dalilyStatus.initializeForInsert();
                    this.weeklyPlanItemDalilyStatusDAO.insertSelective(dalilyStatus);
                } catch (Exception e) {
                    log.error(ExceptionUtil.stackTraceToString(e));
                }
            }
        }
    }


    @Transactional
    public Long insertWeeklyPlanItemAndPlan(InsertWeekPlanItemAndPlanDTO dto) {
        Plan plan = new Plan(dto.getDesc(), dto.getLevel(), dto.getOwnerId(), dto.getOwnerName(), (byte) 0, null);
        plan.initializeForInsert();
        this.planDAO.insertSelective(plan);
        WeeklyPlanItem weeklyPlanItem = new WeeklyPlanItem(dto.getWeeklyPlanId(), plan.getId(), dto.getWeek(), dto.getCategorieId(), null, 0, dto.getOwnerId(), dto.getOwnerName());
        weeklyPlanItem.initializeForInsert();
        this.weeklyPlanItemDAO.insertSelective(weeklyPlanItem);
        String desc = String.format("weekly plan is insert id is :%s ", dto.getWeeklyPlanId());
        this.insertLog(dto.getWeeklyPlanId(), BussinessLogOperateTypeEnum.INSERT, desc);
        return weeklyPlanItem.getId();
    }


    @Transactional
    public void carriedOverWeeklyPlanItem(Long userId, Integer week, Long weeklyPlanId, Long orgId, WeeklyPlanTemplateDTO template) {
        Integer lastWeek=weeklyPlanDAO.getLaskWeekForUserWeeklyPlan(userId, week, orgId, template.getId());
        if(lastWeek==null){
            lastWeek=0;
        }
        Set<Long> statusNeedCarry = new HashSet<>();
        template.getItems().forEach(item -> {
            if (item.getNeedCarry()) {
                statusNeedCarry.add(item.getId());
            }
        });
        //搬运上周的category
        List<WeeklyPlanItem> weeklyPlanItems = weeklyPlanItemDAO.listUndoneWeeklyPlanItemByUser(userId, lastWeek, orgId, template.getId(), new ArrayList<Long>() {{
            addAll(statusNeedCarry);
        }});
        if (!CollectionUtils.isEmpty(weeklyPlanItems)) {
            updateWeeklyPlanItemForCarriedOver(weeklyPlanItems, week, weeklyPlanId);
            for (WeeklyPlanItem weeklyPlan : weeklyPlanItems) {
                this.weeklyPlanItemDAO.insertSelective(weeklyPlan);
                String desc = String.format("%s weekly plan is carried over ! ", weeklyPlan.getId());
                insertLog(weeklyPlanId, BussinessLogOperateTypeEnum.CARRIED_OVER, desc);
            }
        }
    }

    private void updateWeeklyPlanItemForCarriedOver(List<WeeklyPlanItem> list, Integer week, Long id) {
        for (WeeklyPlanItem weeklyPlanItem : list) {
            weeklyPlanItem.setCarriedOverAge(weeklyPlanItem.getCarriedOverAge() + (week - weeklyPlanItem.getWeek()));
            weeklyPlanItem.setWeek(week);
            weeklyPlanItem.setWeeklyPlanId(id);
            weeklyPlanItem.setId(null);
            weeklyPlanItem.setCreatedAt(null);
            weeklyPlanItem.setUpdatedAt(null);
            weeklyPlanItem.setDeletedAt(null);
            weeklyPlanItem.initializeForInsert();
        }
    }

    private WeeklyPlanItem getWeeklyPlanItemById(Long id) {
        WeeklyPlanItem weeklyPlanItem = this.weeklyPlanItemDAO.selectByPrimaryKey(id);
        if (weeklyPlanItem == null) {
            throw new CustomerException("weeklyplan item is not exisit id:" + id);
        }
        return weeklyPlanItem;
    }

    private WeeklyPlan getWeeklyPlanById(Long id) {
        WeeklyPlan weeklyPlan = this.weeklyPlanDAO.selectByPrimaryKey(id);
        if (weeklyPlan == null) {
            throw new CustomerException("weeklyplan is not exsit id:" + id);
        }
        return weeklyPlan;
    }

    private WeeklyPlan getUserWeeklyPlan(Long userId, Integer week, Long organizationId, Long templateId) {
        return this.weeklyPlanDAO.getUserWeeklyPlanByOrg(userId, week, organizationId, templateId);
    }

    private void insertLog(Long id, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        this.insertLog(id, null, BussinessLogEntityEnum.WEEKLYPLAN, operateTypeEnum, desc);
    }

    private List<WeeklyPlanTimeLineDTO> listLog(Long id) {
        GetPagedBussinessLogDTO query = new GetPagedBussinessLogDTO();
        query.setEntityType(BussinessLogEntityEnum.WEEKLYPLAN);
        query.setRefEntityId(id);
        query.setPageNumber(Integer.valueOf(1));
        query.setPageSize(Integer.valueOf(0));
        PageInfo<BussinessLogDTO> businessLogList = listLog(query);
        return BeanConvertUtils.batchTransform(WeeklyPlanTimeLineDTO.class, businessLogList.getList());
    }


    private WeeklyPlanTemplateDTO getWeeklyTemplateByOrg(Long orgId) {
        WeeklyPlanTemplateDTO template = templateService.getWeeklyPlanTemplateByOrganization(orgId);
        if (template != null) {
            return template;
        } else {
            return templateService.getDefaultTemplate();
        }
    }

    private WeeklyPlanTemplateDTO getWeeklyTemplateById(Long templateId) {
        WeeklyPlanTemplateDTO template = templateService.getWeeklyPlanTemplateById(templateId);
        if (template != null) {
            return template;
        } else {
            throw new CustomerException(" weekly plan template not exsit ");
        }
    }

    private void checkWeeklyPlanStatus(WeeklyPlanTemplateDTO template,String status){
        if(CollectionUtils.isEmpty(template.getItems())){
            throw new CustomerException(" Weekly plan template has not category " + status);
        }
        template.getItems().forEach(item->{
            if(item.getName().equals(status)){
                return ;
            }
        });
        throw new CustomerException(" Weekly plan template has not category " + status);
    }

    public static String getDefaultPersonalWeeklyPlanTitle(String userName){
        String title="Weekly Report By "+ userName;
        return title;
    }

    private void sendKeyResultNewComentNotifycation(String commenter,String userName, Long userId,Long organizationId){
        notifyManager.sendNotify(new Notify<WeeklyPlanHasNewCommmentSystemNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.SYSTEM;
            }

            @Override
            public WeeklyPlanHasNewCommmentSystemNotifyDTO message() {
                return new WeeklyPlanHasNewCommmentSystemNotifyDTO(commenter,userName,userId,organizationId);
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.SYSTEM_WEEKLY_PLAN_HAS_NEW_COMMENT;
            }
        });
    }

    @Autowired
    private WeeklyPlanDAO weeklyPlanDAO;

    @Autowired
    private WeeklyPlanItemDAO weeklyPlanItemDAO;

    @Autowired
    private WeeklyReportSendRecordDAO recordDAO;

    @Autowired
    private PlanDAO planDAO;

    @Autowired
    private PlanObjectiveLinkDAO planObjectiveLinkDAO;

    @Autowired
    private PlanCommentDAO planCommentDAO;

    @Autowired
    private OkrService okrService;

    @Autowired
    private ObjectiveDAO objectiveDao;

    @Autowired
    private UserService userService;

    @Autowired
    private WeeklyPlanItemDalilyStatusDAO weeklyPlanItemDalilyStatusDAO;

    @Autowired
    private NotifyManager notifyManager;

    @Autowired
    private WeeklyPlanTemplateService templateService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberSettingService memberSettingService;


}
