package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.entity.Objective;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * ObjectiveDAO继承基类
 */
@Mapper
public interface ObjectiveDAO extends MyBatisBaseDao<Objective, Long> {
    /**
     * 统计组织okr的数量
     * @param id
     * @return
     */
    Integer countOrganizationObjective(Long id);

    /**
     * lsit member team org的objective
     * @param queryDTO
     * @return
     */
    List<Objective> listObjective(ListObjectiveDTO queryDTO);

    /**
     * list objective children
     * @param id
     * @return
     */
    List<Objective> listChildren(Long id);

    /**
     * 统计 member team org objective中不同status的数量
     * @param queryDTO
     * @return
     */
    ObjectiveStatisticsDTO countObjectiveByStatus(CountObjectiveByStatusDTO queryDTO);

    /**
     * 统计 member team org objective的 overtime
     * @param queryDTO
     * @return
     */
    List<ObjectiveProgressOverTimeDTO> countObjectiveOverTimeProgressByStatus(CountObjectiveByStatusDTO queryDTO);

    /**
     * list 用户所有的objective信息
     * @param query
     * @return
     */
    List<Objective> listAllObjectives(GetUserObjectiveReportDTO query);

    /**
     * 按照查询条件获取Objective的信息
     * @param query
     * @return
     */
    List<Objective> listObjectivesByFilter(ObjectiveStaticAndListQueryDTO query);

    /**
     * 获取objective的报告
     * @param start
     * @param end
     * @return
     */
    ObjectiveCountReportDTO getTotalObjectiveReport(@Param("start") Date start, @Param("end") Date end);

    /**
     * 根据plan查询objective
     * @param id
     * @return
     */
    List<Objective> listObjectiveByPlan(Long id);

    /**
     * 获取未close的的objective
     * @param now
     * @return
     */
    List<Objective> listOpenObjective(@Param("end") Date now);


    /**
     * 获取用户未关闭的objective
     *
     * @param now
     * @return
     */
    List<Objective> listOpenObjectiveByUser(@Param("userId") Long userId, @Param("organizationId") Long organizationId, @Param("end") Date now);

    /**
     *
     * @param organizationId
     * @param type
     * @return
     */
    List<Objective> listOrgObjectiveByLevel(@Param("organizationId") Long organizationId, @Param("level") String type);

    void updateOwnerName(@Param("userId") Long userId, @Param("userName") String userName);

    List<Objective> listOffTrackObjective();

}