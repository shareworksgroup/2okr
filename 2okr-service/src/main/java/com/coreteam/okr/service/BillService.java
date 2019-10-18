package com.coreteam.okr.service;


import com.coreteam.okr.dto.bill.BillCreateRequestDTO;
import com.coreteam.okr.dto.bill.BillDTO;
import com.coreteam.okr.dto.bill.ListBillRequestDTO;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: BillService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 15:49
 * @Version 1.0.0
 */
public interface BillService {

    PageInfo<BillDTO> listBills(ListBillRequestDTO reqeust);

    void createBill(BillCreateRequestDTO reqeust);

}
