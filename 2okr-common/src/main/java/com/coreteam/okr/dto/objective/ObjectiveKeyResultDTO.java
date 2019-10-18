package com.coreteam.okr.dto.objective;

import com.coreteam.okr.dto.Authorization.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ObjectiveKeyResultDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 15:23
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveKeyResultDTO {
    private Long id;

    /**
     * the key result's name
     */
    private String name;

    private String desc;

    private String metricUnit;

    private Double metricStartValue;

    private Double metricEndValue;

    private Double weight;

    private Long ownerId;

    private String ownerName;

    private Double value;

    private Double progress;

    private Double lastProgress;

    private Long objectiveId;

    private Long updateUserId;

    private String updateUserName;

    private Date updatedAt;

    private Long keyResultCommentsNum;

    private Privilege privilege;

    private Integer confidenceLevel;

    private Double midTermRating;

    private Double finalRating;

    private String summary;

    private List<ObjectiveKeyResultCommentDTO> keyResultComments;
}
