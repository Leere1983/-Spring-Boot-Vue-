package com.spboot.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.config.Configure;
import com.spboot.app.mapper.LiuyanbanMapper;
import com.spboot.app.pojo.Liuyanban;
import com.spboot.app.service.LiuyanbanService;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "留言板控制器" })
@RestController
@RequestMapping("/api/liuyanban")
public class LiuyanbanController {

    @Autowired
    public LiuyanbanService liuyanbanService;

    @ApiOperation(value = "获取全部留言板", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public R<List<Liuyanban>> selectAll() {
        return liuyanbanService.selectAll();
    }

    @ApiOperation(value = "根据条件筛选获取管理员列表，并分页", httpMethod = "POST")
    @RequestMapping("/selectPages")
    public R selectPages(@RequestBody Map<String, Object> req) {
        return liuyanbanService.selectPages(req);
    }

    @ApiOperation(value = "根据条件筛选获取反馈用户字段值为当前用户列表并分页", httpMethod = "POST")
    @RequestMapping("/selectFankuiyonghu")
    public R selectFankuiyonghu(@RequestBody Map<String, Object> req) {
        return liuyanbanService.selectPagesFankuiyonghu(req);
    }

    @ApiOperation(value = "根据id获取信息", httpMethod = "GET")
    @RequestMapping("/findById")
    @ApiImplicitParam(name = "id", value = "留言板对应的id", dataType = "Integer")
    public R findById(@RequestParam Integer id) {
        return liuyanbanService.findById(id);
    }

    @ApiOperation(value = "根据id更新数据", httpMethod = "POST")
    @RequestMapping("/update")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Liuyanban.class, paramType = "body")
    public R update(@RequestBody Map data) {
        Liuyanban post = BeanUtil.mapToBean(data, Liuyanban.class, true);
        return liuyanbanService.update(post, data);
    }

    @ApiOperation(value = "插入一行数据，返回插入后的数据", httpMethod = "POST")
    @RequestMapping("/insert")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Liuyanban.class, paramType = "body")
    public R insert(@RequestBody Map data) {
        Liuyanban post = BeanUtil.mapToBean(data, Liuyanban.class, true);
        return liuyanbanService.insert(post, data);
    }

    @ApiOperation(value = "根据id列表删除数据", httpMethod = "POST")
    @RequestMapping("/delete")
    @ApiImplicitParam(name = "id", value = "留言板对应的id", type = "json", dataTypeClass = List.class)
    public R delete(@RequestBody List<Integer> id) {
        return liuyanbanService.delete(id);
    }

    @ApiOperation(value = "获取回收站数据", httpMethod = "GET")
    @GetMapping("/recyclebin")
    public R recyclebin() {
        return liuyanbanService.selectDeleted();
    }

    @ApiOperation(value = "恢复数据", httpMethod = "POST")
    @PostMapping("/restore")
    @ApiImplicitParam(name = "id", value = "留言板对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R restore(@RequestParam("id") Integer id) {
        return liuyanbanService.restore(id);
    }
}
