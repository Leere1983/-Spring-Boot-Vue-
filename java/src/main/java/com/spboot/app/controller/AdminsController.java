package com.spboot.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.config.Configure;
import com.spboot.app.mapper.AdminsMapper;
import com.spboot.app.pojo.Admins;
import com.spboot.app.service.AdminsService;
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

@Api(tags = { "管理员控制器" })
@RestController
@RequestMapping("/api/admins")
public class AdminsController {

    @Autowired
    public AdminsService adminsService;

    @ApiOperation(value = "获取全部管理员", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public R<List<Admins>> selectAll() {
        return adminsService.selectAll();
    }

    @ApiOperation(value = "根据条件筛选获取管理员列表，并分页", httpMethod = "POST")
    @RequestMapping("/selectPages")
    public R selectPages(@RequestBody Map<String, Object> req) {
        return adminsService.selectPages(req);
    }

    @ApiOperation(value = "根据id获取信息", httpMethod = "GET")
    @RequestMapping("/findById")
    @ApiImplicitParam(name = "id", value = "管理员对应的id", dataType = "Integer")
    public R findById(@RequestParam Integer id) {
        return adminsService.findById(id);
    }

    @ApiOperation(value = "根据id更新数据", httpMethod = "POST")
    @RequestMapping("/update")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Admins.class, paramType = "body")
    public R update(@RequestBody Map data) {
        Admins post = BeanUtil.mapToBean(data, Admins.class, true);
        return adminsService.update(post, data);
    }

    @ApiOperation(value = "插入一行数据，返回插入后的数据", httpMethod = "POST")
    @RequestMapping("/insert")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Admins.class, paramType = "body")
    public R insert(@RequestBody Map data) {
        Admins post = BeanUtil.mapToBean(data, Admins.class, true);
        return adminsService.insert(post, data);
    }

    @ApiOperation(value = "根据id列表删除数据", httpMethod = "POST")
    @RequestMapping("/delete")
    @ApiImplicitParam(name = "id", value = "管理员对应的id", type = "json", dataTypeClass = List.class)
    public R delete(@RequestBody List<Integer> id) {
        return adminsService.delete(id);
    }

    @ApiOperation(value = "获取回收站数据", httpMethod = "GET")
    @GetMapping("/recyclebin")
    public R recyclebin() {
        return adminsService.selectDeleted();
    }

    @ApiOperation(value = "恢复数据", httpMethod = "POST")
    @PostMapping("/restore")
    @ApiImplicitParam(name = "id", value = "管理员对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R restore(@RequestParam("id") Integer id) {
        return adminsService.restore(id);
    }
}
