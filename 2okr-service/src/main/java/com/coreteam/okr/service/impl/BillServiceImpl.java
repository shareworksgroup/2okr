package com.coreteam.okr.service.impl;

import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.BillStatusEnum;
import com.coreteam.okr.dao.BillDAO;
import com.coreteam.okr.dto.bill.BillCreateRequestDTO;
import com.coreteam.okr.dto.bill.BillDTO;
import com.coreteam.okr.dto.bill.ListBillRequestDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.entity.Bill;
import com.coreteam.okr.entity.Objective;
import com.coreteam.okr.service.BillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: BillServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 16:48
 * @Version 1.0.0
 */
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillDAO billDAO;


    @Override
    public PageInfo<BillDTO> listBills(ListBillRequestDTO reqeust) {
        PageHelper.startPage(reqeust.getPageNumber(), reqeust.getPageSize());
        List<Bill> list = this.billDAO.listBills(reqeust);
        PageInfo pageInfo = new PageInfo(list);
        if (!CollectionUtils.isEmpty(list)) {
            List<BillDTO> result = BeanConvertUtils.batchTransform(BillDTO.class, list);
            pageInfo.setList(result);
        }
        return pageInfo;
    }

    @Override
    public void createBill(BillCreateRequestDTO reqeust) {
        Bill bill = new Bill();
        BeanConvertUtils.copyEntityProperties(reqeust, bill);
        bill.setBillStatus(BillStatusEnum.PAID);
        bill.initializeForInsert();
        this.billDAO.insertSelective(bill);

    }

}
