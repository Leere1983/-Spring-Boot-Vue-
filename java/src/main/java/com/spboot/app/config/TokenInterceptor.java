package com.spboot.app.config;

import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.utils.Info;
import com.spboot.app.utils.JwtTokenUtils;
import com.spboot.app.utils.Session;
import com.spboot.app.utils.SessionFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在这里设置cors跨域请求处理
        if (!StringUtil.isNullOrEmpty(request.getHeader("Origin")) && response.getHeader("Access-Control-Allow-Origin") == null) {
            // 设置允许的连接地址、默认为提交的地址
            response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            // 设置允许的提交类型
            response.addHeader("Access-Control-Allow-Methods", "POST, GET ,OPTIONS, PUT,DELETE");
            // 设置允许的头提交
            response.addHeader("Access-Control-Allow-Headers", "x-requested-with,token,content-type,auth-token,app-http,Authorization");
            // 允许cookie 提交
            response.addHeader("Access-Control-Allow-Credentials", "true");
            // 有效时长
            response.addHeader("Access-Control-Max-Age", Integer.toString(86400 * 7));

            // 如果是OPTIONS 方式提交，则这个后续可以不需要提交，这个是浏览器提交前进行的测试提交、并获取是否有cors、只需要直接返回就好
            if ("OPTIONS".equals(request.getMethod().toUpperCase())) { // 这个方法只是试探，所以需要直接返回信息
                PrintWriter writer = null;
                try {
                    writer = response.getWriter();
                    writer.println("ok");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        List<Map> select = DB.select("SELECT * FROM shangpinxinxi WHERE jieshushijian<'" + Info.date("yyyy-MM-dd HH:mm:ss", Info.time()) + "' AND shangpinzhuangtai='拍卖中' ");
        if(select.size()>0){

            for (Map map : select) {
                long count = DB.name("jingpaijilu").where("shangpinxinxiid", map.get("id")).count();
                if(count == 0){
                    DB.name("shangpinxinxi").where("id" ,map.get("id")).setField("shangpinzhuangtai" , "已流拍");
                }else{
                    DB.name("shangpinxinxi").where("id" ,map.get("id")).setField("shangpinzhuangtai" , "已结束");
                }
                //DB.execute("UPDATE xuesheng SET jifen=jifen-10 WHERE xuehao='"+map.get("yuyueren")+"'");
            }
        }



        // 获取提交的token、检测是否有
        String token = request.getHeader("token");
        if (!StringUtil.isNullOrEmpty(token)) {
            /**
             * 原子对象操作
             */
            try {
                AtomicReference<Session> user = new AtomicReference();
                // token 数据解析，使用jwt token 进行数据解析
                jwtTokenUtils.getClaimFromToken(
                    token,
                    map -> {
                        // 将解析出来的数据
                        user.set(JSONObject.parseObject(JSONObject.toJSONString(map), Session.class));
                        return null;
                    }
                );
                // 对数据写入session 会话当中
                Session session = user.get();
                // 更新获取到的session 数据
                session.refresh();
                // 写入到当前session 工厂类
                SessionFactory.set(session);
            } catch (Exception e) {
                // 解析出错了，则置null
                SessionFactory.set(null);
            }
        } else {
            // 无数据进行置null 处理，覆盖之前的设置，多线程下这个是必须设置的
            SessionFactory.set(null);
        }
        return true;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //         System.out.println("执行了TestInterceptor的postHandle方法");
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //        System.out.println("执行了TestInterceptor的afterCompletion方法");
    }
}
