package com.coreteam.okr.util;

import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.plan.*;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateItemDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: WeeklyPlanViewRender
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 16:59
 * @Version 1.0.0
 */
public class WeeklyPlanViewRender {
    public static WeeklyPlanCategoryDTO transformCategoryView(WeeklyPlanDTO data) {
        WeeklyPlanCategoryDTO dto = new WeeklyPlanCategoryDTO();
        BeanConvertUtils.copyEntityProperties(data,dto);
        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        Map<String,List<WeeklyPlanItemViewDTO>> dataItems=new HashMap<>();
        categoryList.forEach(categoryItem->{
            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
            dataItems.put(categoryItem.getName(),list);
        });
        dto.setCategorieList(categoryList);
        dto.setItemMap(dataItems);
        
        List<WeeklyPlanItemDTO> items=data.getItems();
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
                List<WeeklyPlanItemViewDTO> list = dataItems.get(categorieMap.get(itemViewDTO.getCategorieId()));
                if(list!=null){
                    list.add(itemViewDTO);
                }
            });
        }
        return dto;
    }

    public static  WeeklyPlanObjectiveDTO transformObjectiveView(WeeklyPlanDTO data){
        WeeklyPlanObjectiveDTO dto= new WeeklyPlanObjectiveDTO();
        BeanConvertUtils.copyEntityProperties(data,dto);
        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap=new HashMap<>();

        categoryList.forEach(categorieItem->{
            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
            unLinkItemMap.put(categorieItem.getName(),list);
        });

        List<ObjectiveConbineWeeklyPlanDTO> okrItems=new ArrayList<>();
        List<ObjectiveDTO> objectiveList=new ArrayList<>();
        Map<Long,ObjectiveConbineWeeklyPlanDTO> objectivePlanItemMap=new HashMap<>();

        dto.setCategorieList(categoryList);
        dto.setUnlinkItemMap(unLinkItemMap);
        dto.setOkrItems(okrItems);



        List<WeeklyPlanItemDTO> items=data.getItems();
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
                        Map<String,List<WeeklyPlanItemViewDTO>> linkOkrPlanMap=new HashMap<>();
                        categoryList.forEach(statusItem->{
                            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
                            linkOkrPlanMap.put(statusItem.getName(),list);
                        });
                        objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                        objectiveConbineWeeklyPlanDTO.setItemMap(linkOkrPlanMap);
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
                        List<WeeklyPlanItemViewDTO> list = objectivePlanItemMap.get(objectiveItem.getId()).getItemMap().get(categorieMap.get(itemViewDTO.getCategorieId()));
                        if(list!=null){
                            list.add(itemViewDTO);
                        }

                    });
                }else{
                    itemViewDTO.setLinkedObjectives(objectives);
                    List<WeeklyPlanItemViewDTO> list = unLinkItemMap.get(categorieMap.get(itemViewDTO.getCategorieId()));
                    if(list!=null){
                        list.add(itemViewDTO);
                    }
                }
            });
        }
        return dto;
    }


    public static WeeklyPlanSingleObjectiveDTO transformSingleObjectiveView(WeeklyPlanDTO data, Long objectiveId) {
        WeeklyPlanSingleObjectiveDTO dto=new WeeklyPlanSingleObjectiveDTO();
        BeanConvertUtils.copyEntityProperties(data,dto);
        List<WeeklyPlanItemDTO> itemsConbineToObjective=null;
        if(!CollectionUtils.isEmpty(data.getItems())){
            itemsConbineToObjective=data.getItems().stream().filter(weeklyPlanItemDTO -> {
                List<ObjectiveDTO> objectives = weeklyPlanItemDTO.getPlan().getLinkedObjectives();
                if(objectives!=null){
                    boolean conbine=false;
                    for (ObjectiveDTO objective:objectives){
                        if(objective.getId().equals(objectiveId)){
                            conbine=true;
                            break;
                        }
                    }
                    return conbine;
                }
                return false;
            }).collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(itemsConbineToObjective)){
            ObjectiveConbineWeeklyPlanDTO weeklyPlans=new ObjectiveConbineWeeklyPlanDTO();
            dto.setWeeklyPlans(weeklyPlans);
            return dto;
        }else{
            ObjectiveConbineWeeklyPlanDTO weeklyPlans=new ObjectiveConbineWeeklyPlanDTO();
            List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
            Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
            final ObjectiveDTO objective=new ObjectiveDTO();
            Map<String,List<WeeklyPlanItemViewDTO>> itemMap =new HashMap<>();
            itemsConbineToObjective.forEach(weeklyPlanItemDTO -> {
                WeeklyPlanItemViewDTO itemViewDTO=new WeeklyPlanItemViewDTO();
                BeanConvertUtils.copyEntityProperties(weeklyPlanItemDTO,itemViewDTO);
                itemViewDTO.setItemId(weeklyPlanItemDTO.getId());
                itemViewDTO.setDesc(weeklyPlanItemDTO.getPlan().getDesc());
                itemViewDTO.setDueDate(weeklyPlanItemDTO.getPlan().getDueDate());
                if(objective.getId()==null){
                    weeklyPlanItemDTO.getPlan().getLinkedObjectives().forEach(objectiveDTO -> {
                        if(objectiveDTO.getId().equals(objectiveId)){
                            BeanConvertUtils.copyEntityProperties(objectiveDTO,objective);
                        }
                    });
                }
                List<WeeklyPlanItemViewDTO> list = itemMap.get(categorieMap.get(weeklyPlanItemDTO.getCategorieId()));
                if(list==null){
                    list=new ArrayList<>();
                    itemMap.put(categorieMap.get(weeklyPlanItemDTO.getCategorieId()),list);
                }
                list.add(itemViewDTO);
            });
            BeanConvertUtils.copyEntityProperties(objective,weeklyPlans);
            weeklyPlans.setCategorieList(categoryList);
            weeklyPlans.setItemMap(itemMap);
            dto.setWeeklyPlans(weeklyPlans);
            return dto;
        }
    }

    public static  Map<String,List<WeeklyPlanReportUserDTO>>  transformReportByUserView(WeekplayReportTranFormDTO data,Map<String,List<Long>> teamUser){
        Map<String,List<WeeklyPlanReportUserDTO>> resultMap=new HashMap();
        List<WeeklyPlanReportUserDTO> result=new ArrayList<>();
        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        if(CollectionUtils.isEmpty(data.getList())){
            return resultMap;
        }
        data.getList().forEach(weeklyPlanDTO -> {
            WeeklyPlanReportUserDTO dto = new WeeklyPlanReportUserDTO();
            BeanConvertUtils.copyEntityProperties(weeklyPlanDTO,dto);
            Map<String,List<WeeklyPlanItemViewDTO>> itemMap=new HashMap<>();
            categoryList.forEach(categoryItem->{
                List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
                itemMap.put(categoryItem.getName(),list);
            });
            dto.setWeeklyPlanId(weeklyPlanDTO.getId());
            dto.setCategorieList(categoryList);
            dto.setItemMap(itemMap);
            List<WeeklyPlanItemDTO> items=weeklyPlanDTO.getItems();
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
                    List<WeeklyPlanItemViewDTO> list= itemMap.get(categorieMap.get(itemViewDTO.getCategorieId()));
                    if(list!=null){
                        list.add(itemViewDTO);
                    }
                });
            }
        result.add(dto);
        });

        Map<Long,Set<String>> userTeamMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(teamUser)){
            teamUser.forEach((k,v)->{
                v.forEach(id->{
                    Set<String> teamSet = userTeamMap.get(id);  
                    if(teamSet==null){
                        teamSet=new HashSet<>();
                        userTeamMap.put(id,teamSet);
                    }
                    teamSet.add(k);
                });
            });
        }

        if(!CollectionUtils.isEmpty(result)){
            for (WeeklyPlanReportUserDTO dto:result) {
                Set<String> teamSet = userTeamMap.get(dto.getOwnerId());
                if(!CollectionUtils.isEmpty(teamSet)){
                    for (String  name:teamSet) {
                        if(!StringUtils.hasText(name)){
                            continue;
                        }
                        List<WeeklyPlanReportUserDTO> list = resultMap.get(name);
                        if(list==null){
                            list=new ArrayList<>();
                            resultMap.put(name,list);
                        }
                        list.add(dto);
                    }
                }else{
                    //加入到others里面
                    List<WeeklyPlanReportUserDTO> list = resultMap.get("others");
                    if(list==null){
                        list=new ArrayList<>();
                        resultMap.put("others",list);
                    }
                    list.add(dto);
                }
            }
        }
        return resultMap;
    }

    public static WeeklyPlanReportCategoryDTO transformReportByCategory(WeekplayReportTranFormDTO data){
        WeeklyPlanReportCategoryDTO dto=new WeeklyPlanReportCategoryDTO();

        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        Map<String,List<WeeklyPlanItemViewDTO>> itemMap=new HashMap<>();
        categoryList.forEach(categorieItem->{
            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
            itemMap.put(categorieItem.getName(),list);
        });
        dto.setCategorieList(categoryList);
        dto.setItemMap(itemMap);

        if(!CollectionUtils.isEmpty(data.getList())){
            data.getList().forEach(weeklyPlanDTO -> {
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
                        List<WeeklyPlanItemViewDTO> list=itemMap.get(categorieMap.get(itemViewDTO.getCategorieId()));
                        if(list!=null){
                            list.add(itemViewDTO);
                        }
                    });
                }
            });
        }

        return dto;
    }

    public static WeeklyPlanReportObjectiveDTO transformReportByObjective(WeekplayReportTranFormDTO data) {
        WeeklyPlanReportObjectiveDTO dto=new WeeklyPlanReportObjectiveDTO();
        if(CollectionUtils.isEmpty(data.getList())){
            return  dto;
        }
        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap=new HashMap<>();

        categoryList.forEach(categorieItem->{
            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
            unLinkItemMap.put(categorieItem.getName(),list);
        });

        List<ObjectiveConbineWeeklyPlanDTO> okrItems=new ArrayList<>();
        dto.setCategorieList(categoryList);
        dto.setUnLinkItemMap(unLinkItemMap);


        List<ObjectiveDTO> objectiveList=new ArrayList<>();
        Map<Long,ObjectiveConbineWeeklyPlanDTO> objectivePlanItemMap=new HashMap<>();
        List<WeeklyPlanItemDTO> items=new ArrayList<>();
        data.getList().forEach(weeklyPlanDTO -> {
            if(weeklyPlanDTO.getItems()!=null){
                items.addAll(weeklyPlanDTO.getItems());
            }
        });

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
                        Map<String,List<WeeklyPlanItemViewDTO>> linkOkrPlanMap=new HashMap<>();
                        categoryList.forEach(statusItem->{
                            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
                            linkOkrPlanMap.put(statusItem.getName(),list);
                        });
                        objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                        objectiveConbineWeeklyPlanDTO.setItemMap(linkOkrPlanMap);
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
                        List<WeeklyPlanItemViewDTO> list=objectivePlanItemMap.get(objectiveItem.getId()).getItemMap().get(categorieMap.get(itemViewDTO.getCategorieId()));
                        if(list!=null){
                            list.add(itemViewDTO);
                        }

                    });
                }else{
                    itemViewDTO.setLinkedObjectives(objectives);
                    List<WeeklyPlanItemViewDTO> list=unLinkItemMap.get(categorieMap.get(itemViewDTO.getCategorieId()));
                    if(list!=null){
                        list.add(itemViewDTO);
                    }
                }
            });
        }

        List<ObjectiveConbineWeeklyPlanDTO> orgOkrs=new ArrayList<>();
        List<ObjectiveConbineWeeklyPlanDTO> teamOkrs=new ArrayList<>();
        List<ObjectiveConbineWeeklyPlanDTO> memberOkrs=new ArrayList<>();
        if(!CollectionUtils.isEmpty(okrItems)){
            okrItems.forEach(okrItem->{
                switch (okrItem.getLevel()){
                    case MEMBER:
                        memberOkrs.add(okrItem);
                        break;
                    case TEAM:
                        teamOkrs.add(okrItem);
                        break;
                    case ORGANIZATION:
                        orgOkrs.add(okrItem);
                        break;
                }
            });
        }
        dto.setOrgOkrs(orgOkrs);
        dto.setTeamOkrs(teamOkrs);
        dto.setMemberOkrs(memberOkrs);
        return dto;
    }

    public static WeeklyPlanReportObjectiveTreeDTO transformReportByObjectiveTree(WeekplayReportTranFormDTO data) {
        WeeklyPlanReportObjectiveTreeDTO dto=new WeeklyPlanReportObjectiveTreeDTO();
        if(CollectionUtils.isEmpty(data.getList())){
            dto.setCategorieList(new ArrayList<>());
            dto.setOkrs(new ArrayList<>());
            dto.setUnLinkItemMap(new HashMap<>());
            return  dto;
        }
        List<WeeklyTemplateCategoryInfoDTO> categoryList= buildSortedWeeklyCategoryList(data.getTemplate());
        Map<Long, String> categorieMap = buildWeeklyCategoryMap(data.getTemplate());
        Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap=new HashMap<>();

        categoryList.forEach(categorieItem->{
            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
            unLinkItemMap.put(categorieItem.getName(),list);
        });

        List<ObjectiveConbineWeeklyPlanDTO> okrItems=new ArrayList<>();
        dto.setCategorieList(categoryList);
        dto.setUnLinkItemMap(unLinkItemMap);


        List<ObjectiveDTO> objectiveList=new ArrayList<>();
        Map<Long,ObjectiveConbineWeeklyPlanDTO> objectivePlanItemMap=new HashMap<>();
        List<WeeklyPlanItemDTO> items=new ArrayList<>();
        data.getList().forEach(weeklyPlanDTO -> {
            if(weeklyPlanDTO.getItems()!=null){
                items.addAll(weeklyPlanDTO.getItems());
            }
        });

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
                        Map<String,List<WeeklyPlanItemViewDTO>> linkOkrPlanMap=new HashMap<>();
                        categoryList.forEach(statusItem->{
                            List<WeeklyPlanItemViewDTO> list=new ArrayList<>();
                            linkOkrPlanMap.put(statusItem.getName(),list);
                        });
                        objectiveConbineWeeklyPlanDTO.setCategorieList(categoryList);
                        objectiveConbineWeeklyPlanDTO.setItemMap(linkOkrPlanMap);
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
                        List<WeeklyPlanItemViewDTO> list=objectivePlanItemMap.get(objectiveItem.getId()).getItemMap().get(categorieMap.get(itemViewDTO.getCategorieId()));
                        if(list!=null){
                            list.add(itemViewDTO);
                        }

                    });
                }else{
                    itemViewDTO.setLinkedObjectives(objectives);
                    List<WeeklyPlanItemViewDTO> list=unLinkItemMap.get(categorieMap.get(itemViewDTO.getCategorieId()));
                    if(list!=null){
                        list.add(itemViewDTO);
                    }
                }
            });
        }

        List<ObjectiveConbineWeeklyPlanTreeNodeDTO> okrs=new ArrayList<>();
        if(!CollectionUtils.isEmpty(okrItems)){
            okrItems.forEach(okrItem->{
                ObjectiveConbineWeeklyPlanTreeNodeDTO okrNode=new ObjectiveConbineWeeklyPlanTreeNodeDTO();
                BeanUtils.copyProperties(okrItem,okrNode);
                okrs.add(okrNode);
            });
        }
        dto.setOkrs(buildTree(okrs));
        return dto;
    }

    public static ObjectiveWeeklyPlanRegularReportObjectiveViewDTO reportTransform2ObjectiveView(ObjectiveWeeklyPlanRegularEmailReportDTO data){
        ObjectiveWeeklyPlanRegularReportObjectiveViewDTO objectiveView=new   ObjectiveWeeklyPlanRegularReportObjectiveViewDTO();
        objectiveView.setReportTime(data.getReportTime());
        objectiveView.setCategorieList(data.getCategorieList());
        objectiveView.setMemberAvgProgress(data.getMemberAvgProgress());
        objectiveView.setTeamAvgProgress(data.getTeamAvgProgress());
        objectiveView.setOrgAvgProgress(data.getOrgAvgProgress());
        objectiveView.setMemberReportStatics(data.getMemberReportStatics());
        objectiveView.setOrgOkrs(data.getOrgOkrs());
        objectiveView.setTeamOkrs(data.getTeamOkrs());
        objectiveView.setMemberOkrs(data.getMemberOkrs());
        objectiveView.setUnLinkItemMap(data.getUnLinkItemMap());
        return objectiveView;
    }

    public static ObjectiveWeeklyPlanRegularReportCategoryViewDTO reportTransform2CategoryView(ObjectiveWeeklyPlanRegularEmailReportDTO data) {
        ObjectiveWeeklyPlanRegularReportCategoryViewDTO categoryView=new ObjectiveWeeklyPlanRegularReportCategoryViewDTO();
        categoryView.setReportTime(data.getReportTime());
        categoryView.setCategorieList(data.getCategorieList());
        categoryView.setMemberAvgProgress(data.getMemberAvgProgress());
        categoryView.setTeamAvgProgress(data.getTeamAvgProgress());
        categoryView.setOrgAvgProgress(data.getOrgAvgProgress());
        categoryView.setMemberReportStatics(data.getMemberReportStatics());
        categoryView.setCategorieList(data.getCategorieList());
        Map<String,List<WeeklyPlanItemViewDTO>> weeklyPlans=new HashMap<>();
        categoryView.setWeeklyPlans(weeklyPlans);
        weeklyPlans.putAll(data.getUnLinkItemMap());
        //添加组织的okr绑定的plan
        if(data.getOrgOkrs()!=null&&!CollectionUtils.isEmpty(data.getOrgOkrs().getOkrs())){
            data.getOrgOkrs().getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                    objectiveConbineWeeklyPlanDTO.getItemMap().forEach((k,v)->{
                        List<WeeklyPlanItemViewDTO> plans= weeklyPlans.get(k);
                        if(plans==null){
                            plans=new ArrayList<>();
                        }
                        plans.addAll(v);
                    });
                }
            });
        }
        //添加team的okr绑定的plan
        if(!CollectionUtils.isEmpty(data.getTeamOkrs())){
            data.getTeamOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
                if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                    memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                        if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                            objectiveConbineWeeklyPlanDTO.getItemMap().forEach((k,v)->{
                                List<WeeklyPlanItemViewDTO> plans= weeklyPlans.get(k);
                                if(plans==null){
                                    plans=new ArrayList<>();
                                }
                                plans.addAll(v);
                            });
                        }

                    });
                }
            });
        }
        //添加memberokr绑定的plan
        if(!CollectionUtils.isEmpty(data.getMemberOkrs())){
            data.getMemberOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
                if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                    memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                        if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                            objectiveConbineWeeklyPlanDTO.getItemMap().forEach((k,v)->{
                                List<WeeklyPlanItemViewDTO> plans= weeklyPlans.get(k);
                                if(plans==null){
                                    plans=new ArrayList<>();
                                }
                                plans.addAll(v);
                            });
                        }

                    });
                }
            });
        }

        return categoryView;
    }

    public static ObjectiveWeeklyPlanRegularReportUserDTO reportTransform2UserView(ObjectiveWeeklyPlanRegularEmailReportDTO data) {
        ObjectiveWeeklyPlanRegularReportUserDTO userView=new ObjectiveWeeklyPlanRegularReportUserDTO();
        userView.setReportTime(data.getReportTime());
        userView.setCategorieList(data.getCategorieList());
        userView.setMemberAvgProgress(data.getMemberAvgProgress());
        userView.setTeamAvgProgress(data.getTeamAvgProgress());
        userView.setOrgAvgProgress(data.getOrgAvgProgress());
        userView.setMemberReportStatics(data.getMemberReportStatics());
        userView.setCategorieList(data.getCategorieList());
        Map<Long,WeeklyPlanReportUserDTO> map=new Hashtable<>();
        Map<Long,String> categoryMap=new HashMap<>();
        data.getCategorieList().forEach(weeklyTemplateCategoryInfoDTO -> {
            categoryMap.put(weeklyTemplateCategoryInfoDTO.getId(),weeklyTemplateCategoryInfoDTO.getName());
        });
        //将组织okr绑定的plan,分派到对应的个人
        if(data.getOrgOkrs()!=null&&!CollectionUtils.isEmpty(data.getOrgOkrs().getOkrs())){
            data.getOrgOkrs().getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                    objectiveConbineWeeklyPlanDTO.getItemMap().values().forEach(weeklyPlanItemViewDTOList -> {
                        weeklyPlanItemViewDTOList.forEach(weeklyPlanItemViewDTO -> {
                            WeeklyPlanReportUserDTO userPlan= map.get(weeklyPlanItemViewDTO.getOwnerId());
                            if(userPlan==null){
                                userPlan=new WeeklyPlanReportUserDTO();
                                userPlan.setOwnerId(weeklyPlanItemViewDTO.getOwnerId());
                                userPlan.setOwnerName(weeklyPlanItemViewDTO.getOwnerName());
                                userPlan.setCategorieList(data.getCategorieList());
                                userPlan.setItemMap(buildCategoryMap(data.getCategorieList()));
                                map.put(weeklyPlanItemViewDTO.getOwnerId(),userPlan);
                            }
                            String categoryName = categoryMap.get(weeklyPlanItemViewDTO.getCategorieId());
                            if(!StringUtils.isEmpty(categoryName)){
                                List<WeeklyPlanItemViewDTO> planList = userPlan.getItemMap().get(categoryName);
                                if(planList==null){
                                    planList=new ArrayList<>();
                                    userPlan.getItemMap().put(categoryName,planList);
                                }
                                planList.add(weeklyPlanItemViewDTO);
                            }

                        });
                    });
                }
            });
        }

        //将team okr绑定的plan，分派给对应的每个人
        if(!CollectionUtils.isEmpty(data.getTeamOkrs())){
            data.getTeamOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
               if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                   memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                       if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                           objectiveConbineWeeklyPlanDTO.getItemMap().values().forEach(weeklyPlanItemViewDTOList -> {
                               weeklyPlanItemViewDTOList.forEach(weeklyPlanItemViewDTO -> {
                                   WeeklyPlanReportUserDTO userPlan= map.get(weeklyPlanItemViewDTO.getOwnerId());
                                   if(userPlan==null){
                                       userPlan=new WeeklyPlanReportUserDTO();
                                       userPlan.setOwnerId(weeklyPlanItemViewDTO.getOwnerId());
                                       userPlan.setOwnerName(weeklyPlanItemViewDTO.getOwnerName());
                                       userPlan.setCategorieList(data.getCategorieList());
                                       userPlan.setItemMap(buildCategoryMap(data.getCategorieList()));
                                       map.put(weeklyPlanItemViewDTO.getOwnerId(),userPlan);
                                   }
                                   String categoryName = categoryMap.get(weeklyPlanItemViewDTO.getCategorieId());
                                   if(!StringUtils.isEmpty(categoryName)){
                                       List<WeeklyPlanItemViewDTO> planList = userPlan.getItemMap().get(categoryName);
                                       if(planList==null){
                                           planList=new ArrayList<>();
                                           userPlan.getItemMap().put(categoryName,planList);
                                       }
                                       planList.add(weeklyPlanItemViewDTO);
                                   }

                               });
                           });
                       }

                   });
               }
            });
        }

        //将member okr绑定的plan，分派给对应的个人
        if(!CollectionUtils.isEmpty(data.getMemberOkrs())){
            data.getMemberOkrs().forEach(memberObjectiveRegularEmailReportDTO -> {
                if(!CollectionUtils.isEmpty(memberObjectiveRegularEmailReportDTO.getOkrs())){
                    memberObjectiveRegularEmailReportDTO.getOkrs().forEach(objectiveConbineWeeklyPlanDTO -> {
                        if(!CollectionUtils.isEmpty(objectiveConbineWeeklyPlanDTO.getItemMap())){
                            objectiveConbineWeeklyPlanDTO.getItemMap().values().forEach(weeklyPlanItemViewDTOList -> {
                                weeklyPlanItemViewDTOList.forEach(weeklyPlanItemViewDTO -> {
                                    WeeklyPlanReportUserDTO userPlan= map.get(weeklyPlanItemViewDTO.getOwnerId());
                                    if(userPlan==null){
                                        userPlan=new WeeklyPlanReportUserDTO();
                                        userPlan.setOwnerId(weeklyPlanItemViewDTO.getOwnerId());
                                        userPlan.setOwnerName(weeklyPlanItemViewDTO.getOwnerName());
                                        userPlan.setCategorieList(data.getCategorieList());
                                        userPlan.setItemMap(buildCategoryMap(data.getCategorieList()));
                                        map.put(weeklyPlanItemViewDTO.getOwnerId(),userPlan);
                                    }
                                    String categoryName = categoryMap.get(weeklyPlanItemViewDTO.getCategorieId());
                                    if(!StringUtils.isEmpty(categoryName)){
                                        List<WeeklyPlanItemViewDTO> planList = userPlan.getItemMap().get(categoryName);
                                        if(planList==null){
                                            planList=new ArrayList<>();
                                            userPlan.getItemMap().put(categoryName,planList);
                                        }
                                        planList.add(weeklyPlanItemViewDTO);
                                    }

                                });
                            });
                        }

                    });
                }
            });
        }

        //将未绑定的plan，分派给个人
        if(!CollectionUtils.isEmpty(data.getUnLinkItemMap())){
            data.getUnLinkItemMap().forEach((k,v)->{
                if(!CollectionUtils.isEmpty(v)){
                    v.forEach(weeklyPlanItemViewDTO -> {
                        WeeklyPlanReportUserDTO userPlan= map.get(weeklyPlanItemViewDTO.getOwnerId());
                        if(userPlan==null){
                            userPlan=new WeeklyPlanReportUserDTO();
                            userPlan.setOwnerId(weeklyPlanItemViewDTO.getOwnerId());
                            userPlan.setOwnerName(weeklyPlanItemViewDTO.getOwnerName());
                            userPlan.setCategorieList(data.getCategorieList());
                            userPlan.setItemMap(buildCategoryMap(data.getCategorieList()));
                        }
                        String categoryName = categoryMap.get(weeklyPlanItemViewDTO.getCategorieId());
                        if(!StringUtils.isEmpty(categoryName)){
                            List<WeeklyPlanItemViewDTO> planList = userPlan.getItemMap().get(categoryName);
                            if(planList==null){
                                planList=new ArrayList<>();
                                userPlan.getItemMap().put(categoryName,planList);
                            }
                            planList.add(weeklyPlanItemViewDTO);
                        }
                    });
                }


            });
        }
        List<WeeklyPlanReportUserDTO> memberPlans=new ArrayList<>();
        memberPlans.addAll(map.values());
        userView.setMemberWeeklyPlans(memberPlans);
        return userView;
    }


    private static List<WeeklyTemplateCategoryInfoDTO> buildSortedWeeklyCategoryList(WeeklyPlanTemplateDTO template){
        List<WeeklyTemplateCategoryInfoDTO> list=new ArrayList<>();
        List<WeeklyPlanTemplateItemDTO> templateItems=template.getItems();
        Collections.sort(templateItems, new Comparator<WeeklyPlanTemplateItemDTO>() {
            @Override
            public int compare(WeeklyPlanTemplateItemDTO o1, WeeklyPlanTemplateItemDTO o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
        for (WeeklyPlanTemplateItemDTO item:templateItems){
            WeeklyTemplateCategoryInfoDTO categoryInfo=new WeeklyTemplateCategoryInfoDTO();
            categoryInfo.setId(item.getId());
            categoryInfo.setName(item.getName());
            categoryInfo.setColor(item.getColor());
            categoryInfo.setOrder(item.getOrder());
            list.add(categoryInfo);
        }
        return list;
    }

    private static   Map<Long,String>  buildWeeklyCategoryMap(WeeklyPlanTemplateDTO template){
        Map<Long,String> categoryMap=new HashMap<>();
        for (WeeklyPlanTemplateItemDTO item:template.getItems()){
            categoryMap.put(item.getId(),item.getName());
        }
        return categoryMap;
    }

    private static Map<String,List<WeeklyPlanItemViewDTO>> buildCategoryMap(List<WeeklyTemplateCategoryInfoDTO> categoryList){
        Map<String,List<WeeklyPlanItemViewDTO>> map=new HashMap<>();
        categoryList.forEach(weeklyTemplateCategoryInfoDTO -> {
            map.put(weeklyTemplateCategoryInfoDTO.getName(),new ArrayList<>());
        });
        return map;
    }


    private static List<ObjectiveConbineWeeklyPlanTreeNodeDTO> buildTree(List<ObjectiveConbineWeeklyPlanTreeNodeDTO> rawList) {
        Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> brokenRoot = brokenCircle(rawList);
        Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> root = findTreeRoot(rawList);
        root.addAll(brokenRoot);
        List<ObjectiveConbineWeeklyPlanTreeNodeDTO> tree = new ArrayList<>(root);
        for (ObjectiveConbineWeeklyPlanTreeNodeDTO node : rawList) {
            for (ObjectiveConbineWeeklyPlanTreeNodeDTO child : rawList) {
                if (!root.contains(child) && node.getId().equals(child.getLinkedObjectiveId())) {
                    if (node.getChildren() == null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.getChildren().add(child);
                }
            }
        }
        sortedObjectiveConbineWeeklyPlanTreeNodeList(tree);
        return tree;
    }

    private static void sortedObjectiveConbineWeeklyPlanTreeNodeList(List<ObjectiveConbineWeeklyPlanTreeNodeDTO> list){
        if(list==null){
            return ;
        }
        Collections.sort(list, new Comparator<ObjectiveConbineWeeklyPlanTreeNodeDTO>() {
            @Override
            public int compare(ObjectiveConbineWeeklyPlanTreeNodeDTO o1, ObjectiveConbineWeeklyPlanTreeNodeDTO o2) {
                return o1.getId()-o2.getId()>0?-1:1;
            }
        });
        list.forEach( item->{
            sortedObjectiveConbineWeeklyPlanTreeNodeList(item.getChildren());
        });
    }

    private static Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> findTreeRoot(List<ObjectiveConbineWeeklyPlanTreeNodeDTO> rawList){
        Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> heads = new HashSet<>();
        //构建id和node的映射
        Map<Long, ObjectiveConbineWeeklyPlanTreeNodeDTO> map = new HashMap<>();
        rawList.forEach(node -> {
            map.put(node.getId(), node);
        });

        for (ObjectiveConbineWeeklyPlanTreeNodeDTO node : rawList) {
            if(map.get(node.getLinkedObjectiveId())==null){
                heads.add(node);
            }
        }
        return heads;
    }

    private static Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> brokenCircle(List<ObjectiveConbineWeeklyPlanTreeNodeDTO> rawList) {
        Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> heads = new HashSet<>();
        //构建id和node的映射
        Map<Long, ObjectiveConbineWeeklyPlanTreeNodeDTO> map = new HashMap<>();
        rawList.forEach(node -> {
            map.put(node.getId(), node);
        });
        //遍历判断是否存在圆
        Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> finded = new HashSet<>();
        for (ObjectiveConbineWeeklyPlanTreeNodeDTO node : rawList) {
            //没有关联
            if (node.getLinkedObjectiveId() == null) {
                continue;
            }
            //已经在一个环内的
            if (finded.contains(node)) {
                continue;
            }
            //查找环，如果存在将环中最小的id作为树根
            ObjectiveConbineWeeklyPlanTreeNodeDTO cursor = node;
            Long minId = cursor.getId();
            Set<ObjectiveConbineWeeklyPlanTreeNodeDTO> circle = new HashSet<>();
            while (cursor != null) {
                if (!circle.contains(cursor)) {
                    if (minId > cursor.getId()) {
                        minId = cursor.getId();
                    }
                    circle.add(cursor);
                    finded.add(cursor);
                    cursor = map.get(cursor.getLinkedObjectiveId());
                } else {
                    //存在环
                    heads.add(map.get(minId));
                    break;
                }
            }
        }
        return heads;
    }

}
