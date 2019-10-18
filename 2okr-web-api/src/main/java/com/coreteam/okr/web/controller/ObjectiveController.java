package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.plan.WeeklyPlanSingleObjectiveDTO;
import com.coreteam.okr.service.OkrService;
import com.coreteam.okr.util.WeeklyPlanViewRender;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: ObjectiveController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/09 10:41
 * @Version 1.0.0
 */
@RestController
@RequestMapping("objectives")
@AuditLogAnnotation(value = "objective接口")
@Slf4j
public class ObjectiveController {

    @PostMapping
    @ApiOperation("添加objective")
    @SubscriptionActiveLimit(organizationId = "#{insertObjectiveDTO.organizationId}")
    public Long insertObjective(@RequestBody @Valid InsertObjectiveDTO insertObjectiveDTO) {
        return okrService.insertObjective(insertObjectiveDTO);
    }

    @PutMapping
    @ApiOperation("更新objective的基本信息")
    public void updateObjective(@RequestBody @Valid UpdateObjectiveDTO updateObjectiveDTO) {
        okrService.updateObjective(updateObjectiveDTO);
    }

    @PutMapping("/status")
    @ApiOperation("更新objective的起止时间状态")
    public void updateObjectiveStatus(@RequestBody @Valid UpdateObjectiveStatusDTO updateObjectiveDTO) {
        okrService.updateObjectiveStatus(updateObjectiveDTO);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除指定的objective")
    public void deleteObjective(@PathVariable("id") @NotNull Long objectiveId) {
        okrService.deleteObjective(objectiveId);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取指定的objective")
    public ObjectiveDTO getObjective(@PathVariable("id") @NotNull Long objectiveId) {
        return okrService.getObjectiveById(objectiveId);

    }

    @GetMapping("/{id}/children")
    @ApiOperation("获取okr的children")
    public ObjectiveChildrenDTO listChildren(@PathVariable("id") @NotNull Long objectiveId) {
        return okrService.listChildrenObjectives(objectiveId);

    }

    @PutMapping("/{id}/close")
    @ApiOperation("关闭指定的objective")
    public void closeObjective(@PathVariable("id") @NotNull Long objectiveId, @RequestBody @Valid CloseObjectiveRequestDTO closeObjectiveRequestDTO) {
        okrService.closeObjective(objectiveId, closeObjectiveRequestDTO);
    }

    @PutMapping("/{id}/reopen")
    @ApiOperation("重新打开object")
    public void reOpenObjective(@PathVariable("id") @NotNull Long objectiveId) {
        okrService.reOpenObjective(objectiveId);
    }

    @PutMapping("/{id}/link")
    @ApiOperation("将当前objective关联到指定的objective")
    public void linkedObjective(@PathVariable("id") @NotNull Long objectiveId, @RequestBody @NotNull LinkedObjectiveDTO linkedObjectiveDTO) {
        okrService.linkedParentObjective(objectiveId, linkedObjectiveDTO.getLinkedObjectiveId());
    }

    @PutMapping("/{id}/unlink")
    @ApiOperation("解除当前objective的关联关系")
    public void unLinkedObjective(@PathVariable("id") @NotNull Long objectiveId) {
        okrService.unLinkedParentObjective(objectiveId);
    }

    @GetMapping("/{id}/timeline")
    @ApiOperation("获取objective的timeline")
    public PageInfo<ObjectiveTimelineDTO> listObjectiveTimeline(@PathVariable("id") @NotNull Long objectiveId, Integer pageNumber, Integer pageSize) {
        ListObjectiveTimeLineQueryDTO listObjectiveTimeLineQueryDTO = new ListObjectiveTimeLineQueryDTO();
        listObjectiveTimeLineQueryDTO.setObjectiveId(objectiveId);
        listObjectiveTimeLineQueryDTO.setPageNumber(pageNumber);
        listObjectiveTimeLineQueryDTO.setPageSize(pageSize);
        return okrService.listObjectiveOperateTimeline(listObjectiveTimeLineQueryDTO);
    }

    @GetMapping("/{id}/key_results")
    @ApiOperation("分页获取指定objective的keyresult列表")
    public PageInfo<ObjectiveKeyResultDTO> listObjectiveKeyResult(@PathVariable("id") @NotNull Long objectiveId, Integer pageNumber, Integer pageSize) {
        ListObjectiveKeyResultPageRequestDTO listObjectiveKeyResultPageRequest = new ListObjectiveKeyResultPageRequestDTO();
        listObjectiveKeyResultPageRequest.setObjectiveId(objectiveId);
        listObjectiveKeyResultPageRequest.setPageNumber(pageNumber);
        listObjectiveKeyResultPageRequest.setPageSize(pageSize);
        return okrService.listKeyResultsForObjective(listObjectiveKeyResultPageRequest);
    }

    @GetMapping("/{id}/weekly_plan")
    @ApiOperation("获取Objective绑定的weeklyPlan")
    public WeeklyPlanSingleObjectiveDTO listObjectiveWeeklyPlan(@PathVariable("id") @NotNull Long objectiveId, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformSingleObjectiveView(okrService.getObjectvieCombineWeeklyPlan(objectiveId, week),objectiveId);
    }

    @GetMapping("/report")
    @ApiOperation("获取所有objective的统计信息")
    public ObjectiveCountReportDTO getObjectiveTotalReport(Long organizationId, Date start, Date end) {
        return okrService.analysisOrgObjectiveTotalReport(start, end);

    }

    @Autowired
    private OkrService okrService;

}
