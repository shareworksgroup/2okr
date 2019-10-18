package com.coreteam.okr.service.impl;

import com.coreteam.core.dto.PageBaseDTO;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dao.DemoDAO;
import com.coreteam.okr.dto.demo.GetDemoByIdResponseDTO;
import com.coreteam.okr.dto.demo.GetPagedDemoListResponstDTO;
import com.coreteam.okr.dto.demo.InsertDemoRequestDTO;
import com.coreteam.okr.entity.Demo;
import com.coreteam.okr.service.DemoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDAO demoDAO;

    @Override
    public GetDemoByIdResponseDTO getDemoById(int id) {
        Demo demo = demoDAO.selectByPrimaryKey(id);
        GetDemoByIdResponseDTO dto = new GetDemoByIdResponseDTO();
        BeanConvertUtils.copyEntityProperties(demo, dto);
        return dto;
    }

    @Override
    public void insertDemo(InsertDemoRequestDTO requestDTO) {
        Demo demo = new Demo();
        BeanConvertUtils.copyEntityProperties(requestDTO, demo);
        demo.setGmtCreate(LocalDateTime.now());
        demo.setGmtCreateBy(0);
        demo.setGmtModified(LocalDateTime.now());
        demo.setGmtModifiedBy(0);

        demoDAO.insert(demo);
    }

    @Override
    public PageInfo<GetPagedDemoListResponstDTO> getPagedDemoList(PageBaseDTO pageBaseDTO) {
        PageHelper.startPage(pageBaseDTO.getPageNumber(), pageBaseDTO.getPageSize());
        List<Demo> demoList = demoDAO.selectList();
        List<GetPagedDemoListResponstDTO> pageList = BeanConvertUtils.batchTransform(GetPagedDemoListResponstDTO.class, demoList);
        return new PageInfo<>(pageList);
    }
}
