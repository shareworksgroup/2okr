package com.coreteam.okr.service;

import com.coreteam.okr.dto.plantemplate.SaveWeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.coreteam.okr.entity.Organization;

/**
 * @ClassName: WeeklyPlanTemplateService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 13:38
 * @Version 1.0.0
 */
public interface WeeklyPlanTemplateService {

    /**
     * 获取默认模板
     * @return
     */
    WeeklyPlanTemplateDTO getDefaultTemplate();

    /**
     * 根据组织id，返回组织的week plan 模板
     * @param orgId
     * @return
     */
    WeeklyPlanTemplateDTO getWeeklyPlanTemplateByOrganization(Long orgId);

    /**
     * 设置默认组织模板
     * @param org
     */
    void setOrganizationDefaultWeeklyPlanTemplate(Organization org);

    /**
     * 根据 id返回template
     * @param templateId
     * @return
     */
    WeeklyPlanTemplateDTO getWeeklyPlanTemplateById(Long templateId);

    /**
     * 插入week plan 模板数据
     * @param insertDto
     */
    void saveWeeklyPlanTemplate(SaveWeeklyPlanTemplateDTO insertDto);

    void deleteWeeklyPlanTemplateItem(Long organizationId, Long itemId);
}
