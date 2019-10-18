package com.coreteam.okr.manager.impl;

import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dao.BussinessLogDAO;
import com.coreteam.okr.dao.query.BussinessLogPageQuery;
import com.coreteam.okr.dto.log.BussinessLogDTO;
import com.coreteam.okr.dto.log.GetPagedBussinessLogDTO;
import com.coreteam.okr.dto.log.InsertBussinessLogDTO;
import com.coreteam.okr.entity.BussinessLog;
import com.coreteam.okr.manager.BussinessLogManager;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: BussinessLogManagerImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 15:20
 * @Version 1.0.0
 */
@Service
public class BussinessLogManagerImpl implements BussinessLogManager {

    @Autowired
    private BussinessLogDAO dao;

    @Override
    public Long insertLog(InsertBussinessLogDTO insertBussinessLogDTO) {
        BussinessLog log = new BussinessLog();
        BeanConvertUtils.copyEntityProperties(insertBussinessLogDTO, log);
        log.initializeForInsert();

        this.dao.insert(log);

        return log.getId();
    }

    @Override
    public PageInfo<BussinessLogDTO> pageListLogs(GetPagedBussinessLogDTO queryDTO) {
        BussinessLogPageQuery query = new BussinessLogPageQuery();
        BeanConvertUtils.copyEntityProperties(queryDTO, query);
        PageHelper.startPage(query.getPageNumber(), query.getPageSize());
        List<BussinessLog> logList = this.dao.pageListLogs(query);
        PageInfo pageInfo=new PageInfo(logList);
        pageInfo.setList(BeanConvertUtils.batchTransform(BussinessLogDTO.class, logList));
        return pageInfo;
    }
}
