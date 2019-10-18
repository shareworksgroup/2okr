package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.OkrResultEnum;
import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.constant.WeeklyPlanTemplateTypeEnum;
import com.coreteam.okr.dao.WeeklyPlanTemplateDAO;
import com.coreteam.okr.dao.WeeklyPlanTemplateItemDAO;
import com.coreteam.okr.dto.plantemplate.SaveWeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.plantemplate.SaveWeeklyPlanTemplateItemDTO;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateItemDTO;
import com.coreteam.okr.entity.Organization;
import com.coreteam.okr.entity.WeeklyPlanTemplate;
import com.coreteam.okr.entity.WeeklyPlanTemplateItem;
import com.coreteam.okr.service.OrganizationService;
import com.coreteam.okr.service.WeeklyPlanTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ThinkPad
 */
@Service
public class WeeklyPlanTemplateServiceImpl implements WeeklyPlanTemplateService {

    @Autowired
    private WeeklyPlanTemplateDAO weeklyPlanTemplateDAO;

    @Autowired
    private WeeklyPlanTemplateItemDAO weeklyPlanTemplateItemDAO;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 获取默认模板
     *
     * @return
     */
    @Override
    public WeeklyPlanTemplateDTO getDefaultTemplate() {
        return getWeeklyPlanTemplateByTypeAndId(WeeklyPlanTemplateTypeEnum.SYSTEM.toString(), Long.parseLong("0"));
    }

    /**
     * 根据组织id，返回组织的week plan 模板
     *
     * @param orgId
     * @return
     */
    @Override
    public WeeklyPlanTemplateDTO getWeeklyPlanTemplateByOrganization(Long orgId) {
        return getWeeklyPlanTemplateByTypeAndId(WeeklyPlanTemplateTypeEnum.ORGANIZATION.toString(), orgId);
    }

    @Override
    public void setOrganizationDefaultWeeklyPlanTemplate(Organization org) {
        WeeklyPlanTemplateDTO dto = getWeeklyPlanTemplateByOrganization(org.getId());
        String [] templateItemContent={"Plans","Done","Problems"};
        String [] templateItemColor={"ccolor7","ccolor11","ccolor5"};
        Boolean [] templateItemNeedCarry={true,false,true};
        if(dto==null){
            List<SaveWeeklyPlanTemplateItemDTO> items=new ArrayList<>();
            for (int i=0;i<templateItemContent.length;i++){
                SaveWeeklyPlanTemplateItemDTO itemDTO=new SaveWeeklyPlanTemplateItemDTO();
                itemDTO.setColor(templateItemColor[i]);
                itemDTO.setName(templateItemContent[i]);
                itemDTO.setNeedCarry(templateItemNeedCarry[i]);
                itemDTO.setOrder(i+1);
                items.add(itemDTO);
            }


            SaveWeeklyPlanTemplateDTO saveDto=new SaveWeeklyPlanTemplateDTO();
            saveDto.setType("ORGANIZATION");
            saveDto.setTemplateName(org.getName());
            saveDto.setEntityId(org.getId());
            saveDto.setItems(items);
            saveDto.setId(-1L);
            insertTemplate(saveDto);
        }
    }

