package com.rocky.dd.ctrlapp.info;

/**
 * Created by Administrator on 2016/1/21.
 */
public class AndroidID {
    /**
     * 获取新的android_id
     *
     * @return android_id字符串
     */
    public static String get() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            id.append(Constant.OX16[((int) (Math.random() * 100) % 16)]);
        }
        return id.toString();
    }
}
