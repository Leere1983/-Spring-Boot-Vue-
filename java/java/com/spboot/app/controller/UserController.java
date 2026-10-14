package com.spboot.app.controller;

import cn.hutool.core.util.StrUtil;
import com.spboot.app.pojo.LoginPojo;
import com.spboot.app.pojo.PasswordPojo;
import com.spboot.app.service.UserService;
import com.spboot.app.utils.JwtTokenUtils;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Api("用户登录")
public class UserController {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "账号密码登录", httpMethod = "POST")

    @PostMapping("/login")
    public R<Object> login(@RequestBody LoginPojo loginPojo) {
        // 检查验证码是否为空
        if (StrUtil.isEmpty(loginPojo.getPagerandom())) {
            return R.error("请输入验证码");
        }
        // 从token中获取正确的验证码
        String token = jwtTokenUtils.getUserIdFromToken(loginPojo.getCaptchToken());
        // 验证用户输入的验证码是否正确
        if (!token.equals(loginPojo.getPagerandom())) {
            return R.error("验证码错误请重新输入");
        }
        return userService.login(loginPojo.getUsername(), loginPojo.getPwd(), loginPojo.getCx());
    }


    @ApiOperation(value = "修改密码", httpMethod = "POST")
    @PostMapping("/editPassword")
    public R<Object> editPassword(@RequestBody PasswordPojo passwordPojo) {
        return userService.editPassword(passwordPojo);
    }

    @ApiOperation(value = "使用token获取用户信息，并更新token", httpMethod = "POST")
    @GetMapping("/tokenLogin")
    public R<Object> tokenLogin(@RequestParam("token") String token) {
        return userService.tokenLogin(token);
    }

    @ApiOperation(value = "主动退出操作", httpMethod = "POST")
    @RequestMapping("/logout")
    public R<Object> logout() {
        return R.ok();
    }

    @PostMapping("/query")
    @ApiOperation(value = "根据参数查询数据库信息", httpMethod = "POST")
    public R<Object> query(@RequestBody Map map) {
        return userService.query(map);
    }

    @RequestMapping("/select")
    @ApiOperation(value = "根据参数获取数据库信息", httpMethod = "POST")
    public R select(@RequestBody Map<String, Object> data) {
        return userService.select(data);
    }
}
