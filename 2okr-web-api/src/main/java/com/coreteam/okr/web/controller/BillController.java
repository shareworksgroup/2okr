package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.bill.BillDTO;
import com.coreteam.okr.dto.bill.ListBillRequestDTO;
import com.coreteam.okr.service.BillService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: BillController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/03 16:59
 * @Version 1.0.0
 */
@RestController
@RequestMapping("bill")
@AuditLogAnnotation(value = "payment接口")
@Slf4j
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/list")
    @ApiOperation("分页获取bill列表")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public PageInfo<BillDTO> listBills(Long organizationId, Integer pageNumber, Integer pageSize) {
        ListBillRequestDTO request = new ListBillRequestDTO();
        request.setOrganizationId(organizationId);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        return billService.listBills(request);
    }
}
