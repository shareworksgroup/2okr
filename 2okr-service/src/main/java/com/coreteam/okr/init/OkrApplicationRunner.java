package com.coreteam.okr.init;

import com.coreteam.okr.manager.SysDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OkrApplicationRunner
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 9:51
 * @Version 1.0.0
 */
@Component
public class OkrApplicationRunner implements ApplicationRunner {

    @Autowired
    private SysDictionaryManager dictionaryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.dictionaryService.init();
    }
}
