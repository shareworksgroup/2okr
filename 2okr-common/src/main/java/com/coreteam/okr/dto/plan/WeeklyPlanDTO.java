package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeeklyPlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:35
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanDTO {

    private Long id;

    /**
     * year+week
     */
    private Integer week;

    /**
     * 自我评分1-5
     */
    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;

    private Long organizationId;

    private List<WeeklyPlanItemDTO> items;

    private WeeklyPlanTemplateDTO template;



   /* public WeeklyPlanCategoryDTO transformCategoryView() {
        WeeklyPlanCategoryDTO dto = new WeeklyPlanCategoryDTO();
        BeanConvertUtils.copyEntityProperties(this,dto);
        List<WeeklyPlanItemViewDTO> planItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> doneItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> problemItems=new ArrayList<>();
        if(items!=null){
            items.forEach( weeklyPlanItemDTO -> {
                WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                List<ObjectiveNotConbineKRDTO> objectives=new ArrayList<>();
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null){
                    objectives=BeanConvertUtils.batchTransform(ObjectiveNotConbineKRDTO.class,weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                }
                itemViewDTO.setLinkedObjectives(objectives);
                switch (itemViewDTO.getStatus()){
                    case PLAN:
                        planItems.add(itemViewDTO);
                        break;
                    case DONE:
                        doneItems.add(itemViewDTO);
                        break;
                    case PROBLEM:
                        problemItems.add(itemViewDTO);
                        break;
                }
            });
        }
        dto.setPlanItems(planItems);
        dto.setDoneItems(doneItems);
        dto.setProblemItems(problemItems);
        return dto;
    }

    public WeeklyPlanObjectiveDTO transformObjectiveView(){
        WeeklyPlanObjectiveDTO dto= new WeeklyPlanObjectiveDTO();
        BeanConvertUtils.copyEntityProperties(this,dto);

        List<WeeklyPlanItemViewDTO> planItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> doneItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> problemItems=new ArrayList<>();
        List<ObjectiveConbineWeeklyPlanDTO> okrItems=new ArrayList<>();
        List<ObjectiveDTO> objectiveList=new ArrayList<>();
        Map<Long,ObjectiveConbineWeeklyPlanDTO> objectivePlanItemMap=new HashMap<>();



        if(items!=null&&items.size()>0){
            //获取所有的objectives
            items.forEach(weeklyPlanItemDTO ->{
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null||weeklyPlanItemDTO.getPlan().getLinkedObjectives().size()>0){
                    objectiveList.addAll(weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                }
            });
            //objective去重
            if(objectiveList.size()>0){
                Set<Long> objectiveIdSet=new HashSet<>();
                new ArrayList<>(objectiveList).forEach(objectiveDTO -> {
                    if(objectiveIdSet.contains(objectiveDTO.getId())){
                        objectiveList.remove(objectiveDTO);
                    }else{
                        objectiveIdSet.add(objectiveDTO.getId());
                        //创建objectiveConbineWeeklyPlanDTO 和map映射
                        ObjectiveConbineWeeklyPlanDTO objectiveConbineWeeklyPlanDTO=new ObjectiveConbineWeeklyPlanDTO();
                        BeanConvertUtils.copyEntityProperties(objectiveDTO,objectiveConbineWeeklyPlanDTO);
                        List<WeeklyPlanItemViewDTO> plan=new ArrayList<>();
                        List<WeeklyPlanItemViewDTO> done=new ArrayList<>();
                        List<WeeklyPlanItemViewDTO> problem=new ArrayList<>();
                        objectiveConbineWeeklyPlanDTO.setPlanItems(plan);
                        objectiveConbineWeeklyPlanDTO.setDoneItems(done);
                        objectiveConbineWeeklyPlanDTO.setProblemItems(problem);
                        objectivePlanItemMap.put(objectiveDTO.getId(),objectiveConbineWeeklyPlanDTO);
                        okrItems.add(objectiveConbineWeeklyPlanDTO);
                        //移除
                        objectiveList.remove(objectiveDTO);
                    }
                });
            }

            items.forEach( weeklyPlanItemDTO -> {
                WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                List<ObjectiveNotConbineKRDTO> objectives=new ArrayList<>();
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null&&weeklyPlanItemDTO.getPlan().getLinkedObjectives().size()>0){
                    objectives=BeanConvertUtils.batchTransform(ObjectiveNotConbineKRDTO.class,weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                    itemViewDTO.setLinkedObjectives(objectives);
                    objectives.forEach(objectiveItem->{
                        switch (itemViewDTO.getStatus()){
                            case PLAN:
                                objectivePlanItemMap.get(objectiveItem.getId()).getPlanItems().add(itemViewDTO);
                                break;
                            case DONE:
                                objectivePlanItemMap.get(objectiveItem.getId()).getDoneItems().add(itemViewDTO);
                                break;
                            case PROBLEM:
                                objectivePlanItemMap.get(objectiveItem.getId()).getProblemItems().add(itemViewDTO);
                                break;
                        }
                    });
                }else{
                    itemViewDTO.setLinkedObjectives(objectives);
                    switch (itemViewDTO.getStatus()){
                        case PLAN:
                            planItems.add(itemViewDTO);
                            break;
                        case DONE:
                            doneItems.add(itemViewDTO);
                            break;
                        case PROBLEM:
                            problemItems.add(itemViewDTO);
                            break;
                    }
                }
            });
        }
        dto.setDoneItemsUnlinked(doneItems);
        dto.setPlanItemsUnlinked(planItems);
        dto.setProblemItemsUnlinked(problemItems);
        dto.setOkrItems(okrItems);
        return dto;
    }

    public WeeklyPlanReportUserDTO transformReportByUserView(){
        WeeklyPlanReportUserDTO dto = new WeeklyPlanReportUserDTO();
        BeanConvertUtils.copyEntityProperties(this,dto);
        dto.setWeeklyPlanId(id);
        List<WeeklyPlanItemViewDTO> planItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> doneItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> problemItems=new ArrayList<>();
        if(items!=null){
            items.forEach( weeklyPlanItemDTO -> {
                WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                List<ObjectiveNotConbineKRDTO> objectives=new ArrayList<>();
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null){
                    objectives=BeanConvertUtils.batchTransform(ObjectiveNotConbineKRDTO.class,weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                }
                itemViewDTO.setLinkedObjectives(objectives);
                switch (itemViewDTO.getStatus()){
                    case PLAN:
                        planItems.add(itemViewDTO);
                        break;
                    case DONE:
                        doneItems.add(itemViewDTO);
                        break;
                    case PROBLEM:
                        problemItems.add(itemViewDTO);
                        break;
                }
            });
        }
        dto.setPlanItems(planItems);
        dto.setDoneItems(doneItems);
        dto.setProblemItems(problemItems);
        return dto;
    }

    public static WeeklyPlanReportCategoryDTO transformReportByCategory(List<WeeklyPlanDTO> list){
        WeeklyPlanReportCategoryDTO dto=new WeeklyPlanReportCategoryDTO();
        List<WeeklyPlanItemViewDTO> planItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> doneItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> problemItems=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(weeklyPlanDTO -> {
                if(weeklyPlanDTO.getItems()!=null){
                    weeklyPlanDTO.getItems().forEach( weeklyPlanItemDTO -> {
                        WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                        BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                        itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                        itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                        itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                        List<ObjectiveNotConbineKRDTO> objectives=new ArrayList<>();
                        if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null){
                            objectives=BeanConvertUtils.batchTransform(ObjectiveNotConbineKRDTO.class,weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                        }
                        itemViewDTO.setLinkedObjectives(objectives);
                        switch (itemViewDTO.getStatus()){
                            case PLAN:
                                planItems.add(itemViewDTO);
                                break;
                            case DONE:
                                doneItems.add(itemViewDTO);
                                break;
                            case PROBLEM:
                                problemItems.add(itemViewDTO);
                                break;
                        }
                    });
                }
            });
        }
        dto.setDoneItems(doneItems);
        dto.setPlanItems(planItems);
        dto.setProblemItems(problemItems);
        return dto;
    }

    public static WeeklyPlanReportObjectiveDTO transformReportByObjective(List<WeeklyPlanDTO> list) {
        WeeklyPlanReportObjectiveDTO dto=new WeeklyPlanReportObjectiveDTO();
        List<WeeklyPlanItemViewDTO> planItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> doneItems=new ArrayList<>();
        List<WeeklyPlanItemViewDTO> problemItems=new ArrayList<>();
        List<ObjectiveConbineWeeklyPlanDTO> okrItems=new ArrayList<>();
        List<ObjectiveDTO> objectiveList=new ArrayList<>();
        Map<Long,ObjectiveConbineWeeklyPlanDTO> objectivePlanItemMap=new HashMap<>();
        List<WeeklyPlanItemDTO> items=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(weeklyPlanDTO -> {
                if(weeklyPlanDTO.getItems()!=null){
                    items.addAll(weeklyPlanDTO.getItems());
                }
            });
        }
        if(items!=null&&items.size()>0){
            //获取所有的objectives
            items.forEach(weeklyPlanItemDTO ->{
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null||weeklyPlanItemDTO.getPlan().getLinkedObjectives().size()>0){
                    objectiveList.addAll(weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                }
            });
            //objective去重
            if(objectiveList.size()>0){
                Set<Long> objectiveIdSet=new HashSet<>();
                new ArrayList<>(objectiveList).forEach(objectiveDTO -> {
                    if(objectiveIdSet.contains(objectiveDTO.getId())){
                        objectiveList.remove(objectiveDTO);
                    }else{
                        //创建objectiveConbineWeeklyPlanDTO 和map映射
                        objectiveIdSet.add(objectiveDTO.getId());
                        ObjectiveConbineWeeklyPlanDTO objectiveConbineWeeklyPlanDTO=new ObjectiveConbineWeeklyPlanDTO();
                        BeanConvertUtils.copyEntityProperties(objectiveDTO,objectiveConbineWeeklyPlanDTO);
                        List<WeeklyPlanItemViewDTO> plan=new ArrayList<>();
                        List<WeeklyPlanItemViewDTO> done=new ArrayList<>();
                        List<WeeklyPlanItemViewDTO> problem=new ArrayList<>();
                        objectiveConbineWeeklyPlanDTO.setPlanItems(plan);
                        objectiveConbineWeeklyPlanDTO.setDoneItems(done);
                        objectiveConbineWeeklyPlanDTO.setProblemItems(problem);
                        objectivePlanItemMap.put(objectiveDTO.getId(),objectiveConbineWeeklyPlanDTO);
                        okrItems.add(objectiveConbineWeeklyPlanDTO);
                        //移除
                        objectiveList.remove(objectiveDTO);
                    }
                });
            }

            items.forEach( weeklyPlanItemDTO -> {
                WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                List<ObjectiveNotConbineKRDTO> objectives=new ArrayList<>();
                if(weeklyPlanItemDTO.getPlan().getLinkedObjectives()!=null&&weeklyPlanItemDTO.getPlan().getLinkedObjectives().size()>0){
                    objectives=BeanConvertUtils.batchTransform(ObjectiveNotConbineKRDTO.class,weeklyPlanItemDTO.getPlan().getLinkedObjectives());
                    itemViewDTO.setLinkedObjectives(objectives);
                    objectives.forEach(objectiveItem->{
                        switch (itemViewDTO.getStatus()){
                            case PLAN:
                                objectivePlanItemMap.get(objectiveItem.getId()).getPlanItems().add(itemViewDTO);
                                break;
                            case DONE:
                                objectivePlanItemMap.get(objectiveItem.getId()).getDoneItems().add(itemViewDTO);
                                break;
                            case PROBLEM:
                                objectivePlanItemMap.get(objectiveItem.getId()).getProblemItems().add(itemViewDTO);
                                break;
                        }
                    });
                }else{
                    itemViewDTO.setLinkedObjectives(objectives);
                    switch (itemViewDTO.getStatus()){
                        case PLAN:
                            planItems.add(itemViewDTO);
                            break;
                        case DONE:
                            doneItems.add(itemViewDTO);
                            break;
                        case PROBLEM:
                            problemItems.add(itemViewDTO);
                            break;
                    }
                }
            });
        }
        dto.setDoneItemsUnlinked(doneItems);
        dto.setPlanItemsUnlinked(planItems);
        dto.setProblemItemsUnlinked(problemItems);
        dto.setOkrItems(okrItems);
        return dto;
    }*/

}
