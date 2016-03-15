package com.rocky.dd.ctrlapp.info;

/**
 * Created by Administrator on 2016/2/22.
 */
public class WifiName {
    /**
     * 获取新的android_id
     *
     * @return android_id字符串
     */
    public static String get() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int number = (int) (Math.random() * 127);
            if(48 < number && number <  57) {

            } else if(64 < number && number <  90) {

            } else if(95 < number && number <  122) {

            } else {
                continue;
            }
            id.append((char)number);
        }
        return id.toString();
    }
}
