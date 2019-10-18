package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.WeeklyReportSendRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: WeeklyReportSendRecordDAO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Mapper
public interface WeeklyReportSendRecordDAO extends MyBatisBaseDao<WeeklyReportSendRecord, Long> {

    void deleteBySenderReciverAndWeek(@Param("userId") Long userId, @Param("reciverId") Long reciverId, @Param("week") Integer week);

    void clearSendRecordByUser(@Param("userId") Long id, @Param("week") Integer week, @Param("weeklyPlanId") Long weeklyPlanId);
}