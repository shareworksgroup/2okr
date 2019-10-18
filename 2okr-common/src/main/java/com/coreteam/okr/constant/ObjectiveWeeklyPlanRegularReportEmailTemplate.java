package com.coreteam.okr.constant;

import com.coreteam.okr.dto.objective.MemberObjectiveRegularEmailReportDTO;
import com.coreteam.okr.dto.objective.ObjectiveConbineWeeklyPlanDTO;
import com.coreteam.okr.dto.objective.ObjectiveKeyResultDTO;
import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.plan.PersonalWeeklyPlanSimpleReportForRegularReportDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanItemViewDTO;
import com.coreteam.okr.dto.plan.WeeklyTemplateCategoryInfoDTO;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: ObjectiveWeeklyPlanRegularReportEmailTemplate
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/13 11:27
 * @Version 1.0.0
 */
public class ObjectiveWeeklyPlanRegularReportEmailTemplate {
    private static Map<String, String> categoryColorMap = new HashMap() {{
        put("ccolor1", "#3354EB");
        put("ccolor2", "#6800CF");
        put("ccolor3", "#6800CF");
        put("ccolor4", "#E9008C");
        put("ccolor5", "#FF000e");
        put("ccolor6", "#FA3D00");
        put("ccolor7", "#FA8500");
        put("ccolor8", "#FAAC1c");
        put("ccolor9", "#F4D302");
        put("ccolor10", "#9FDA00");
        put("ccolor11", "#32C000");
    }};

    private static Map<String, String> objectiveColorMap = new HashMap() {{
        put("OFF_TRACK", "#FF4D4F");
        put("AT_RISK", "#FFA940");
        put("ON_TRACK", "#52C41A");
        put("EXCEEDED", "#52C41A");

    }};

    private static Map<String, String> keyResultColorMap = new HashMap() {{
        put("OFF_TRACK", "#FFCCC7");
        put("AT_RISK", "#FFD591");
        put("ON_TRACK", "#B7EB8F");
        put("EXCEEDED", "#B7EB8F");

    }};

    static DecimalFormat df = new DecimalFormat("0");

