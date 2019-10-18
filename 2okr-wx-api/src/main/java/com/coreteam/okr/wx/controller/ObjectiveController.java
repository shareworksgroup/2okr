package com.coreteam.okr.wx.controller;

import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.service.OkrService;
import com.coreteam.okr.service.UserService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ObjectiveController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 11:35
 * @Version 1.0.0
 */
@RestController
@RequestMapping("objectives")
@AuditLogAnnotation(value = "objective接口")
@Slf4j
public class ObjectiveController {

    @Autowired
    private OkrService okrService;

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation("添加objective")
    public Long insertObjective(@RequestBody @Valid InsertObjectiveDTO insertObjectiveDTO) {
        return okrService.insertObjective(insertObjectiveDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取指定的objective")
    public ObjectiveDTO getObjective(@PathVariable("id") @NotNull Long objectiveId) {
        return okrService.getObjectiveById(objectiveId);

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

    @GetMapping("/list/user")
    @ApiOperation("获取user拥有的objective列表")
    public List<ObjectiveDTO> listMemberLevelObjectiveKeyResult(@NotNull Long organizationId, Date start, Date end) {
        PageInfo<ObjectiveDTO> page = this.userService.listMemberLevelObjectives(organizationId, 1, 0, start, end);
        return page.getList();
    }

    @GetMapping("/list/organization")
    @ApiOperation("获取组织所有的okr")
    public ObjectiveStaticAndListGroupByLevelDTO listOrganizationObjectiveStatisticsAndListGroupByLevel(ObjectiveStaticAndListQueryDTO query) {
        return userService.analysisObjectiveStatisticsAndListGroupByLevel(query);
    }

    @GetMapping("/list/linkable")
    @ApiOperation("获取user可供链接的objective列表")
    public List<ObjectiveDTO> listOrgObjectiveLinkable(Long organizationId) {
        return this.userService.listObjectivesAble(organizationId);
    }


}
