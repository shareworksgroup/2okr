package com.coreteam.okr.web.config;

import com.coreteam.okr.common.util.NamedThreadFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @ClassName: ScheduleConfig
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/05 10:28
 * @Version 1.0.0
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(5, new NamedThreadFactory("okr-schedule")));
    }
}
