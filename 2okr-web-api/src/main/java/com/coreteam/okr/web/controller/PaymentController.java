package com.coreteam.okr.web.controller;

import com.coreteam.okr.dto.payment.PaymentRequestDTO;
import com.coreteam.okr.dto.payment.PaymentResponseDTO;
import com.coreteam.okr.service.PaymentService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: PaymentController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/26 13:46
 * @Version 1.0.0
 */
@RestController
@RequestMapping("payment")
@AuditLogAnnotation(value = "payment接口")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @ApiOperation("创建支付需求")
    public PaymentResponseDTO requestForPayment(@Valid @RequestBody PaymentRequestDTO request) {
        return paymentService.requestForPayment(request);
    }

    @PostMapping(value = "callback/alipay")
    @ApiOperation("创建支付需求")
    public void alipayCallback(HttpServletRequest request) {
        Map<String, String> params = toMap(request);
        paymentService.handleAlipayCallBack(params);
    }

    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }



}
