package com.coreteam.okr.common.util;

import com.coreteam.core.exception.CustomerException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtil
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:15
 * @Version 1.0.0
 */
public class DateUtil {
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当前日期的当年周数
     * @return 201915
     */
    public static Integer getCurrentWeek(){
        Calendar calendar=Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
        String week = weekNum<10?"0"+weekNum:String.valueOf(weekNum);
        return Integer.valueOf(year+week);
    }


    /**
     * 获取两个日期之前的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        if (year1 != year2)  //不同年
        {
            int timeDistance = 0;
            for (int i = year1 ; i < year2 ;i++){ //闰年
                if (i%4==0 && i%100!=0||i%400==0){
                    timeDistance += 366;
                }else { // 不是闰年
                    timeDistance += 365;
                }
            }
            return  timeDistance + (day2-day1);
        }else{// 同年
            return day2-day1;
        }

    }

    public static Date getFirstDateOfWeek(Integer week){
        String weekStr=String.valueOf(week).trim();
        if(weekStr.length()!=6){
            throw new CustomerException("week format error ,must like 201902");
        }
        Integer yearNum=Integer.valueOf(weekStr.substring(0,4));
        Integer weekNum=Integer.valueOf(weekStr.substring(4));
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.YEAR,yearNum);
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        cl.set(Calendar.WEEK_OF_YEAR,weekNum);
        cl.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        if(yearNum.equals(cl.get(Calendar.YEAR))){
            return cl.getTime();
        }else{
            cl.set(Calendar.YEAR,yearNum);
            cl.set(Calendar.MONTH,0);
            cl.set(Calendar.DAY_OF_MONTH,1);
            return cl.getTime();
        }

    }

    public static Date getEndDateOfWeek(Integer week){
        String weekStr=String.valueOf(week).trim();
        if(weekStr.length()!=6){
            throw new CustomerException("week format error ,must like 201902");
        }
        Integer yearNum=Integer.valueOf(weekStr.substring(0,4));
        Integer weekNum=Integer.valueOf(weekStr.substring(4));
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.YEAR,yearNum);
        cl.setFirstDayOfWeek(Calendar.MONDAY);
        cl.set(Calendar.WEEK_OF_YEAR,weekNum);
        cl.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        if(yearNum.equals(cl.get(Calendar.YEAR))){
            return cl.getTime();
        }else{
            cl.set(Calendar.YEAR,yearNum);
            cl.set(Calendar.MONTH,11);
            cl.set(Calendar.DAY_OF_MONTH,31);
            return cl.getTime();
        }

    }

    public static Date getDayBefore(Date date, Integer before ){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DATE,before);
        return calendar1.getTime();
    }

    public static Date getDayAfterFewMonth(Date date, Integer Month ){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.MONTH,Month);
        return calendar1.getTime();
    }

    public static Date getQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Integer getQuarter(){
        Calendar c = Calendar.getInstance();
        int currentMonth=c.get(Calendar.MONTH)+ 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            return 1;
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            return 2;
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            return 3;
        } else {
            return 4;
        }
    }

    public static Integer getYear(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static Integer getMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public static Integer getDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月的第一天
     *
     * @return date
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取当前月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTime();
    }


}
