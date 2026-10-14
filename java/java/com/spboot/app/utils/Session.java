package com.spboot.app.utils;

import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.Map;

/**
 * 保存用户会话信息的数据
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    // 用户id
    private Integer id;
    // 角色
    private String cx;
    // 所属表
    private String table;
    // 用户名
    private String username;
    // 密码
    private String pwd;
    // 登录类型
    private String roles;

    // 角色数据
    private JSONObject object;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public JSONObject getObject() {
        if (object == null && table != null && id != null) {
            setObject(DB.json(table).find(id));
        }
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public void setObject(Map object) {
        this.object = new JSONObject(object);
    }

    public boolean isEnabled() {
        if (object.containsKey("issh")) {
            if (object.getString("issh").equals("否")) {
                return false;
            }
        }
        return true;
    }

    public void refresh() {
        if (getId() != null && getTable() != null) {
            Map map = DB.name(getTable()).find(getId());
            setObject(map);
        }
    }
}
