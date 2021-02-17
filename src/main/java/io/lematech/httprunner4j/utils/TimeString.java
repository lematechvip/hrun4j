package io.lematech.httprunner4j.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimeString {
    private  static String valueOfString(String str, int len) {
        String string = "";
        for (int i = 0; i < len - str.length(); i++) {
            string = string + "0";
        }
        return (str.length() == len) ? (str) : (string + str);
    }

    public static String getSimpleDateFormat(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getTimeString() {
        Calendar calendar = new GregorianCalendar();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = valueOfString(String.valueOf(calendar.get(Calendar.MONTH) + 1),2);
        String day = valueOfString(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),2);
        String hour = valueOfString(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2);
        String minute = valueOfString(String.valueOf(calendar.get(Calendar.MINUTE)),2);
        String second = valueOfString(String.valueOf(calendar.get(Calendar.SECOND)),2);
        String millisecond = valueOfString(String.valueOf(calendar.get(Calendar.MILLISECOND)),3);
        return year+month+day+hour+minute+second+millisecond;
    }

    //随机生成指定长度的中文字符
    public static String getRandomChineseWord(int length) {
        String str = "";
        int highPos, lowPos;
        Random random = new Random();
        for(int i=1;i<=length;i++){
            highPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = 161 + Math.abs(random.nextInt(93));
            byte[] b = new byte[2];
            b[0] = (new Integer(highPos)).byteValue();
            b[1] = (new Integer(lowPos)).byteValue();
            try {
                str= new String(b)+str;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }
    //随机生成指定长度的数值和字母组合
    public static String getRandomString(int length) {
        //待生成的字符组合
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i= 1; i <=length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    //随机生成指定长度的数字
    public static String getRandomNum(int length) {
        //待生成的字符组合
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i= 1; i <=length; i++) {
            int number = random.nextInt(base.length());
            char temp = base.charAt(number);
            if(i==1&&temp=='0'){
                temp = base.charAt(number);
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}
