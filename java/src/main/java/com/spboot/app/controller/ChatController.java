package com.spboot.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;

import com.spboot.app.pojo.Siliao;
import com.spboot.app.pojo.Xiaoxi;
import com.spboot.app.utils.Info;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import com.spboot.app.websocket.WebSocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @RequestMapping("/check")
    public synchronized R<Object> check(@RequestBody Map<String, Object> req)
    {
        if(!SessionFactory.checkLogin()){
            return R.error("尚未登录");
        }

        JSONObject jsonObject = new JSONObject(req);
        String sid = jsonObject.getString("sid");
        String rid = jsonObject.getString("rid");

        Map siliao = DB.name("siliao").where("(faxinren = '"+sid+"' AND shouxinren='"+rid+"') OR (faxinren = '"+rid+"' AND shouxinren='"+sid+"')").find();
        if(siliao == null){
            siliao = new HashMap();
            siliao.put("faxinren" , sid);
            siliao.put("shouxinren" , rid);
            siliao.put("bianhao" , Info.getID());
            siliao.put("addtime" , Info.getDateStr());
            int id = DB.name("siliao").insert(siliao);
            siliao.put("id" , id);
        }
        WebSocketUtils.formatSession(siliao, SessionFactory.getUsername());
        return R.success(siliao);
    }

    @RequestMapping("/checkmine")
    public R<Object> checkmine(@RequestParam String sid){
        return R.success(ChatController.getUser(sid));
    }

    public static Map getUser(String u)
    {
        return WebSocketUtils.getUser(u);
    }

    @RequestMapping("/updateRead")
    // 更新为已读
    public R<Object> updateRead(@RequestBody Map<String, Object> req)
    {
        JSONObject jsonObject = new JSONObject(req);
        String chatid = jsonObject.getString("chatid");
        String sid = jsonObject.getString("sid");
        DB.name(Xiaoxi.class).where("siliaoid" , chatid).where("fasongren" , "!=" , sid).setField("shifouzhakan","是");
        //DB.name(Zaixiangoutong.class).where("id",chatid).setField("xiaoxizuihoushijian" , Info.getDateStr());
        return R.success("ok");
    }

    public static String getString(JSONObject obj , String name)
    {
        return getString(obj,name,"");
    }
    public static String getString(JSONObject obj , String name , String def)
    {
        if(StringUtil.isNullOrEmpty(obj.getString(name))){
            return def;
        }
        return obj.getString(name);
    }
    public static int getInt(JSONObject obj , String name)
    {
        return getInt(obj,name,0);
    }
    public static int getInt(JSONObject obj , String name , int def)
    {
        if(StringUtil.isNullOrEmpty(obj.getString(name))){
            return def;
        }
        return obj.getIntValue(name);
    }

    @RequestMapping("/chatMessage")
    public R<List> chatMessage(@RequestBody Map<String, Object> req)
    {
        JSONObject jsonObject = new JSONObject(req);
        String chatid = jsonObject.getString("chatid");
        int pageNumber = getInt(jsonObject,"pageNumber" , 1);
        int pageSize = getInt(jsonObject,"pageSize" , 20);
        String offsetMax = getString(jsonObject,"offsetMax" , "");
        String offsetMin = getString(jsonObject,"offsetMin" , "");
        String type = getString(jsonObject,"type");
        List list = new ArrayList();
        if(type.equals("one")){
            // 第一次加载
            list = DB.name(Xiaoxi.class).where("siliaoid",chatid).order("id desc").limit(pageSize).select();
        }else if(type.equals("top")){
            list = DB.name(Xiaoxi.class).where("siliaoid" , chatid)
                    .order("id desc").limit(pageSize)
                    .where("id" ,"<" , offsetMin).select();
        }else if(type.equals("new")){
            list = DB.name(Xiaoxi.class).where("siliaoid" , chatid)
                    .order("id desc").limit(pageSize)
                    .where("id" ,">" , offsetMax).select();
        }

        return R.success(list);
    }
}
