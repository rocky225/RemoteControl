package com.rocky.dd.ctrlapp.info;

/**
 * Created by Administrator on 2016/2/22.
 */
public class SERIAL {
    public static String get() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            id.append(Constant.OX16[((int) (Math.random() * 100) % 16)]);
        }
        return id.toString();
    }
}