    @Override
    public WeeklyPlanTemplateDTO getWeeklyPlanTemplateById(Long templateId) {
        WeeklyPlanTemplate template = this.weeklyPlanTemplateDAO.selectByPrimaryKey(templateId);
        if(template==null){
            return null;
        }
        WeeklyPlanTemplateDTO dto = new WeeklyPlanTemplateDTO();
        BeanConvertUtils.copyEntityProperties(template, dto);
        List<WeeklyPlanTemplateItem> itemList = weeklyPlanTemplateItemDAO.selectByTemplateId(dto.getId());
        if (!itemList.isEmpty()) {
            dto.setItems(itemList.stream().map(m -> {
                WeeklyPlanTemplateItemDTO d = new WeeklyPlanTemplateItemDTO();
                BeanConvertUtils.copyEntityProperties(m, d);
                return d;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 插入week plan 模板数据
     *
     * @param saveDto
     */
    @Override
    @Transactional
    public void saveWeeklyPlanTemplate(SaveWeeklyPlanTemplateDTO saveDto) {
        if(!organizationService.hasPermission(saveDto.getEntityId(), PrivilegeTypeEnum.UPDATABLE)){
            throw new CustomerException("Has no Permission, Update weekly plan template should be owner");
        }
        if (saveDto.getId() > 0) {
            updateTemplate(saveDto);
        } else {
            insertTemplate(saveDto);
        }
    }

    @Override
    public void deleteWeeklyPlanTemplateItem(Long organizationId, Long itemId) {
        if(!organizationService.hasPermission(organizationId,PrivilegeTypeEnum.UPDATABLE)){
            throw new CustomerException("Has no Permission, Update weekly plan template should be owner");
        }else{
            weeklyPlanTemplateItemDAO.deleteByPrimaryKey(itemId);
       }
    }

    private void insertTemplate(SaveWeeklyPlanTemplateDTO saveDto) {
        WeeklyPlanTemplateDTO weeklyPlanTemplateDTO = getWeeklyPlanTemplateByTypeAndId(saveDto.getType(), saveDto.getEntityId());
        if (weeklyPlanTemplateDTO != null) {
            throw new CustomerException(OkrResultEnum.CANNOTINSERTWEEKTEMPLATEDUPLICATE);
        }

        WeeklyPlanTemplate addTemplate = new WeeklyPlanTemplate();
        BeanConvertUtils.copyEntityProperties(saveDto, addTemplate);
        addTemplate.setEnable((byte) 0);
        addTemplate.initializeForInsert();
        weeklyPlanTemplateDAO.insert(addTemplate);

        List<WeeklyPlanTemplateItem> itemList = BeanConvertUtils.batchTransform(WeeklyPlanTemplateItem.class, saveDto.getItems());
        if (!itemList.isEmpty()) {
            for (WeeklyPlanTemplateItem item : itemList
            ) {
                item.setTemplateId(addTemplate.getId());
                item.initializeForInsert();
                weeklyPlanTemplateItemDAO.insert(item);
            }
        }
    }

    private void updateTemplate(SaveWeeklyPlanTemplateDTO saveDto) {
        WeeklyPlanTemplate updateTemplate = new WeeklyPlanTemplate();
        BeanUtils.copyProperties(saveDto, updateTemplate);
        updateTemplate.initializeForUpdate();
        weeklyPlanTemplateDAO.updateByPrimaryKeySelective(updateTemplate);

        List<WeeklyPlanTemplateItem> itemList = BeanConvertUtils.batchTransform(WeeklyPlanTemplateItem.class, saveDto.getItems());
        if (!itemList.isEmpty()) {
            List<WeeklyPlanTemplateItem> updateItemList = itemList.stream().filter(m -> m.getId() > 0).collect(Collectors.toList());
            if (!updateItemList.isEmpty()) {
                for (WeeklyPlanTemplateItem item : updateItemList
                ) {
                    item.setTemplateId(updateTemplate.getId());
                    item.initializeForUpdate();
                    weeklyPlanTemplateItemDAO.updateByPrimaryKeySelective(item);
                }
            }

            List<WeeklyPlanTemplateItem> addItemList = itemList.stream().filter(m -> m.getId() == 0 || m.getId() == null).collect(Collectors.toList());
            if (!addItemList.isEmpty()) {
                for (WeeklyPlanTemplateItem item : addItemList
                ) {
                    item.setTemplateId(updateTemplate.getId());
                    item.initializeForInsert();
                    weeklyPlanTemplateItemDAO.insert(item);
                }
            }
        }
    }

    private WeeklyPlanTemplateDTO getWeeklyPlanTemplateByTypeAndId(String type, Long entityId) {
        WeeklyPlanTemplate weeklyPlanTemplate = weeklyPlanTemplateDAO.selectByTypeAndEntityId(type, entityId);
        if (weeklyPlanTemplate == null) {
            return null;
        }
        WeeklyPlanTemplateDTO dto = new WeeklyPlanTemplateDTO();
        BeanConvertUtils.copyEntityProperties(weeklyPlanTemplate, dto);
        List<WeeklyPlanTemplateItem> itemList = weeklyPlanTemplateItemDAO.selectByTemplateId(dto.getId());
        if (!itemList.isEmpty()) {
            dto.setItems(itemList.stream().map(m -> {
                WeeklyPlanTemplateItemDTO d = new WeeklyPlanTemplateItemDTO();
                BeanConvertUtils.copyEntityProperties(m, d);
                return d;
            }).collect(Collectors.toList()));
        }
        return dto;
    }


}
