package com.coreteam.okr.web.controller;

import com.coreteam.okr.common.util.DateUtil;
import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.objective.PlanLinkedObjectiveDTO;
import com.coreteam.okr.dto.plan.*;
import com.coreteam.okr.service.WeeklyPlanService;
import com.coreteam.okr.util.WeeklyPlanViewRender;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: WeeklyPlanController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:28
 * @Version 1.0.0
 */
@RestController
@RequestMapping("weeklyplans")
@AuditLogAnnotation(value = "weeklyplan接口")
@Slf4j
public class WeeklyPlanController {

    @PutMapping("/{id}")
    @ApiOperation("更新weeklyPlan的状态,提交用户weeklyplan的自评")
    public void updateWeeklyPlanStatus(@PathVariable("id") @NotNull Long id,@RequestBody @Valid UpdateWeeklyPlanDTO updateWeeklyPlanStatusDTO){
        this.weeklyPlanService.updateWeeklyPlan(id,updateWeeklyPlanStatusDTO);
    }

    @GetMapping("/week")
    @ApiOperation("获取一周的起止时间,week要求格式为201902")
    public WeeklyTimeDTO transFromWeekToTime(Integer week){
        WeeklyTimeDTO dto=new WeeklyTimeDTO();
        dto.setWeek(week);
        dto.setStart(DateUtil.getFirstDateOfWeek(week));
        dto.setEnd(DateUtil.getEndDateOfWeek(week));
        return dto;

    }

    @GetMapping("/report/view/objective_type")
    @ApiOperation("获取weeklyplan的objective视图")
    public ObjectiveWeeklyPlanRegularReportObjectiveViewDTO getObjectiveWeeklyPlanRegularReportObjectiveView(String info){
        ObjectiveWeeklyPlanRegularEmailReportDTO result=this.weeklyPlanService.getObjectiveWeeklyPlanRegularReport(info);
        return WeeklyPlanViewRender.reportTransform2ObjectiveView(result);
    }

    @GetMapping("/report/view/category_type")
    @ApiOperation("获取weeklyplan的category视图")
    public ObjectiveWeeklyPlanRegularReportCategoryViewDTO getObjectiveWeeklyPlanRegularReportCategoryViewDTO(String info){
        ObjectiveWeeklyPlanRegularEmailReportDTO result=this.weeklyPlanService.getObjectiveWeeklyPlanRegularReport(info);
        return WeeklyPlanViewRender.reportTransform2CategoryView(result);
    }


    @GetMapping("/report/view/user_type")
    @ApiOperation("获取weeklyplan的user_type视图")
    public ObjectiveWeeklyPlanRegularReportUserDTO getObjectiveWeeklyPlanRegularReportUserDTO(String info){
        ObjectiveWeeklyPlanRegularEmailReportDTO result=this.weeklyPlanService.getObjectiveWeeklyPlanRegularReport(info);
        return WeeklyPlanViewRender.reportTransform2UserView(result);
    }

    @PostMapping("/{id}/report")
    @ApiOperation("发送weeklyplan report")
    public void reportWeeklyPlan(@PathVariable("id") @NotNull Long id,@RequestBody @Valid WeeklyPlanReportReciverDTO reportReciverDTO){
        this.weeklyPlanService.reportWeeklyPlan(id,reportReciverDTO);
    }

    @DeleteMapping("/{id}/report")
    @ApiOperation("撤销weeklyplan report")
    public void deleteWeeklyPlanReport(@PathVariable("id") @NotNull Long id,Long reciverId){
        this.weeklyPlanService.deletetWeeklyPlanReport(id,reciverId);
    }

    @GetMapping("/{id}/timeline")
    @ApiOperation("获取周计划的time line")
    public List<WeeklyPlanTimeLineDTO> listWeeklyPlanTimeLine(@PathVariable("id") @NotNull Long id){
        return this.weeklyPlanService.listWeeklyPlanTimeLine(id);
    }

    @PostMapping("/{id}/item")
    @ApiOperation("新增weeklyplan item")
    public void insertWeeklyPlanItem(@PathVariable("id") @NotNull Long id, @RequestBody @Valid InsertWeelyPlanItemDTO dto){
        this.weeklyPlanService.insertWeeklyPlanItem(id,dto);
    }
    @PutMapping("/item/{id}")
    @ApiOperation("更新weeklyplan item的信息，color，status，desc，duedate等")
    public void updateWeeklyPlanItem(@PathVariable("id") @NotNull Long id,@RequestBody @Valid UpdateWeelyPlanItemDTO dto){
        this.weeklyPlanService.updateWeeklyPlanItemAndPlan(id,dto);
    }

    @DeleteMapping("/item/{id}")
    @ApiOperation("移除weekplay item，删除plan")
    public void deleteWeeklyPlanItem(@PathVariable("id") @NotNull Long id,@RequestParam(defaultValue = "false",required = false) Boolean deletePlan){
        this.weeklyPlanService.deleteWeeklyPlanItemAndPlan(id,deletePlan);
    }

    @PostMapping("/item/{id}/comment")
    @ApiOperation("增加plan评论")
    public void insertPlanComment(@PathVariable("id") @NotNull Long planId,@RequestBody @Valid EditPlanCommentDTO comment){
        this.weeklyPlanService.insertPlanComment(planId,comment.getComments());
    }

    @GetMapping("/item/{id}/comments")
    @ApiOperation("list item 所有的comment")
    public List<PlanCommentDTO> listPlanComment(@PathVariable("id") @NotNull Long id){
        return this.weeklyPlanService.listPlanComments(id);
    }

    @PutMapping("/item/comment/{id}")
    @ApiOperation("更新plan的comment")
    public void updatePlanComment(@PathVariable("id") @NotNull Long id,@RequestBody @Valid EditPlanCommentDTO comment){
        this.weeklyPlanService.updatePlanComment(id,comment.getComments());
    }

    @DeleteMapping("/item/comment/{id}")
    @ApiOperation("delete plan comment")
    public void deletePlanComment(@PathVariable("id") @NotNull Long id){
        this.weeklyPlanService.deletePlanComment(id);
    }

    @PostMapping("/item/{id}/objective")
    @ApiOperation("关联objective到plan")
    public void linkPlanObjective(@PathVariable("id") @NotNull Long id, @RequestBody @Valid PlanLinkedObjectiveDTO objectiveDTO){
        this.weeklyPlanService.updatePlanObjectiveLinks(id,objectiveDTO);
    }

    @DeleteMapping("/plan/{id}/objective")
    @ApiOperation("解除objective和plan的关联")
    public void unlinkedPlanObjective(@PathVariable("id") @NotNull Long id,Long objectiveId){
        this.weeklyPlanService.unlinkedPlanToObjective(id,objectiveId);
    }

    @Autowired
    private WeeklyPlanService weeklyPlanService;


}
