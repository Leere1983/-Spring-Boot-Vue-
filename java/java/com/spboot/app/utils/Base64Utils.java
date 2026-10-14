package com.spboot.app.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64 编解码类
 */
public class Base64Utils {

    private static final String CHARSET = "utf-8";

    /**
     * 编码数据为 base64
     * @param str
     * @return
     */
    public static String encode(String str) {
        try {
            return encode(str.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编码数据为 base64
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        try {
            return new String(Base64.getEncoder().encode(bytes), CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码数据
     * @param bytes
     * @return
     */
    public static String decode(byte[] bytes) {
        byte[] bytes1 = Base64.getDecoder().decode(bytes);

        try {
            return new String(bytes1, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码数据
     * @param data
     * @return
     */
    public static String decode(String data) {
        try {
            return decode(data.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
