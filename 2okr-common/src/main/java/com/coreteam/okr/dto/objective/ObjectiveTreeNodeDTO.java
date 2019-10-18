package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ObjectiveTreeNodeDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/28 15:39
 * @Version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveTreeNodeDTO implements Serializable {

    private Long id;

    private Long linkedObjectiveId;

    private String name;

    private String desc;

    /**
     * Organization
     */
    private Long organizationId;

    private Long teamId;

    private String groupName;

    private ObjectiveStatusEnum status;

    private ObjectiveBusinessStatusEnum businessStatus;

    private ObjectiveLevelEnum level;

    private Long ownerId;

    private String ownerName;

    private Long validatorId;

    private String validatorName;

    private Date periodStartTime;

    private Date periodEndTime;

    private String alignment;

    private Long confirmUserId;

    private String confirmUserName;

    private String confirmComment;

    private Long createdUser;

    private String createdName;

    private Date confirmAt;

    private Double confirmFinalResult;

    private Double progress;

    private Double lastProgress;

    private ObjectivePrivilege privilege;

    private List<ObjectiveKeyResultDTO> keyResults;

    private List<ObjectiveTreeNodeDTO> children;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Long validatorId) {
        this.validatorId = validatorId;
    }

    public String getValidatorName() {
        return validatorName;
    }

    public void setValidatorName(String validatorName) {
        this.validatorName = validatorName;
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

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public String getConfirmUserName() {
        return confirmUserName;
    }

    public void setConfirmUserName(String confirmUserName) {
        this.confirmUserName = confirmUserName;
    }

    public String getConfirmComment() {
        return confirmComment;
    }

    public void setConfirmComment(String confirmComment) {
        this.confirmComment = confirmComment;
    }

    public Date getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(Date confirmAt) {
        this.confirmAt = confirmAt;
    }

    public Double getConfirmFinalResult() {
        return confirmFinalResult;
    }

    public void setConfirmFinalResult(Double confirmFinalResult) {
        this.confirmFinalResult = confirmFinalResult;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public List<ObjectiveTreeNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ObjectiveTreeNodeDTO> children) {
        this.children = children;
    }

    public List<ObjectiveKeyResultDTO> getKeyResults() {
        return keyResults;
    }

    public void setKeyResults(List<ObjectiveKeyResultDTO> keyResults) {
        this.keyResults = keyResults;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getLastProgress() {
        return lastProgress;
    }

    public void setLastProgress(Double lastProgress) {
        this.lastProgress = lastProgress;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public ObjectivePrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(ObjectivePrivilege privilege) {
        this.privilege = privilege;
    }
}
