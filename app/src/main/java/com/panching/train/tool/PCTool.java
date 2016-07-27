package com.panching.train.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by panching on 16/4/28.
 */
public class PCTool {

    public  String dateDiff(String startTime, String endTime,  String format, String str) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;

        String returnStr = "";
        // 获得两个时间的毫秒时间差异
        try {
            String start =  startTime.substring(0,startTime.indexOf(":"));
            String end =  endTime.substring(0,endTime.indexOf(":"));
            int iStart = Integer.parseInt(start);
            int iEnd = Integer.parseInt(end);

            if(iEnd < iStart)
                endTime = (iEnd+24)+endTime.substring(endTime.indexOf(":"));

            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            if(hour<=0)
                returnStr = (min - day * 24 * 60) + "分";
            else
                returnStr = (hour - day * 24) + "小時" + (min - day * 24 * 60) + "分";
//returnStr = "时间相差：" + day + "天" + (hour - day * 24) + "小时"+ (min - day * 24 * 60) + "分钟" + sec + "秒。";
            if (str.equalsIgnoreCase("h"))
            {
                return String.valueOf(hour);
            }
            else if (str.equalsIgnoreCase("m")){
                return String.valueOf(min);
            }
            else
            {
                return returnStr;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("h"))
        {
            return String.valueOf(hour);
        }
        else if (str.equalsIgnoreCase("m")){
            return String.valueOf(min);
        }
        else
        {
            return returnStr;
        }
    }
}
