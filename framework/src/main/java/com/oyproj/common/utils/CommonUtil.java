package com.oyproj.common.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author oywq3000
 * @since 2026-01-23
 */
public class CommonUtil {
    public static final String BASE_NUMBER = "0123456789";
    /**
     * 以UUID重命名
     */
    public static String rename(String fileName){
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-","")+extName;
    }

    /**
     * 随机6位数生成
     */
    public static String getRandomNum(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<6;i++){
            int num = ThreadLocalRandom.current().nextInt(BASE_NUMBER.length());
            sb.append(num);
        }
        return sb.toString();
    }
    /**
     * 获取特定字符+6位随机数
     */
    public static String getSpecialStr(String value){
        return value+getRandomNum();
    }
}
