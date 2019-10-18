package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.bill.ListBillRequestDTO;
import com.coreteam.okr.entity.Bill;

import java.util.List;

/**
 * BillDAO继承基类
 */
public interface BillDAO extends MyBatisBaseDao<Bill, Long> {

    List<Bill> listBills(ListBillRequestDTO reqeust);
}