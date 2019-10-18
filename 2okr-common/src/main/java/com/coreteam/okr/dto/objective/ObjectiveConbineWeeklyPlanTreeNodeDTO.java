package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.coreteam.okr.dto.plan.WeeklyPlanItemViewDTO;
import com.coreteam.okr.dto.plan.WeeklyTemplateCategoryInfoDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveConbineWeeklyPlanTreeNodeDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/15 17:40
 * @Version 1.0.0
 */
public class ObjectiveConbineWeeklyPlanTreeNodeDTO {
    private Long id;

    private Long linkedObjectiveId;

    private String name;

    private String desc;

    private ObjectiveStatusEnum status;

    private ObjectiveBusinessStatusEnum businessStatus;

    private ObjectiveLevelEnum level;

    private Date periodStartTime;

    private Date periodEndTime;

    private Double progress;

    private Double lastProgress;

    private ObjectivePrivilege privilege;

    private List<ObjectiveKeyResultDTO> keyResults;

    private List<WeeklyTemplateCategoryInfoDTO> categorieList;

    Map<String,List<WeeklyPlanItemViewDTO>> itemMap;

    private List<ObjectiveConbineWeeklyPlanTreeNodeDTO> children;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLinkedObjectiveId() {
        return linkedObjectiveId;
    }

    public void setLinkedObjectiveId(Long linkedObjectiveId) {
        this.linkedObjectiveId = linkedObjectiveId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ObjectiveStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ObjectiveStatusEnum status) {
        this.status = status;
    }

    public ObjectiveBusinessStatusEnum getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(ObjectiveBusinessStatusEnum businessStatus) {
        this.businessStatus = businessStatus;
    }

    public ObjectiveLevelEnum getLevel() {
        return level;
    }

    public void setLevel(ObjectiveLevelEnum level) {
        this.level = level;
    }

    public Date getPeriodStartTime() {
        return periodStartTime;
    }

    public void setPeriodStartTime(Date periodStartTime) {
        this.periodStartTime = periodStartTime;
    }

    public Date getPeriodEndTime() {
        return periodEndTime;
    }

    public void setPeriodEndTime(Date periodEndTime) {
        this.periodEndTime = periodEndTime;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public List<ObjectiveKeyResultDTO> getKeyResults() {
        return keyResults;
    }

    public void setKeyResults(List<ObjectiveKeyResultDTO> keyResults) {
        this.keyResults = keyResults;
    }

    public List<WeeklyTemplateCategoryInfoDTO> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<WeeklyTemplateCategoryInfoDTO> categorieList) {
        this.categorieList = categorieList;
    }

    public Map<String, List<WeeklyPlanItemViewDTO>> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, List<WeeklyPlanItemViewDTO>> itemMap) {
        this.itemMap = itemMap;
    }

    public List<ObjectiveConbineWeeklyPlanTreeNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ObjectiveConbineWeeklyPlanTreeNodeDTO> children) {
        this.children = children;
    }

    public Double getLastProgress() {
        return lastProgress;
    }

    public void setLastProgress(Double lastProgress) {
        this.lastProgress = lastProgress;
    }

    public ObjectivePrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(ObjectivePrivilege privilege) {
        this.privilege = privilege;
    }
}
