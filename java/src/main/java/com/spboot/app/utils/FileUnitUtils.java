package com.spboot.app.utils;

import java.text.DecimalFormat;

/**
 * 文件数据单位转换,从字节转化为带单位的可视化数据
 * 从单位中转换为电脑看得懂的数字
 */
public class FileUnitUtils {

    /**
     * 将2MB、100MB、1G这样的可视化字符串转换成长整数类型，方便进行比较
     * @param size 可视化数字
     * @return
     */
    public static long unitToLong(String size) {
        String s = size.replaceAll(",", "").toLowerCase();
        char f = s.charAt(s.length() - 1);
        String sizeDouble = size.substring(0, s.length() - 1);
        if (f == 'b') {
            f = s.charAt(s.length() - 2);
            if (!(f >= '0' && f <= '9')) {
                sizeDouble = size.substring(0, s.length() - 2);
            }
        }
        Double res = Double.valueOf(sizeDouble);
        switch (f) {
            case 't':
                res *= 1024;
            case 'g':
                res *= 1024;
            case 'm':
                res *= 1024;
            case 'k':
                res *= 1024;
        }
        return res.longValue();
    }

    /**
     * 将数字长度转换为可视化的形式,如1024 转换为1 KB,2*1024*1024 转换为2MB
     * @param size 长度
     * @return
     */
    public static String longToUnit(long size) {
        // 将Size 转化为带单位的字节
        String[] uints = { "", "KB", "MB", "GB", "TB" };
        int i = 0;
        double s = Double.valueOf(size);
        for (i = 0; i < uints.length && s > 1024; i++) {
            s /= 1024;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###.####");
        return String.format("%s %s", decimalFormat.format(s), uints[i]);
    }
}
