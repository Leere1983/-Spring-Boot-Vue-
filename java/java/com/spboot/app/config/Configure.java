package com.spboot.app.config;

// 常亮设置
public class Configure {

    // 获取系统的运行时的根路径
    public static final String ROOT_DIR = System.getProperty("user.dir");
    // 设置上传文件的保存路径、默认为当前运行环境的upload 目录
    public static final String UPLOAD_DIR = System.getProperty("user.dir") + "/upload/";
    // 上传后的访问路径前缀
    public static final String FILE_PREFIX = "/upload/";
    // 设置是否为 md5 加密
    public static final boolean isMd5 = false;
}