    public static String transForm(ObjectiveWeeklyPlanRegularEmailReportDTO data, String greeting) {
        if(greeting==null){
            greeting="Sir or Madam";
        }
        StringBuilder builder = new StringBuilder();
        //构造头部greeting和okr统计信息
        builder.append(HEAD.replace("{name}",greeting).replace("{reportUrl}",data.getReportUrl()).replace("{reportTime}",data.getReportTime())
                .replace("{orgAvgProgress}",data.getOrgAvgProgress()).replace("{teamAvgProgress}",data.getTeamAvgProgress())
                .replace("{memberAvgProgress}",data.getMemberAvgProgress()));
        //构造weekly plan的统计信息
        List<WeeklyTemplateCategoryInfoDTO> categoryList = data.getCategorieList();
        Map<Long,String> categoryIdColorMap=new HashMap<>();
        Map<String,String> categoryNameColorMap=new HashMap<>();
        categoryList.forEach(weeklyTemplateCategoryInfoDTO -> {
            categoryIdColorMap.put(weeklyTemplateCategoryInfoDTO.getId(),weeklyTemplateCategoryInfoDTO.getColor());
            categoryNameColorMap.put(weeklyTemplateCategoryInfoDTO.getName(),weeklyTemplateCategoryInfoDTO.getColor());
        });
        List<PersonalWeeklyPlanSimpleReportForRegularReportDTO> weeklyPlanStatics = data.getMemberReportStatics();
        if(!CollectionUtils.isEmpty(weeklyPlanStatics)){
            builder.append(WEEKLY_PLAN_STATICS_HEAD);
            weeklyPlanStatics.forEach(personalWeeklyPlanSimpleReportForRegularReportDTO -> {
                //每个人的report
                Integer totalNum=0;
                for(Integer num:personalWeeklyPlanSimpleReportForRegularReportDTO.getPlanNum().values()){
                    totalNum+=num;
                }
                builder.append(WEEKLY_PLAN_STATICS_ITEM_NAME.replace("{name}",personalWeeklyPlanSimpleReportForRegularReportDTO.getName()));
                if(!CollectionUtils.isEmpty(personalWeeklyPlanSimpleReportForRegularReportDTO.getPlanNum())){
                    builder.append(WEEKLY_PLAN_STATICS_ITEM_CATEGORY_HEAD);
                    for(WeeklyTemplateCategoryInfoDTO categoryItem:categoryList){
                        if(personalWeeklyPlanSimpleReportForRegularReportDTO.getPlanNum().get(categoryItem.getId())!=null){
                            Integer categorySum = personalWeeklyPlanSimpleReportForRegularReportDTO.getPlanNum().get(categoryItem.getId());
                            Integer width=Double.valueOf((Double.valueOf(categorySum)/Double.valueOf(totalNum))*100).intValue();
                            builder.append(WEEKLY_PLAN_STATICS_ITEM_CATEGORY_ITEM.replace("{width}",String.valueOf(width))
                                    .replace("{categorycolor}",categoryColorMap.get(categoryIdColorMap.get(categoryItem.getId())))
                                    .replace("{categorySum}",String.valueOf(categorySum)));
                        }
                    }
                    builder.append(WEEKLY_PLAN_STATICS_ITEM_CATEGORY_FOOT);
                }
                builder.append(WEEKLY_PLAN_STATICS_ITEM_START.replace("{star}",getStartHtml(personalWeeklyPlanSimpleReportForRegularReportDTO.getWeeklyJobSatisfaction())));
            });
            builder.append(WEEKLY_PLAN_STATICS_FOOT);
        }
        MemberObjectiveRegularEmailReportDTO orgOkrs = data.getOrgOkrs();
        //构建组织的okr和weeklyplan
        if(orgOkrs!=null&&orgOkrs.getName()!=null&&!CollectionUtils.isEmpty(orgOkrs.getOkrs())){
            builder.append(ORG_OKR_WEEKLY_PLAN_HEAD.replace("{size}","1")
                    .replace("{name_captain}",orgOkrs.getName().split("")[0]).replace("{name}",orgOkrs.getName()).replace("{okrColor}",objectiveColorMap.get(getBusinessStatusByProgress(orgOkrs.getAvgProgress())))
                    .replace("{okrNum}",String.valueOf(orgOkrs.getOkrNum())).replace("{krColor}",keyResultColorMap.get(getBusinessStatusByProgress(orgOkrs.getAvgProgress()))).replace("{krNum}",String.valueOf(orgOkrs.getKrNum()))
                    .replace("{color}",objectiveColorMap.get(getBusinessStatusByProgress(orgOkrs.getAvgProgress()))).replace("{okrAvgProgress}",df.format(orgOkrs.getAvgProgress())));
            List<ObjectiveConbineWeeklyPlanDTO> orgOkrList = orgOkrs.getOkrs();
            if(!CollectionUtils.isEmpty(orgOkrList)){
                orgOkrList.forEach(okrAndWeeklyPlan->{
                    //构造Objective
                    builder.append(OKR.replace("{name_captain}","O")
                            .replace("{name}",okrAndWeeklyPlan.getName()).replace("{progress}",df.format(okrAndWeeklyPlan.getProgress()*100)));
                    //构造k-r
                    List<ObjectiveKeyResultDTO> krs = okrAndWeeklyPlan.getKeyResults();
                    if(!CollectionUtils.isEmpty(krs)){
                        krs.forEach(kr->{
                            builder.append(KEY_RESULT.replace("{name}",kr.getName()).replace("{progress}",df.format(kr.getProgress()*100)));
                        });
                    }
                    //构造weeklyPlan

                    Map<String, List<WeeklyPlanItemViewDTO>> weeklyPlans = okrAndWeeklyPlan.getItemMap();
                    if(!CollectionUtils.isEmpty(weeklyPlans)){
                        builder.append(OKR_WEEKLY_PLAN_SPLIT);
                        builder.append(WEEKLY_PLAN_HEAD);
                        categoryList.forEach(category->{
                            /*if (!CollectionUtils.isEmpty(weeklyPlans.get(category.getName()))){*/
                            builder.append(WEEKLY_PLAN_CATEGORY_ITEM_HEAD.replace("{category_color}",categoryColorMap.get(categoryIdColorMap.get(category.getId())))
                                    .replace("{category}",category.getName()));
                            List<WeeklyPlanItemViewDTO> plans = weeklyPlans.get(category.getName());
                            if(!CollectionUtils.isEmpty(plans)){
                                plans.forEach(weeklyPlanItemViewDTO -> {
                                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM.replace("{name_captain}",weeklyPlanItemViewDTO.getOwnerName().split("")[0])
                                            .replace("{name}",weeklyPlanItemViewDTO.getOwnerName())
                                            .replace("{plan}",weeklyPlanItemViewDTO.getDesc()));
                                });
                            }
                            builder.append(WEEKLY_PLAN_CATEGORY_ITEM_FOOT);
                            /*  }*/
                        });
                        builder.append(WEEKLY_PLAN_FOOT);
                    }
                });
            }
            builder.append(ORG_OKR_WEEKLY_PLAN_FOOT);
        }

