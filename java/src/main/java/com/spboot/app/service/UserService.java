package com.spboot.app.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.jntoo.db.DB;
import com.jntoo.db.QueryMap;
import com.jntoo.db.model.Options;
import com.jntoo.db.utils.InfoUtil;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.pojo.*;
import com.spboot.app.utils.JwtTokenUtils;
import com.spboot.app.utils.R;
import com.spboot.app.utils.Session;
import com.spboot.app.utils.SessionFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Resource
    private AdminsService adminsService;

    @Resource
    private YonghuService yonghuService;

    @Resource
    private ShangjiaService shangjiaService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    /**
     * 用户登录
     * @param username 用户名
     * @param pwd  密码
     * @param cx 角色
     * @return
     */
    public R<Object> login(String username, String pwd, String cx) {
        // 判断是否填写用户,没有则提示请填写用户
        if (StringUtil.isNullOrEmpty(username)) {
            return R.error("请填写用户");
        }
        // 判断是否填写密码,没有则提示请填写密码
        if (StringUtil.isNullOrEmpty(pwd)) {
            return R.error("请填写密码");
        }
        // 判断是否选择某角色登录,没有则提示请选择角色
        if (StringUtil.isNullOrEmpty(cx)) {
            return R.error("请选择角色");
        }

        // 使用的表,将保存在session中
        String table = "";
        String roles = cx;
        // 创建session会话变量
        Session session = null;
        //pwd = passwordEncoder.encode(pwd);

        // 判断是否为"管理员"角色登录时则使用这个进行登录
        if (cx.equals("管理员")) {
            // 通过AdminsService.login 方法进行登录

            Admins user = adminsService.login(username, pwd);
            // user 为null 则提示用户账号或密码错误
            if (user == null) {
                return R.error("账号或密码错误");
            }

            // 设置table 变量
            // 将bean 实体类转化为session
            session = adminsService.toSession(user);
        }
        // 判断是否为"用户"角色登录时则使用这个进行登录
        if (cx.equals("用户")) {
            // 通过YonghuService.login 方法进行登录

            Yonghu user = yonghuService.login(username, pwd);
            // user 为null 则提示用户账号或密码错误
            if (user == null) {
                return R.error("账号或密码错误");
            }

            // 设置table 变量
            // 将bean 实体类转化为session
            session = yonghuService.toSession(user);
        }
        // 判断是否为"商家"角色登录时则使用这个进行登录
        if (cx.equals("商家")) {
            // 通过ShangjiaService.login 方法进行登录

            Shangjia user = shangjiaService.login(username, pwd);
            // user 为null 则提示用户账号或密码错误
            if (user == null) {
                return R.error("账号或密码错误");
            }
            if (!user.getIssh().equals("是")) {
                return R.error("账号审核中");
            }

            // 设置table 变量
            // 将bean 实体类转化为session
            session = shangjiaService.toSession(user);
        }

        // session 为null 则认为登录失败
        if (session == null) {
            return R.error("没有找到相关登录信息");
        }

        // 设置jwtToken 登录令牌
        Map<String, Object> m = BeanUtil.beanToMap(session);
        m.remove("object");
        long exp = 7 * 86400;
        m.put("exp", InfoUtil.time() + exp);
        // 生成 jwtToken 登录令牌
        String token = jwtTokenUtils.generateToken(session.getId(), m);

        return R.success(
            R
                .result()
                .set("session", session) // 将session 返回给前端
                .set("token", token) // 返回登录令牌给用户
        );
    }

    /**
     * token 令牌进行刷新、session 用户信息重新获取
     * @param token 保存在客户端的token令牌
     * @return
     */
    public R<Object> tokenLogin(String token) {
        try {
            /**
             * 原子对象操作
             */
            AtomicReference<Session> user = new AtomicReference();
            // token 数据解析
            jwtTokenUtils.getClaimFromToken(
                token,
                map -> {
                    // 将解析出来的数据
                    user.set(JSONObject.parseObject(JSONObject.toJSONString(map), Session.class));
                    return null;
                }
            );

            Session session = user.get();
            if (session == null) {
                return R.error("token解析错误");
            }
            Map<String, Object> m = BeanUtil.beanToMap(session);
            m.remove("object");
            m.put("exp", InfoUtil.time() + 7 * 86400);
            session.refresh();
            String token1 = jwtTokenUtils.generateToken(session.getId(), m);

            return R.success(
                R
                    .result()
                    .set("session", session) // 设置新的session 数据
                    .set("token", token1) // 刷新新的token
            );
        } catch (ExpiredJwtException e) {
            return R.error("已超时");
        } catch (UnsupportedJwtException e) {
            return R.error("空数据");
        } catch (MalformedJwtException e) {
            return R.error("数据出错");
        } catch (Exception e) {
            return R.error("解析token 错误");
        }
    }

    public R<Object> editPassword(PasswordPojo passwordPojo) {
        if (StringUtil.isNullOrEmpty(passwordPojo.getOldPassword())) {
            return R.error("请填写原密码");
        }
        if (StringUtil.isNullOrEmpty(passwordPojo.getNewPassword())) {
            return R.error("请填写新密码");
        }
        if (!passwordPojo.getNewPassword().equals(passwordPojo.getConfirmPassword())) {
            return R.error("新密码与原密码不一致");
        }
        String roles = SessionFactory.getRoles();
        if (roles.equals("管理员")) {
            return adminsService.editPassword(SessionFactory.getId(), passwordPojo.getOldPassword(), passwordPojo.getNewPassword());
        }

        if (roles.equals("用户")) {
            return yonghuService.editPassword(SessionFactory.getId(), passwordPojo.getOldPassword(), passwordPojo.getNewPassword());
        }

        if (roles.equals("商家")) {
            return shangjiaService.editPassword(SessionFactory.getId(), passwordPojo.getOldPassword(), passwordPojo.getNewPassword());
        }

        return R.error("没找到相关信息");
    }

    /**
     * 根据提供的参数查询数据库信息
     * @param map
     * @return
     */
    public R<Object> query(Map map) {
        if (!map.containsKey("name")) {
            return R.error("找不到相关名称");
        }
        if (!map.containsKey("options") && !(map.get("options") instanceof Map)) {
            return R.error("找不到相关配置");
        }
        if (!map.containsKey("func")) {
            return R.error("找不到引用");
        }

        JSONObject object = new JSONObject();
        object.putAll((Map) map.get("options"));

        QueryMap queryWrapper = DB.name(map.get("name").toString());
        queryWrapper.setOptions(object.toJavaObject(Options.class));
        String func = map.get("func").toString().toLowerCase();
        Object result = null;
        List args = (List) map.get("args");
        try {
            if (func.equals("select")) {
                result = queryWrapper.select();
            } else if (func.equals("find")) {
                if (args == null) {
                    result = queryWrapper.find();
                } else {
                    result = queryWrapper.find(args.get(0));
                }
            } else if (func.equals("count")) {
                if (args == null) {
                    result = queryWrapper.count();
                } else {
                    result = queryWrapper.count(String.valueOf(args.get(0)));
                }
            } else if (func.equals("avg")) {
                result = queryWrapper.avg(String.valueOf(args.get(0)));
            } else if (func.equals("sum")) {
                result = queryWrapper.sum(String.valueOf(args.get(0)));
            } else if (func.equals("max")) {
                result = queryWrapper.max(String.valueOf(args.get(0)));
            } else if (func.equals("min")) {
                result = queryWrapper.min(String.valueOf(args.get(0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.success(result);
    }

    /**
     * 根据参数执行数据库语句
     * @param data
     * @return
     */
    public R select(@RequestBody Map<String, Object> data) {
        String sql = String.valueOf(data.get("sql"));
        String type = String.valueOf(data.get("type"));
        List binds = null;
        if (data.containsKey("binds") && data.get("binds") instanceof List) {
            binds = (List) data.get("binds");
        } else {
            binds = new ArrayList();
        }

        Object[] datas = binds.toArray();
        if ("execute".equals(type)) {
            return R.success(DB.execute(sql, datas));
        } else if ("select".equals(type)) {
            return R.success(DB.select(sql, datas));
        } else {
            return R.success(DB.find(sql, datas));
        }
    }
}
