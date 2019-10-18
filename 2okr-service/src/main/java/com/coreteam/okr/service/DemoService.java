package com.coreteam.okr.service;

import com.coreteam.core.dto.PageBaseDTO;
import com.coreteam.okr.dto.demo.GetDemoByIdResponseDTO;
import com.coreteam.okr.dto.demo.GetPagedDemoListResponstDTO;
import com.coreteam.okr.dto.demo.InsertDemoRequestDTO;
import com.github.pagehelper.PageInfo;

public interface DemoService {
    GetDemoByIdResponseDTO getDemoById(int id);

    void insertDemo(InsertDemoRequestDTO requestDTO);

    PageInfo<GetPagedDemoListResponstDTO> getPagedDemoList(PageBaseDTO pageBaseDTO);
}
