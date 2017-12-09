package com.snail.collectioncode;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yongjie on 2017/12/9.
 */

public class CalenderIncrease {

    /**
     * 实现日期 从 20170101 到 20171206 连续的变化过程
     */
    public void test() {
        try {
            //定义起始日期
            Date startDate = new SimpleDateFormat("yyyyMMdd").parse("20170101");
            //定义结束日期
            Date endDate = new SimpleDateFormat("yyyyMMdd").parse("20171207");
            //定义日期实例
            Calendar dd = Calendar.getInstance();
            //设置日期起始时间
            dd.setTime(startDate);
            //判断是否到结束日期
            while (dd.getTime().before(endDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String day = sdf.format(dd.getTime());
                System.out.println("Day=" + Integer.parseInt(day));
                //进行当前日期月份加1
                dd.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
