package com.rocky.dd.ctrlapp.info;

/**
 * Created by Administrator on 2016/1/21.
 */
public class Imei {

    public static String get() {
        StringBuilder sb = new StringBuilder();
        // IMEI由15位数字组成
        for(int i=0;i<15;i++) {
            sb.append(Constant.number[((int) (Math.random() * 50) % 10)]);
        }
        return sb.toString();
    }
}
