package com.spboot.app.utils;

import java.util.HashMap;

/**
 * 返回前端数据公共类库工具
 * 结构如下：
 * {"code":0,"msg":"",data:"" }
 * code 错误代码，0则无错误
 * msg  错误的消息
 * data 成功的时候返回的数据
 */
public class R<T> extends HashMap<String, Object> {

    public R() {}

    public R(Integer code, String msg, T data) {
        set("code", code);
        set("msg", msg);
        set("data", data);
    }

    public R(String msg) {
        this(1, msg, null);
    }

    public R(T data) {
        this(0, null, data);
    }

    /**
     * 链式设置数据
     * @param name
     * @param value
     * @return
     */
    public R<T> set(String name, Object value) {
        this.put(name, value);
        return this;
    }

    /**
     * 返回成功的数据
     * @param data  成功的数据
     * @param <T>
     * @return
     */
    public static <T> R<T> success(T data) {
        return new R(data);
    }

    /**
     * 返回错误数据
     * @param msg 错误的信息
     * @return
     */
    public static R<Object> error(String msg) {
        return new R(1, msg, null);
    }

    /**
     * 返回错误的数据
     * @param msg   错误的信息
     * @param code  错误码
     * @return
     */
    public static R<Object> error(String msg, int code) {
        return new R(code == 0 ? 1 : code, msg, null);
    }

    /**
     * 快捷的操作成功
     * @return
     */
    public static R ok() {
        return success("操作成功");
    }

    /**
     * 串联式数据
     * @return
     */
    public static R result() {
        return new R();
    }
}
