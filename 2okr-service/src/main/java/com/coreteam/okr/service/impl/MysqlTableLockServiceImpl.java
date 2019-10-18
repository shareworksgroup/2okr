package com.coreteam.okr.service.impl;

import com.coreteam.okr.dao.MethodLockDAO;
import com.coreteam.okr.entity.MethodLock;
import com.coreteam.okr.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MysqlTableLockServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 11:50
 * @Version 1.0.0
 */
@Service
public class MysqlTableLockServiceImpl implements LockService {

    @Override
    public boolean tryLock(String key) {
        try {
            MethodLock methodLock=new MethodLock();
            methodLock.setMethodName(key);
            methodLock.initializeForInsert();
            this.lockDAO.insertSelective(methodLock);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void unLock(String key) {
        this.lockDAO.deleteByMethodName(key);
    }

    @Autowired
    private MethodLockDAO lockDAO;

}
