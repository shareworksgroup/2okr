package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Order;

/**
 * OrderDAO继承基类
 */
public interface OrderDAO extends MyBatisBaseDao<Order, Long> {

    Order getOrderBySubscribeId(Long id);

    Order getOrderByOrderNo(Long no);
}