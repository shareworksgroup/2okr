package com.coreteam.okr.constant;

import com.coreteam.okr.dto.plan.WeeklyPlanCategoryDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanItemViewDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanReportEmailTemplate
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/27 13:55
 * @Version 1.0.0
 */
public class WeeklyPlanReportEmailTemplate {
    private static Map<String,String> colorMap= new HashMap(){{
        put("ccolor1","#3354EB");
        put("ccolor2","#6800CF");
        put("ccolor3","#6800CF");
        put("ccolor4","#E9008C");
        put("ccolor5","#FF000e");
        put("ccolor6","#FA3D00");
        put("ccolor7","#FA8500");
        put("ccolor8","#FAAC1c");
        put("ccolor9","#F4D302");
        put("ccolor10","#9FDA00");
        put("ccolor11","#32C000");
    }};


    public static String transForm(WeeklyPlanCategoryDTO weeklyPlanDTO, String title){
        StringBuilder builder=new StringBuilder();
        builder.append(GREETING.replace("{greeting}", title));
        if(weeklyPlanDTO.getItemMap()!=null&&!weeklyPlanDTO.getItemMap().isEmpty()){
            builder.append(SUM_START);
            weeklyPlanDTO.getCategorieList().forEach(categories->{
                String color=colorMap.get(categories.getColor());
                if(color==null){
                    color=colorMap.get("ccolor1");
                }
                List<WeeklyPlanItemViewDTO> itemList = weeklyPlanDTO.getItemMap().get(categories.getName());
                int sum=itemList!=null?itemList.size():0;
                builder.append(SUM.replace("{sum_color}",color).replace("{sum}",String.valueOf(sum)));

            });
            builder.append(SUM_END);
            weeklyPlanDTO.getCategorieList().forEach(categories->{
                List<WeeklyPlanItemViewDTO> itemList = weeklyPlanDTO.getItemMap().get(categories.getName());
                if(itemList!=null&&!itemList.isEmpty()){
                    String color=colorMap.get(categories.getColor());
                    if(color==null){
                        color=colorMap.get("ccolor1");
                    }
                    builder.append(ITEM_STARY.replace("{color}",color).replace("{category}",categories.getName()));
                    itemList.forEach(weeklyPlanItemViewDTO -> {
                        builder.append(ITEM_LIST.replace("{category_content}",weeklyPlanItemViewDTO.getDesc()));
                    });
                    builder.append(ITEM_END);
                }

            });
        }
        builder.append(FOOTER);
        return builder.toString();
    }




    public static final String GREETING="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\" />\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />\n" +
            "    <title>2OKR-Weekly Planning</title>\n" +
            "</head>\n" +
            "\n" +
            "<body style=\"background: #fbfbfb;\">\n" +
            "\t<table style=\"width:100%;border:none;background: #fbfbfb;\">\n" +
            "\t\t<tbody>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td class=\"padding:30px;\">\n" +
            "\t\t\t\t\t<table style=\"width:1000px;background:#fff;border:none;margin:auto;margin-top:100px;box-shadow: 0 0 2px 0 rgba(0,0,0,0.04), 0 4px 8px 0 rgba(0,0,0,0.04);\" align=\"center\">\n" +
            "\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:30px 30px 0;\">\n" +
            "\t\t\t\t\t\t\t\t\t<img src=\"https://2okr.com/assets/images/logo@2x.png\" style=\"height:24px;\" />\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:20px 0 0;margin: 10px 0 0;font-size:16px; font-weight: 600;\">{greeting}</p>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";
    public static final String SUM_START="<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 30px 30px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>";
    public static final String SUM="\t\t<td style=\"background: {sum_color};text-align:center;height: 18px;color: #fff;\">{sum}</td>";
    public static final String SUM_END="\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String ITEM_STARY="\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:0 30px 30px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<table style=\"width:100%;border:none;margin:10px auto;background:#f9f9f9;\" align=\"center\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:15px 15px 5px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"display: inline-block;width:10px;height: 10px;margin-right: 5px;background: {color}\"></span> {category}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding:20px 8px 15px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<ol style=\"margin:10px\">";
    public static final String ITEM_LIST="\t\t<li style=\"font-size:14px;line-height: 28px;\">{category_content}</li>";

    public static final String ITEM_END="</ol>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>";

    public static final String FOOTER="\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t</table>\n" +
            "\t\t\t\t</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t</tbody>\n" +
            "\t</table>\n" +
            "</body>\n" +
            "</html>";
}
