package com.coreteam.okr.service;

/**
 * @ClassName: LockService
 * @Description
 * @Author sean.deng
 * @Date 2019/04/18 11:48
 * @Version 1.0.0
 */
public interface LockService {
    boolean tryLock(String key);
    void unLock(String key);
}
