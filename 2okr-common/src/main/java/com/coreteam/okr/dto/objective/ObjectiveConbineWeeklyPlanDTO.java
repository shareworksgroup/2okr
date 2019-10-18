package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.coreteam.okr.dto.plan.WeeklyPlanItemViewDTO;
import com.coreteam.okr.dto.plan.WeeklyTemplateCategoryInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveConbineWeeklyPlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 14:20
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveConbineWeeklyPlanDTO {

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

}
