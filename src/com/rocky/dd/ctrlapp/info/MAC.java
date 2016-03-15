package com.rocky.dd.ctrlapp.info;

/**
 * Created by Administrator on 2016/2/22.
 */
public class MAC {
    /**
     * 获取新的android_id
     *
     * @return android_id字符串
     */
    public static String get() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            id.append(Constant.OX16[((int) (Math.random() * 100) % 16)]);
            if(i % 2 == 1 && i < 11) {
                id.append(":");
            }
        }
        return id.toString();
    }
}