        //构建team的okr和weeklyplan
        List<MemberObjectiveRegularEmailReportDTO> teamOkrs = data.getTeamOkrs();
        if(!CollectionUtils.isEmpty(teamOkrs)){
            builder.append(TEAM_OKR_WEEKLY_PLAN.replace("{size}",String.valueOf(teamOkrs.size())));
            teamOkrs.forEach(memberObjectiveRegularEmailReportDTO -> {
                builder.append(TEAM_OKR_WEEKLY_PLAN_HEAD
                        .replace("{name_captain}",memberObjectiveRegularEmailReportDTO.getName().split("")[0]).replace("{name}",memberObjectiveRegularEmailReportDTO.getName()).replace("{okrColor}",objectiveColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress())))
                        .replace("{okrNum}",String.valueOf(memberObjectiveRegularEmailReportDTO.getOkrNum())).replace("{krColor}",keyResultColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress()))).replace("{krNum}",String.valueOf(memberObjectiveRegularEmailReportDTO.getKrNum()))
                        .replace("{color}",objectiveColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress()))).replace("{avgProgress}",df.format(memberObjectiveRegularEmailReportDTO.getAvgProgress())));
                List<ObjectiveConbineWeeklyPlanDTO> teamOkrList = memberObjectiveRegularEmailReportDTO.getOkrs();
                if(!CollectionUtils.isEmpty(teamOkrList)){
                    teamOkrList.forEach(okrAndWeeklyPlan->{
                        //构造Objective
                        builder.append(OKR.replace("{name_captain}","O")
                                .replace("{name}",okrAndWeeklyPlan.getName()).replace("{progress}",df.format(okrAndWeeklyPlan.getProgress()*100)));
                        //构造k-r
                        List<ObjectiveKeyResultDTO> krs = okrAndWeeklyPlan.getKeyResults();
                        if(!CollectionUtils.isEmpty(krs)){
                            krs.forEach(kr->{
                                builder.append(KEY_RESULT.replace("{name}",kr.getName()).replace("{progress}",df.format(kr.getProgress()*100)));
                            });
                        }
                        //构造weeklyPlan

                        Map<String, List<WeeklyPlanItemViewDTO>> weeklyPlans = okrAndWeeklyPlan.getItemMap();
                        if(!CollectionUtils.isEmpty(weeklyPlans)){
                            builder.append(OKR_WEEKLY_PLAN_SPLIT);
                            builder.append(WEEKLY_PLAN_HEAD);
                            categoryList.forEach(category->{
                               /* if (!CollectionUtils.isEmpty(weeklyPlans.get(category.getName()))){*/
                                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_HEAD.replace("{category_color}",categoryColorMap.get(categoryIdColorMap.get(category.getId())))
                                            .replace("{category}",category.getName()));
                                    List<WeeklyPlanItemViewDTO> plans = weeklyPlans.get(category.getName());
                                    if(!CollectionUtils.isEmpty(plans)){
                                        plans.forEach(weeklyPlanItemViewDTO -> {
                                            builder.append(WEEKLY_PLAN_CATEGORY_ITEM.replace("{name_captain}",weeklyPlanItemViewDTO.getOwnerName().split("")[0])
                                                    .replace("{name}",weeklyPlanItemViewDTO.getOwnerName())
                                                    .replace("{plan}",weeklyPlanItemViewDTO.getDesc()));
                                        });
                                    }
                                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_FOOT);
                              /*  }*/
                            });
                            builder.append(WEEKLY_PLAN_FOOT);
                        }
                    });
                }

                builder.append(TEAM_OKR_WEEKLY_PLAN_FOOT);
            });
            builder.append(TEAM_OKR_WEEKLY_FOOT);
        }

        //构建member的okr和weeklyplan
        List<MemberObjectiveRegularEmailReportDTO> memberOkrs = data.getMemberOkrs();
        if(!CollectionUtils.isEmpty(memberOkrs)){
            builder.append(MEMBER_OKR_WEEKLY_PLAN.replace("{size}",String.valueOf(memberOkrs.size())));
            memberOkrs.forEach(memberObjectiveRegularEmailReportDTO -> {
                builder.append(MEMBER_OKR_WEEKLY_PLAN_HEAD
                        .replace("{name_captain}",memberObjectiveRegularEmailReportDTO.getName().split("")[0]).replace("{name}",memberObjectiveRegularEmailReportDTO.getName()).replace("{okrColor}",objectiveColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress())))
                        .replace("{okrNum}",String.valueOf(memberObjectiveRegularEmailReportDTO.getOkrNum())).replace("{krColor}",keyResultColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress()))).replace("{krNum}",String.valueOf(memberObjectiveRegularEmailReportDTO.getKrNum()))
                        .replace("{color}",objectiveColorMap.get(getBusinessStatusByProgress(memberObjectiveRegularEmailReportDTO.getAvgProgress()))).replace("{avgProgress}",df.format(memberObjectiveRegularEmailReportDTO.getAvgProgress())));
                List<ObjectiveConbineWeeklyPlanDTO> memberOkrList = memberObjectiveRegularEmailReportDTO.getOkrs();
                if(!CollectionUtils.isEmpty(memberOkrList)){
                    memberOkrList.forEach(okrAndWeeklyPlan->{
                        //构造Objective
                        builder.append(OKR.replace("{name_captain}","O")
                                .replace("{name}",okrAndWeeklyPlan.getName()).replace("{progress}",df.format(okrAndWeeklyPlan.getProgress()*100)));
                        //构造k-r
                        List<ObjectiveKeyResultDTO> krs = okrAndWeeklyPlan.getKeyResults();
                        if(!CollectionUtils.isEmpty(krs)){
                            krs.forEach(kr->{
                                builder.append(KEY_RESULT.replace("{name}",kr.getName()).replace("{progress}",df.format(kr.getProgress()*100)));
                            });
                        }
                        //构造weeklyPlan

                        Map<String, List<WeeklyPlanItemViewDTO>> weeklyPlans = okrAndWeeklyPlan.getItemMap();
                        if(!CollectionUtils.isEmpty(weeklyPlans)){
                            builder.append(OKR_WEEKLY_PLAN_SPLIT);
                            builder.append(WEEKLY_PLAN_HEAD);
                            categoryList.forEach(category->{
                               /* if (!CollectionUtils.isEmpty(weeklyPlans.get(category.getName()))){*/
                                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_HEAD.replace("{category_color}",categoryColorMap.get(categoryIdColorMap.get(category.getId())))
                                            .replace("{category}",category.getName()));
                                    List<WeeklyPlanItemViewDTO> plans = weeklyPlans.get(category.getName());
                                    if(!CollectionUtils.isEmpty(plans)){
                                        plans.forEach(weeklyPlanItemViewDTO -> {
                                            builder.append(WEEKLY_PLAN_CATEGORY_ITEM.replace("{name_captain}",weeklyPlanItemViewDTO.getOwnerName().split("")[0])
                                                    .replace("{name}",weeklyPlanItemViewDTO.getOwnerName())
                                                    .replace("{plan}",weeklyPlanItemViewDTO.getDesc()));
                                        });
                                    }
                                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_FOOT);
                                /*}*/
                            });
                            builder.append(WEEKLY_PLAN_FOOT);
                        }
                    });
                }

                builder.append(MEMBER_OKR_WEEKLY_PLAN_FOOT);
            });
            builder.append(MEMBER_OKR_WEEKLY_FOOT);
        }

        //构造unlinked的plan
        Map<String, List<WeeklyPlanItemViewDTO>> unlinkedPlans = data.getUnLinkItemMap();
        if(!CollectionUtils.isEmpty(unlinkedPlans)){
            builder.append(UNLINKED_WEEKLY_PLAN_HEAD);
            categoryList.forEach(category->{
              /*  if (!CollectionUtils.isEmpty(unlinkedPlans.get(category.getName()))){*/
                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_HEAD.replace("{category_color}",categoryColorMap.get(categoryIdColorMap.get(category.getId())))
                            .replace("{category}",category.getName()));
                    List<WeeklyPlanItemViewDTO> plans = unlinkedPlans.get(category.getName());
                    if(!CollectionUtils.isEmpty(plans)){
                        plans.forEach(weeklyPlanItemViewDTO -> {
                            builder.append(WEEKLY_PLAN_CATEGORY_ITEM.replace("{name_captain}",weeklyPlanItemViewDTO.getOwnerName().split("")[0])
                                    .replace("{name}",weeklyPlanItemViewDTO.getOwnerName())
                                    .replace("{plan}",weeklyPlanItemViewDTO.getDesc()));
                        });
                    }
                    builder.append(WEEKLY_PLAN_CATEGORY_ITEM_FOOT);
                /*}*/
            });
            builder.append(UNLINKED_WEEKLY_PLAN_FOOT);

        }
        builder.append(FOOT);
        return builder.toString();
    }

    public static String getBusinessStatusByProgress(Double progress) {
        if (progress >= 66) {
            return "ON_TRACK";
        } else if (progress >= 33) {
            return "AT_RISK";
        } else {
            return "OFF_TRACK";
        }

    }

    public static String getStartHtml(Integer jobStation){
        switch (jobStation){
            case 1:
                return "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">";
            case 2:
                return "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">";
            case 3:
                return "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">";
            case 4:
                return "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">";
            case 5:
                return "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">" +
                        "<img src=\"https://2okr.com/assets/images/email-icon/star-on.png\">";
                default:
                    return "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                            "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                            "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                            "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">" +
                            "<img src=\"https://2okr.com/assets/images/email-icon/star-off.png\">";

        }
    }

    public static final String HEAD="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
            "    <title>2OKR-Weekly Planning</title>\n" +
            "</head>\n" +
            "\n" +
            "<body style=\"background: #fbfbfb;\">\n" +
            "\t<table style=\"width:100%;border:none;background: #fbfbfb;\">\n" +
            "\t\t<tbody>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td class=\"padding:30px;\">\n" +
            "\t\t\t\t\t<table align=\"center\" style=\"width:680px;background:#fefefe;border:none;margin:auto;margin-top:10px;box-shadow: 0 0 2px 0 rgba(0,0,0,0.04), 0 4px 8px 0 rgba(0,0,0,0.04);font-family:Roboto,RobotoDraft,Helvetica,Arial,sans-serif;\">\n" +
            "\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:30px;text-align:center;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:10px 0;margin:0;font-size:24px;font-weight: bold;\">\n" +
            "\t\t\t\t\t\t\t\t\t\tDear {name},\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:0;margin: 10px 0 0;font-size:16px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\tHope you and your team have made good progress.<br/>\n" +
            "\t\t\t\t\t\t\t\t\t\tHere is a snapshot of <a href=\"{reportUrl}\" style=\"text-decoration: none;color:#1D39C4;font-weight: bold;\">your OKRs</a> in 2OKR:\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t<tr style=\"background: #222222;\">\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:15px 30px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"text-align:center; color:#E9E9E9;\">Objectives and Key Results for {reportTime}</p>\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto;text-align:center;padding:30px 0 0;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"width:33%;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 30px;margin:0;padding:0;\">{orgAvgProgress}</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 16px;\">Organization</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"width:33%;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 30px;margin:0;padding:0;\">{teamAvgProgress}</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 16px;\">Team</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"width:33%;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 30px;margin:0;padding:0;\">{memberAvgProgress}</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"color: #BFBFBF;font-size: 16px;\">Personal</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String WEEKLY_PLAN_STATICS_HEAD="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 20px 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:25px auto 0;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<thead>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<th align=\"left\" style=\"background:#f0f0f0;font-weight: normal;padding:8px 10px;font-size: 14px;\">Name</th>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<th align=\"left\" style=\"background:#f0f0f0;font-weight: normal;padding:8px 10px;font-size: 14px;\">Progress, Plans and Problems</th>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<th align=\"right\" style=\"background:#f0f0f0;font-weight: normal;padding:5px 10px;font-size: 14px;\">Happiness</th>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</thead>\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>";

    public static final String WEEKLY_PLAN_STATICS_ITEM_NAME="<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"left\" style=\"padding:8px 10px;font-size: 14px;border-bottom: 1px solid #e9e9e9;\">{name}</td>";
    public static final String WEEKLY_PLAN_STATICS_ITEM_CATEGORY_HEAD="<td align=\"left\" style=\"padding:8px 10px;font-size: 14px;border-bottom: 1px solid #e9e9e9;\">";
    public static final String WEEKLY_PLAN_STATICS_ITEM_CATEGORY_FOOT="</td>";
    public static final String WEEKLY_PLAN_STATICS_ITEM_CATEGORY_ITEM="<span style=\"width:{width}px;height:16px;line-height: 16px;background:{categorycolor};color: #fff;font-size: 12px;display: inline-block;text-align: center;\">{categorySum}</span>";

    public static final String WEEKLY_PLAN_STATICS_ITEM_START="\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"right\" style=\"padding:8px 10px;font-size: 14px;border-bottom: 1px solid #e9e9e9;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t{star}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";
    public static final String WEEKLY_PLAN_STATICS_FOOT="\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String ORG_OKR_WEEKLY_PLAN_HEAD="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 20px 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"font-size: 20px; font-weight: 600;padding-bottom: 10px; margin-bottom:0\">\n" +
            "\t\t\t\t\t\t\t\t\t\tOrganization ({size})\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;margin:10px auto; border:1px solid #e9e9e9;padding:15px 15px 10px 10px;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"width:36px; padding-bottom: 20px;text-align:center;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:36px;height:36px;background-color:#E9E9E9;color:#5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 16px;line-height: 36px;font-weight:600;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name_captain}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:0 5px; padding-bottom: 20px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"javascript:void(0);\" style=\"text-decoration: none;color:#1D39C4;font-weight: bold;color:#333;\">{name}</a>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:30px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{okrColor};margin-left:5px;\">{okrNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:120px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{krColor};margin-left:-5px;\">{krNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"text-align:right;width:130px; padding-bottom: 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"color: {color};font-weight: bold;\">{okrAvgProgress}%</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String ORG_OKR_WEEKLY_PLAN_FOOT="\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";


    public static final String OKR="<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"width:36px;text-align:center; padding-bottom: 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:28px;height:28px;background-color:#E9E9E9;color:#5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 12px;line-height: 28px;font-weight:600;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name_captain}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"padding:0 5px;font-weight: 600;font-size: 16px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t{name}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"text-align:right;width:130px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"font-weight: 600;font-size: 16px;\">{progress}%</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String KEY_RESULT="<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"width:36px;text-align:center; padding-bottom: 10px;\">\n" +
           /* "\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"https://2okr.com/assets/images/email-icon/point-circle.png\" style=\"vertical-align:middle;width:8px;\">\n" +*/
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:28px;height:28px;background-color:#E9E9E9;color:#5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 12px;line-height: 28px;font-weight:600;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tKR\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"padding:0 5px 15px;font-size: 14px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t{name}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"text-align:right;width:130px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"font-size: 16px;\">{progress}%</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String OKR_WEEKLY_PLAN_SPLIT="\t\t\t\t\t\t\t\t\t\t\t<tr><td colspan=\"3\" style=\"border-bottom: 1px solid #e9e9e9;height: 1px;\"></td></tr>";

    public static final String WEEKLY_PLAN_HEAD="\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td  colspan=\"3\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:0 auto;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>";


    public static final String WEEKLY_PLAN_FOOT="\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";



    public static final  String  WEEKLY_PLAN_CATEGORY_ITEM_HEAD="\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:20px 0 10px 45px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:10px;height: 10px;margin-right: 5px;background:{category_color}\"></span> {category}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<ul style=\"margin:0;list-style: none;padding:10px 0;border-bottom: 1px solid #e9e9e9;\">";



    public static final String WEEKLY_PLAN_CATEGORY_ITEM_FOOT="\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</ul>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String WEEKLY_PLAN_CATEGORY_ITEM="\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li style=\"font-size:14px;line-height: 28px;list-style:none\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:0 auto;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"width:135px;\" valign=\"top\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:30px;height:30px;background-color: #E9E9E9;color: #5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 12px;line-height: 30px;font-weight: bold;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name_captain}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"width:12px;\" valign=\"top\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"https://2okr.com/assets/images/email-icon/point.png\" style=\"vertical-align:middle;width:8px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\">{plan}</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</li>";

    public static final String TEAM_OKR_WEEKLY_PLAN="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 20px 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"font-size: 20px; font-weight: 600;padding-bottom: 10px; margin-bottom:0\">\n" +
            "\t\t\t\t\t\t\t\t\t\tTeam ({size})\n" +
            "\t\t\t\t\t\t\t\t\t</p>";

    public static final String TEAM_OKR_WEEKLY_PLAN_HEAD="\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto; border:1px solid #e9e9e9;padding:15px 15px 10px 10px;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"width:36px; padding-bottom: 20px;text-align:center;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:36px;height:36px;background-color: #E9E9E9;color: #5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 16px;line-height: 36px;font-weight:600;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name_captain}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:0 5px; padding-bottom: 20px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"javascript:void(0);\" style=\"text-decoration: none;color:#1D39C4;font-weight: bold;color:#333;\">{name}</a>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:30px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{okrColor};margin-left:5px;\">{okrNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:120px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{krColor};margin-left:-5px;\">{krNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"text-align:right;width:130px; padding-bottom: 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"color: {color};font-weight: bold;\">{avgProgress}%</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String TEAM_OKR_WEEKLY_PLAN_FOOT="\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>";

    public static final String TEAM_OKR_WEEKLY_FOOT="\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String MEMBER_OKR_WEEKLY_PLAN="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 20px 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"font-size: 20px; font-weight: 600;padding-bottom: 10px; margin-bottom:0\">\n" +
            "\t\t\t\t\t\t\t\t\t\tMember ({size})\n" +
            "\t\t\t\t\t\t\t\t\t</p>";

    public static final String MEMBER_OKR_WEEKLY_PLAN_HEAD="\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto; border:1px solid #e9e9e9;padding:15px 15px 10px 10px;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"width:36px; padding-bottom: 20px;text-align:center;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:36px;height:36px;background-color:#E9E9E9;color:#5A5A5A;text-transform: uppercase;text-align: center;border-radius: 50%;font-size: 16px;line-height: 36px;font-weight:600;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t{name_captain}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:0 5px; padding-bottom: 20px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"javascript:void(0);\" style=\"text-decoration: none;color:#1D39C4;font-weight: bold;color:#333;\">{name}</a>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:30px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{okrColor};margin-left:5px;\">{okrNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:120px;height: 16px;font-size: 12px;text-align:center;color:#fff;background:{krColor};margin-left:-5px;\">{krNum}</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td valign=\"top\" style=\"text-align:right;width:130px; padding-bottom: 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"color: {color};font-weight: bold;\">{avgProgress}%</span>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>";

    public static final String MEMBER_OKR_WEEKLY_PLAN_FOOT="\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>";

    public static final String MEMBER_OKR_WEEKLY_FOOT="\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String UNLINKED_WEEKLY_PLAN_HEAD="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 20px 10px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"font-size: 20px; font-weight: 600;padding-bottom: 10px; margin-bottom:0\">\n" +
            "\t\t\t\t\t\t\t\t\t\tUnlinked\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto; border:1px solid #e9e9e9;padding:15px 15px 10px 10px;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td >\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:0 auto;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>";

    public static final String UNLINKED_WEEKLY_PLAN_FOOT="\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";


    public static final String FOOT="\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t</table>\n" +
            "\t\t\t\t</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t</tbody>\n" +
            "\t</table>\n" +
            "</body>\n" +
            "\n" +
            "</html>";


}
