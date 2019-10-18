package com.coreteam.okr.service;

import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.objective.PlanLinkedObjectiveDTO;
import com.coreteam.okr.dto.plan.*;

import java.util.List;

/**
 * @ClassName: WeeklyPlanService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:25
 * @Version 1.0.0
 */
public interface WeeklyPlanService {
    /**
     * list 用户 week plan
     * @param query
     * @return
     */
    WeeklyPlanDTO listUserWeeklyPlanByOrg(ListWeeklyPlanDTO query);

    /**
     * fa
     */


    /**
     * 添加 week plan item
     * @param weeklyPlanId
     * @param dto
     */
    void insertWeeklyPlanItem(Long weeklyPlanId, InsertWeelyPlanItemDTO dto);

    /**
     * 更新week plan的信息
     * @param id
     * @param updateWeeklyPlanDTO
     */
    void updateWeeklyPlan(Long id, UpdateWeeklyPlanDTO updateWeeklyPlanDTO);

    /**
     *  gengxin week plan item和plan的信息
     * @param id
     * @param dto
     */
    void updateWeeklyPlanItemAndPlan(Long id, UpdateWeelyPlanItemDTO dto);

    /**
     * delete week plan item and plam
     * @param id
     * @param deletePlan
     */
    void deleteWeeklyPlanItemAndPlan(Long id, Boolean deletePlan);

    /**
     * list weekly plan time line
     * @param id
     * @return
     */
    List<WeeklyPlanTimeLineDTO> listWeeklyPlanTimeLine(Long id);


    /**
     *增加 plan comment
     * @param id
     * @param comment
     */
    void insertPlanComment(Long id, String comment);

    /**
     * 更新 comment plan
     * @param id
     * @param comment
     */
    void updatePlanComment(Long id, String comment);

    /**
     * list comment plan
     * @param planId
     * @return
     */
    List<PlanCommentDTO> listPlanComments(Long planId);

    /**
     * delete comment
     * @param id
     */
    void deletePlanComment(Long id);

    /**
     * linked object
     * @param id
     * @param objectiveId
     */
    void linkedPlanToObjective(Long id, Long objectiveId);


    /**
     *
     * @param id
     * @param objectives
     */
    void updatePlanObjectiveLinks(Long id, PlanLinkedObjectiveDTO objectives);

    /**
     * unlink object
     * @param id
     * @param objectiveId
     */
    void unlinkedPlanToObjective(Long id, Long objectiveId);

    /**
     * send weekly plan report
     * @param id
     * @param reportReciverDTO
     */
    void reportWeeklyPlan(Long id, WeeklyPlanReportReciverDTO reportReciverDTO);

    /**
     * cancel weekly plan report
     * @param id
     * @param reciverId
     */
    void deletetWeeklyPlanReport(Long id, Long reciverId);



    /**
     *  list team weekly plan report
     *
     * @param entityId
     * @param userId
     * @param week
     * @return
     */
    WeekplayReportTranFormDTO listOrgWeeklyPlanReport(Long entityId, Long userId, Integer week);

    /**
     * 获取组织 weeklu plan report sumary
     * @param id
     * @param week
     * @return
     */
    List<WeeklyPlanReportSummaryDTO> listOrgWeeklyPlanReportSummary(Long id, Integer week);

    /**
     * 记录weekly plan 当天的状态
     */
    void recordWeeklyPlanDalilyStatus();

    void sendOrgWeeklyPlanReport(SendOrgWeeklyPlanReportDTO sendInfo);

    /**
     * weekly 的report
     * @param info
     * @return
     */
    ObjectiveWeeklyPlanRegularEmailReportDTO getObjectiveWeeklyPlanRegularReport(String info);

    /**
     *
     * @param organizationId
     * @param id
     * @param reciverIds
     */
    void autoSendThisWeeklyPlanReportToLeader(Long organizationId, Long id, List<Long> reciverIds);
}
