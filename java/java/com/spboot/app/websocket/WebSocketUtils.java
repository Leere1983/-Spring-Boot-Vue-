package com.spboot.app.websocket;

import com.jntoo.db.DB;
import com.spboot.app.pojo.Xiaoxi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSocketUtils {
    public static Map formatSession(Map map, String sid){
        String u = String.valueOf(sid.equals(map.get("faxinren")) ? map.get("shouxinren") : map.get("faxinren"));
        Map user = getUser(u);
        if( user != null ) {
            map.put("user" , user);
            // 未读取数
            long readCount = DB.name(Xiaoxi.class).where("siliaoid" , map.get("id")).where("fasongren" , "!=" , sid).where("shifouzhakan","否").count();
            map.put("readCount" , readCount);
            return map;
        }
        return null;
    }

    public static List<Map> formatSessionList(List<Map> list , String sid){
        List<Map> result = new ArrayList();
        if(list != null && list.size() > 0){
            for (Map map : list) {
                Map map1 = formatSession(map,sid);
                if(map1 != null){
                    result.add(map1);
                }
            }
        }
        return result;
    }


    public static Map getUser(String u)
    {
        //员工
        Map user = DB.name("yonghu").where("yonghuming" , u).find();
        if(user != null){
//            user.put("xingming","管理员");
//            user.put("touxiang","/upload/img.png");
            return user;
        }

        //管理员
        user = DB.name("admins").where("username" , u).find();
        if(user != null)
        {
            user.put("xingming","管理员");
            user.put("touxiang","/upload/img.png");
            return user;
        }
      user = DB.name("shangjia").where("zhanghao" , u).find();
       if(user != null){
           user.put("xingming",user.get("dianpumingcheng"));

           user.put("touxiang","/upload/img.png");

           return user;
        }
//        user = DB.name("touziren").where("zhanghuming" , u).find();
//        if(user != null){
//            return user;
//        }
        return null;
    }
}
