package com.coreteam.okr.service.impl;

import com.coreteam.okr.service.OrderNoGenerater;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName: OrderNoGeneraterImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/30 11:38
 * @Version 1.0.0
 */
@Service
public class OrderNoGeneraterImpl implements OrderNoGenerater {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
    Random random =new Random();
    @Override
    public Long generateOrderNo() {
        return Long.valueOf(sdf.format(new Date())+random.nextInt(10));
    }

}
