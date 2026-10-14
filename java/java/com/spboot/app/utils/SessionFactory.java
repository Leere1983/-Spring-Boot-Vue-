package com.spboot.app.utils;

import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.utils.StringUtil;

/**
 * 当前session会话处理,系统运行的时候是多线程方式运行的,所以需要设置当前的线程运行时的session会话记录,以保证上下文中的session变量的正确性
 */
public class SessionFactory {

    // 使用 ThreadLocal 线程变量来保存session,保证下文运行时获取到的session数据都是一致的
    private static final ThreadLocal<Session> content = new ThreadLocal<Session>();

    // 设置session
    public static void set(Session session) {
        content.set(session);
    }

    // 获取session
    public static Session get() {
        return content.get();
    }

    // 删除session
    public static void remove() {
        content.remove();
    }

    public static String getUsername() {
        Session session = get();
        if (session != null) {
            return get().getUsername();
        }
        return null;
    }

    public static boolean checkLogin() {
        return !StringUtil.isNullOrEmpty(getUsername());
    }

    public static JSONObject getObject() {
        Session session = get();
        if (session != null) {
            return get().getObject();
        }
        return new JSONObject();
    }

    public static String getCx() {
        Session session = get();
        if (session != null) {
            return get().getCx();
        }
        return null;
    }

    public static String getTable() {
        Session session = get();
        if (session != null) {
            return get().getTable();
        }
        return null;
    }

    public static String getRoles() {
        Session session = get();
        if (session != null) {
            return get().getRoles();
        }
        return null;
    }

    public static Integer getId() {
        Session session = get();
        if (session != null) {
            return get().getId();
        }
        return null;
    }
}
