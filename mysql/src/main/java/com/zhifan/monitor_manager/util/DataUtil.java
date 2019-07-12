package com.zhifan.monitor_manager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {


    public static String defautFormat = "yyyy-MM-dd HH:mm:ss";

    public static String defautFormat02 = "yyyy-MM-dd";

    public static Integer CHANGE_TYPE_ONE = 1;   //时间字符串格式时间戳

    public static Integer CHANGE_TYPE_TWO = 2;    //时间戳转时间字符串

    public static Integer CHANFE_TYPE_THREE = 3;   //当前时间转化字符串

    /**
     * 时间格式的转化，该函数将str1转化为想要的数据格式
     *
     * @param str1   时间戳字符串|时间字符串
     * @param format 转化格式
     * @return
     */
    public static String timeChangeType(String str1, String format, Integer type) {
        String resultStr = null;
        if (type.equals(CHANGE_TYPE_ONE)){
            resultStr = dataToLong(str1,format);
        }else if (type.equals(CHANGE_TYPE_TWO)){
            resultStr = longToData(str1,format);
        }else if (type.equals(CHANFE_TYPE_THREE)){
            resultStr = nowToData(format);
        }
        return resultStr;
    }

    private static String dataToLong(String dataStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return String.valueOf(sdf.parse(dataStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String longToData(String msec,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(new Date(Long.valueOf(msec)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String nowToData(String format){
        return longToData(String.valueOf(System.currentTimeMillis()),format);
    }


}
