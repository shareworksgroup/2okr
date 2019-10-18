package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Payment;

/**
 * PaymentDAO继承基类
 */
public interface PaymentDAO extends MyBatisBaseDao<Payment, Long> {
    Payment getPaymentByTradeNo(Long valueOf);
}