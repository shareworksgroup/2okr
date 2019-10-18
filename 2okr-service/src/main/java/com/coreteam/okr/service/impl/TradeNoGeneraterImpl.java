package com.coreteam.okr.service.impl;

import com.coreteam.okr.service.TradeNoGenerater;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName: TradeNoGeneraterImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/30 13:40
 * @Version 1.0.0
 */
@Service
public class TradeNoGeneraterImpl implements TradeNoGenerater {

    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
    Random random = new Random();

    @Override
    public Long generateTradeNo() {
        return Long.valueOf(sdf.format(new Date()) + random.nextInt(10));
    }
}
