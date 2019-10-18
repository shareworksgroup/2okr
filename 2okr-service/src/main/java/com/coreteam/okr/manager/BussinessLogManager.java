package com.coreteam.okr.manager;

import com.coreteam.okr.dto.log.BussinessLogDTO;
import com.coreteam.okr.dto.log.GetPagedBussinessLogDTO;
import com.coreteam.okr.dto.log.InsertBussinessLogDTO;
import com.github.pagehelper.PageInfo;


/**
 * @ClassName: BussinessLogManager
 * @Description provider bussiness log service
 * @Author jianyong.jiang
 * @Date 2019/3/19 13:57
 * @Version 1.0.0
 */
public interface BussinessLogManager {

    Long insertLog(InsertBussinessLogDTO insertBussinessLogDTO);

    PageInfo<BussinessLogDTO> pageListLogs(GetPagedBussinessLogDTO query);
}
