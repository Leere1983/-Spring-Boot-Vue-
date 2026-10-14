package com.spboot.app.websocket;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.pojo.Siliao;
import com.spboot.app.pojo.Xiaoxi;
import com.spboot.app.utils.Info;
import com.spboot.app.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author： xxt
 * @date： 2022/5/23 16:27
 * @Description： WebSocket操作类
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketSever {
    private static final Logger log = LoggerFactory.getLogger(WebSocketSever.class);
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userId;
    private long heartbeatTime = 0;
    // session集合,存放对应的session
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketSever> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 建立WebSocket连接
     *
     * @param session
     * @param userId 用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        log.info("WebSocket建立连接中,连接用户ID：{}", userId);
        try {
            Session historySession = sessionPool.get(userId);
            // historySession不为空,说明已经有人登陆账号,应该删除登陆的WebSocket对象
            if (historySession != null) {
                webSocketSet.remove(historySession);
                historySession.close();
            }
        } catch (IOException e) {
            log.error("重复登录异常,错误信息：" + e.getMessage(), e);
        }
        // 建立连接
        this.userId = userId;
        this.session = session;
        this.heartbeatTime = Info.time();
        webSocketSet.add(this);
        sessionPool.put(userId, session);
        log.info("建立连接完成,当前在线人数为：{}", webSocketSet.size());
    }

    /**
     * 发生错误
     *e
     * @param throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        // session 池中删除对应的数据
        sessionPool.remove(this.userId);
        webSocketSet.remove(this);
        log.info("连接断开,当前在线人数为：{}", webSocketSet.size());
    }

    /**
     * 接收客户端消息
     *
     * @param message 接收的消息
     */
    @OnMessage
    public void onMessage(String message) {
        // 更新心跳
        this.heartbeatTime = Info.time();
        JSONObject object = JSON.parseObject(message);
        boolean isClose = false;
        try {
            Class<? extends WebSocketSever> aClass = this.getClass();
            Method method = aClass.getMethod(object.getString("cmd"), JSONObject.class);
            if(method != null){
                Object result = method.invoke(this,object);
                if(result != null){
                    if(result instanceof Message)
                    {
                        sendMessageByUser(this.userId,result);
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            isClose = true;
            log.info("没有找到该方法");
            e.printStackTrace();
        } catch (Exception e) {
            isClose = true;
            log.info("调用错误");
            e.printStackTrace();
        }
        if(isClose){
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("收到客户端发来的消息：{}", message);
    }

    public Message heartbeat(JSONObject jsonObject){
        return Message.ok();
    }


    public Message send(JSONObject jsonObject){
        Xiaoxi xiaoxi = new Xiaoxi();
        Xiaoxi map = BeanUtil.fillBeanWithMap(jsonObject.getJSONObject("data") , xiaoxi, true);
        DB.name(map).insert();
        DB.execute("UPDATE siliao SET addtime=now() WHERE id=?",map.getSiliaoid());
        Siliao siliao = DB.name(Siliao.class).find(map.getSiliaoid());
        String targetUserId = siliao.getFaxinren().equals(map.getFasongren()) ? siliao.getShouxinren() : siliao.getFaxinren();
        Map<String, Object> chat = BeanUtil.beanToMap(siliao);
        WebSocketUtils.formatSession(chat,targetUserId);

        R targetMsg = R.result().set("msg",map).set("chat",chat);
        Message msg = new Message("message",targetMsg);
        sendMessageByUser(targetUserId , msg); // 给对方发送消息

        // 给到发送方 一个发送完成的操作
        String job = jsonObject.getString("job");
        return new Message("message_job",R.result().set("job",job).set("msg",map));
    }


    /**
     * 登录后获取会话列表
     * @param jsonObject
     */
    public Message sessionList(JSONObject jsonObject){
        // 获取
        String sid = this.userId; //jsonObject.getString("sid");
        String type = !StringUtil.isNullOrEmpty(jsonObject.getString("type")) ? jsonObject.getString("type") : "one";
        //String lastdate = jsonObject.getString("lastdate");
        List<Map> list =  DB.name("Siliao").where("(faxinren = '"+sid+"' OR shouxinren='"+sid+"')").order("addtime desc").select();
        List result = WebSocketUtils.formatSessionList(list,sid);

        return new Message("sessionList",result);
    }




    /**
     * 推送消息到指定用户
     *
     * @param userId  用户ID
     * @param message 发送的消息
     */
    public static<T> void sendMessageByUser(String userId, T message) {
        log.info("用户ID：" + userId + ",推送内容：" + message);
        Session session = sessionPool.get(userId);
        if(session != null){
            try {
                String msg;
                if(message instanceof String){
                    msg = (String) message;
                }else{
                    msg = JSON.toJSONString(message);
                }
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("推送消息到指定用户发生错误：" + e.getMessage(), e);
            }
        }

    }

    /**
     * 群发消息
     *
     * @param message 发送的消息
     */
    public static void sendAllMessage(Object message) {
        String msg;
        if(message instanceof String){
            msg = (String) message;
        }else{
            msg = JSON.toJSONString(message);
        }
        log.info("发送消息：{}", message);
        for (WebSocketSever webSocket : webSocketSet) {
            try {
                webSocket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("群发消息发生错误：" + e.getMessage(), e);
            }
        }
    }
}
