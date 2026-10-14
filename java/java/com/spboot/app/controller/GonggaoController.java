package com.spboot.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.config.Configure;
import com.spboot.app.mapper.GonggaoMapper;
import com.spboot.app.pojo.Gonggao;
import com.spboot.app.service.GonggaoService;
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

@Api(tags = { "公告控制器" })
@RestController
@RequestMapping("/api/gonggao")
public class GonggaoController {

    @Autowired
    public GonggaoService gonggaoService;

    @ApiOperation(value = "获取全部公告", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public R<List<Gonggao>> selectAll() {
        return gonggaoService.selectAll();
    }

    @ApiOperation(value = "根据条件筛选获取管理员列表，并分页", httpMethod = "POST")
    @RequestMapping("/selectPages")
    public R selectPages(@RequestBody Map<String, Object> req) {
        return gonggaoService.selectPages(req);
    }

    @ApiOperation(value = "根据条件筛选获取发布人字段值为当前用户列表并分页", httpMethod = "POST")
    @RequestMapping("/selectFaburen")
    public R selectFaburen(@RequestBody Map<String, Object> req) {
        return gonggaoService.selectPagesFaburen(req);
    }

    /**
     *   更新数据
     */
    @ApiOperation(value = "设置数据是否为审核状态", httpMethod = "GET")
    @RequestMapping("/checkIssh")
    public R<Object> checkIssh(@RequestParam("id") Integer id, @RequestParam("value") String value) {
        return gonggaoService.checkIssh(id, value);
    }

    @ApiOperation(value = "根据id获取信息", httpMethod = "GET")
    @RequestMapping("/findById")
    @ApiImplicitParam(name = "id", value = "公告对应的id", dataType = "Integer")
    public R findById(@RequestParam Integer id) {
        return gonggaoService.findById(id);
    }

    @ApiOperation(value = "根据id更新数据", httpMethod = "POST")
    @RequestMapping("/update")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Gonggao.class, paramType = "body")
    public R update(@RequestBody Map data) {
        Gonggao post = BeanUtil.mapToBean(data, Gonggao.class, true);
        return gonggaoService.update(post, data);
    }

    @ApiOperation(value = "插入一行数据，返回插入后的数据", httpMethod = "POST")
    @RequestMapping("/insert")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Gonggao.class, paramType = "body")
    public R insert(@RequestBody Map data) {
        Gonggao post = BeanUtil.mapToBean(data, Gonggao.class, true);
        return gonggaoService.insert(post, data);
    }

    @ApiOperation(value = "根据id列表删除数据", httpMethod = "POST")
    @RequestMapping("/delete")
    @ApiImplicitParam(name = "id", value = "公告对应的id", type = "json", dataTypeClass = List.class)
    public R delete(@RequestBody List<Integer> id) {
        return gonggaoService.delete(id);
    }

    @ApiOperation(value = "获取回收站数据", httpMethod = "GET")
    @GetMapping("/recyclebin")
    public R recyclebin() {
        return gonggaoService.selectDeleted();
    }

    @ApiOperation(value = "恢复数据", httpMethod = "POST")
    @PostMapping("/restore")
    @ApiImplicitParam(name = "id", value = "公告对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R restore(@RequestParam("id") Integer id) {
        return gonggaoService.restore(id);
    }

    @ApiOperation(value = "获取收藏", httpMethod = "GET")
    @GetMapping("/getshoucang")
    @ApiImplicitParam(name = "id", value = "公告对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R getshoucang(@RequestParam("id") Integer id) {
        long shoucangCount = DB.name("shoucang").where("biao", "gonggao").where("xwid", id).count();

        long is_shoucang = DB.name("shoucang").where("biao", "gonggao").where("xwid", id).where("username", SessionFactory.getUsername()).count();

        return R.success(R.result().set("shoucangCount", shoucangCount).set("is_shoucang", is_shoucang));
    }
}
