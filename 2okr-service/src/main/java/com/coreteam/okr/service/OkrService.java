package com.coreteam.okr.service;

import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.plan.WeeklyPlanDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanSingleObjectiveDTO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description Objective manager service
 * @Author jianyong.jiang
 * @Date 2019/03/19
 * @Version 1.0.0
 */
public interface OkrService {

    /*=========================================Objective base crud ===================================================*/

    /**
     * 　* @description: create a new objective
     * 　* @return the id of the new objective
     * 　* @date 2019/3/19 9:55
     */
    long insertObjective(InsertObjectiveDTO createObjectiveRequestDTO);

    /**
     * update objective
     *
     * @param updateObjectiveDTO
     * @return
     */
    boolean updateObjective(UpdateObjectiveDTO updateObjectiveDTO);

    /**
     * 更新Objective中影响进度状态信息的属性
     *
     * @param updateObjectiveDTO
     */
    void updateObjectiveStatus(UpdateObjectiveStatusDTO updateObjectiveDTO);

    /**
     * update objective progress
     *
     * @param objectiveDTO
     */
    void updateObjectiveProgress(ObjectiveDTO objectiveDTO);

    /**
     * delete objective
     *
     * @param id
     * @return
     */
    boolean deleteObjective(Long id);

    /**
     * get objective with key result
     *
     * @param id
     * @return
     */
    ObjectiveDTO getObjectiveById(Long id);

    /**
     * close object and score
     * @param objectiveId
     * @param requestDTO
     * @return
     */
    boolean closeObjective(Long objectiveId, CloseObjectiveRequestDTO requestDTO);

    /**
     * reopen object
     * @param id
     * @return
     */
    boolean reOpenObjective(Long id);

    /*=========================================Objective aliment operate ===================================================*/

    /**
     * link  parent objective
     * @param objectiveId
     * @param linkedObjectiveId
     */
    void linkedParentObjective(Long objectiveId, Long linkedObjectiveId);

    /**
     * unlink objective
     * @param objectiveId
     */
    void unLinkedParentObjective(Long objectiveId);

    /**
     * list objective children
     * @param objectiveId
     * @return
     */
    ObjectiveChildrenDTO listChildrenObjectives(Long objectiveId);

    /**
     * 获取可供linked的objective list
     * @param userId
     * @param organizationId
     * @return
     */
    List<ObjectiveDTO> listObjectiveLinkable(Long userId, Long organizationId);

    /**
     * 获取可供linked的objective tree
     * @param userId
     * @param organizationId
     * @return
     */
    List<ObjectiveTreeNodeDTO> listObjectiveTreeViewLinkable(Long userId, Long organizationId);

    /**
     * 获取可以alignment的objective
     * @param organizationId
     * @param level
     * @return
     */
    AlignmentObjectiveDTO listObjectiveAlignment(Long organizationId, ListUserObjectiveAlignmentDTO level);




    /*=========================================Objective complex query ===================================================*/
    /**
     * 按照不同的Level（org，team，member）list Objective
     * @param queryDTO
     * @return
     */
    PageInfo<ObjectiveDTO> listObjectiveByLevelFilter(ListObjectiveDTO queryDTO);

    /**
     * 按照不同的Level，分组统计分析所有的Objective
     * @param query
     * @return
     */
    ObjectiveStaticAndListGroupByLevelDTO analysisObjectiveStatisticsAndListByLevel(ObjectiveStaticAndListQueryDTO query);

    /**
     * 获取所有object的tree视图
     * @param query
     * @return
     */
    List<ObjectiveTreeNodeDTO> listObjectiveTreeView(ObjectiveStaticAndListQueryDTO query);



    /**
     * list objective的key-result信息
     * @param request
     * @return
     */
    PageInfo<ObjectiveKeyResultDTO> listKeyResultsForObjective(ListObjectiveKeyResultPageRequestDTO request);


    /**
     * list objective operate time line
     *
     * @param queryDTO
     * @return
     */
    PageInfo<ObjectiveTimelineDTO> listObjectiveOperateTimeline(ListObjectiveTimeLineQueryDTO queryDTO);



    /**
     * 按照不同的Level（org，team，member）,统计objective的不同状态的数量和list over time Objective不同状态的数量
     *
     * @param queryDTO
     * @return
     */
    ObjectiveStatisticsDTO countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(CountObjectiveByStatusDTO queryDTO);

    /**
     * 准备okr report的数据
     * @param query
     * @param filters
     * @return
     */
    ObjectiveWeeklyPlanRegularEmailReportDTO prepareDataForOrgOKRRegularReport(GetOrgObjectiveRegularReportDTO query, Set<Long> filters);

    /**
     * list plan绑定的Objective
     *
     * @param id
     * @return
     */
    List<ObjectiveDTO> listObjectiveWhichWeeklyPlanLinked(Long id);

    /**
     * 统计组织objective的数量
     * @param id
     * @return
     */
    Integer sumOrganizationObjective(Long id);

    /**
     * @param start
     * @param end
     * @return
     */
    ObjectiveCountReportDTO analysisOrgObjectiveTotalReport(Date start, Date end);

    /**
     * 获取Objective绑定的weeklyplan
     * @param objectiveId
     * @param week
     * @return
     */
    WeeklyPlanDTO getObjectvieCombineWeeklyPlan(Long objectiveId, Integer week);


    /*=========================================Objective job schedule ===================================================*/

    /**
     * 更新记录昨天的objecive的状态
     */
    void updateObjectivePorgressDaily();

    /**
     * 添加 off tract的Objective 的系统提醒
     */
    void recordOffTractObjectiveUpdateRemind();

    /**
     * 记录objective每周对应的进度
     */
    void recordObjectiveLaskWeekProgress();


    /**
     * @param objectiveId
     * @param typeEnum
     * @return
     */
    Boolean hasPermission(Long objectiveId, PrivilegeTypeEnum typeEnum);

}
