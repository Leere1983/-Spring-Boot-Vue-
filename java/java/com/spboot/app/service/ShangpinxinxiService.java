package com.spboot.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.mapper.ShangpinxinxiMapper;
import com.spboot.app.pojo.Shangpinxinxi;
import com.spboot.app.utils.*;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShangpinxinxiService {

    // 获取数据库操作类mapper
    @Resource
    private ShangpinxinxiMapper mapper;

    /**
     *  根据id 获取一行数据
     */
    public R<Shangpinxinxi> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    /**
     *  根据Wrapper 对象进行数据筛选
     */
    public R<List<Shangpinxinxi>> selectAll(Wrapper<Shangpinxinxi> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     *  直接筛选所有数据
     */
    public R<List<Shangpinxinxi>> selectAll() {
        QueryWrapper<Shangpinxinxi> wrapper = Wrappers.query();
        wrapper.orderByDesc("id");
        return selectAll(wrapper);
    }

    /**
     *  根据map 条件筛选数据
     *
     */
    public R selectAll(Map<String, Object> map) {
        // 获取筛选数据
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象，执行动态查询
        QueryWrapper<Shangpinxinxi> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        return selectAll(wrapper);
    }

    /**
     *  根据map 条件筛选数据并分页
     *
     */
    public R selectPages(Map<String, Object> map) {
        // 获取筛选数据
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象，执行动态查询
        QueryWrapper<Shangpinxinxi> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        // 设置分页数据
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *  根据map 条件筛选jingpairen字段等于session.username的数据并分页
     *
     */
    public R selectPagesJingpairen(Map<String, Object> map) {
        // 获取前端给到列表基础参数
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将map参数转换为mybatis-plus的QueryWrapper类
        QueryWrapper<Shangpinxinxi> wrapper = mapToWrapper(map);
        // 设置为当前角色
        wrapper.eq("jingpairen", SessionFactory.getUsername());
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *  根据map 条件筛选faburen字段等于session.username的数据并分页
     *
     */
    public R selectPagesFaburen(Map<String, Object> map) {
        // 获取前端给到列表基础参数
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将map参数转换为mybatis-plus的QueryWrapper类
        QueryWrapper<Shangpinxinxi> wrapper = mapToWrapper(map);
        // 设置为当前角色
        wrapper.eq("faburen", SessionFactory.getUsername());
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *   将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象
     */
    public QueryWrapper<Shangpinxinxi> mapToWrapper(Map<String, Object> map) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Shangpinxinxi> wrapper = Wrappers.query();

        String where = " 1=1 ";
        // 以下是判断搜索框中是否有输入内容，判断是否前台是否有填写相关条件，符合则写入sql搜索语句

        if (!StringUtil.isNullOrEmpty(map.get("shangpinbianhao"))) {
            wrapper.like("shangpinbianhao", map.get("shangpinbianhao"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("fenlei"))) {
            wrapper.eq("fenlei", map.get("fenlei"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("shangpinmingcheng"))) {
            wrapper.like("shangpinmingcheng", map.get("shangpinmingcheng"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("shangpinzhuangtai"))) {
            wrapper.eq("shangpinzhuangtai", map.get("shangpinzhuangtai"));
        }

        if (!StringUtil.isNullOrEmpty(map.get("issh"))) {
            wrapper.eq("issh", map.get("issh"));
        }

        if (map.containsKey("session_name")) {
            wrapper.eq(map.get("session_name").toString(), SessionFactory.getUsername());
        }

        wrapper.apply(where);
        return wrapper;
    }

    public R selectPages(QueryWrapper<Shangpinxinxi> wrapper, IPage page) {
        Map result = new HashMap();
        result.put("lists", mapper.selectPage(page, wrapper));

        return R.success(result);
    }

    /**
     * 插入用户数据
     * @param entityData 插入的对象
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R insert(Shangpinxinxi entityData, Map post) {
        // 判断是否有填写商品编号。
        if (StringUtil.isNullOrEmpty(post.get("shangpinbianhao"))) {
            return R.error("请填写商品编号");
        }
        // 判断是否有填写分类。
        if (StringUtil.isNullOrEmpty(post.get("fenlei"))) {
            return R.error("请填写分类");
        }
        // 判断是否有填写商品名称。
        if (StringUtil.isNullOrEmpty(post.get("shangpinmingcheng"))) {
            return R.error("请填写商品名称");
        }
        // 判断是否有填写商品图片。
        if (StringUtil.isNullOrEmpty(post.get("shangpintupian"))) {
            return R.error("请填写商品图片");
        }
        // 判断是否有填写起拍价。
        if (StringUtil.isNullOrEmpty(post.get("qipaijia"))) {
            return R.error("请填写起拍价");
        }
        // 判断是否有填写加价幅度。
        if (StringUtil.isNullOrEmpty(post.get("jiajiafudu"))) {
            return R.error("请填写加价幅度");
        }
        // 判断是否有填写起拍时间。
        if (StringUtil.isNullOrEmpty(post.get("qipaishijian"))) {
            return R.error("请填写起拍时间");
        }
        // 判断是否有填写结束时间。
        if (StringUtil.isNullOrEmpty(post.get("jieshushijian"))) {
            return R.error("请填写结束时间");
        }

        Info.handlerNullEntity(entityData);

        entityData.setId(null);
        mapper.insert(entityData);
        if (entityData.getId() != null) {
            Integer charuid = entityData.getId();
            DB.name("shangpinxinxi").where("id", charuid).setField("dangqianjingpaijia", entityData.getQipaijia());

            return findById(entityData.getId());
        } else {
            return R.error("插入错误");
        }
    }

    /**
     * 根据id进行更新商品信息数据
     * @param entityData 更新的数据
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R<Object> update(Shangpinxinxi entityData, Map post) {
        // 判断是否有填写商品编号。
        if (StringUtil.isNullOrEmpty(post.get("shangpinbianhao"))) {
            return R.error("请填写商品编号");
        }
        
        // 商品编号不可修改
        Shangpinxinxi oldShangpin = mapper.selectById(entityData.getId());
        if (oldShangpin != null && !oldShangpin.getShangpinbianhao().equals(post.get("shangpinbianhao"))) {
            return R.error("商品编号不可修改");
        }
        
        // 根据商品状态限制修改
        if (oldShangpin != null) {
            String status = oldShangpin.getShangpinzhuangtai();
            
            // 已结束的商品禁止修改
            if ("已结束".equals(status) || "已成交".equals(status) || "已流拍".equals(status)) {
                return R.error("已结束的商品不能修改");
            }
            
            // 竞拍中的商品限制修改价格和时间
            if ("竞拍中".equals(status)) {
                if (!oldShangpin.getQipaijia().equals(post.get("qipaijia")) ||
                    !oldShangpin.getJiajiafudu().equals(post.get("jiajiafudu")) ||
                    !oldShangpin.getQipaishijian().equals(post.get("qipaishijian")) ||
                    !oldShangpin.getJieshushijian().equals(post.get("jieshushijian"))) {
                    return R.error("竞拍中的商品不能修改价格和时间");
                }
            }
        }
        
        // 判断是否有填写分类。
        if (StringUtil.isNullOrEmpty(post.get("fenlei"))) {
            return R.error("请填写分类");
        }
        // 判断是否有填写商品名称。
        if (StringUtil.isNullOrEmpty(post.get("shangpinmingcheng"))) {
            return R.error("请填写商品名称");
        }
        // 判断是否有填写商品图片。
        if (StringUtil.isNullOrEmpty(post.get("shangpintupian"))) {
            return R.error("请填写商品图片");
        }
        // 判断是否有填写起拍价。
        if (StringUtil.isNullOrEmpty(post.get("qipaijia"))) {
            return R.error("请填写起拍价");
        }
        // 判断是否有填写加价幅度。
        if (StringUtil.isNullOrEmpty(post.get("jiajiafudu"))) {
            return R.error("请填写加价幅度");
        }
        // 判断是否有填写起拍时间。
        if (StringUtil.isNullOrEmpty(post.get("qipaishijian"))) {
            return R.error("请填写起拍时间");
        }
        // 判断是否有填写结束时间。
        if (StringUtil.isNullOrEmpty(post.get("jieshushijian"))) {
            return R.error("请填写结束时间");
        }

        mapper.updateById(entityData);

        Integer charuid = entityData.getId();
        DB.name("shangpinxinxi").where("id", charuid).setField("dangqianjingpaijia", entityData.getQipaijia());

        return R.success(mapper.selectById(entityData.getId()));
    }

    /**
     * 更新审核数据
     * @param id      商品信息id
     * @param value   更新的值
     * @return 是否成功
     */
    public R<Object> checkIssh(Integer id, String value) {
        Shangpinxinxi row = mapper.selectById(id);
        row.setIssh(value);
        mapper.updateById(row);
        return R.ok();
    }

    /**
     * 根据 id列表 删除
     * @param ids  id 列表值
     * @return 是否成功
     */
    public R<Object> delete(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                delete(id);
            }
            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    /**
     * 根据 id 删除
     * @param id  id 列表值
     * @return 是否成功
     */
    public R<Object> delete(Integer id) {
        try {
            mapper.deleteById(id);

            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    public R<List<Shangpinxinxi>> selectDeleted() {
        QueryWrapper<Shangpinxinxi> wrapper = Wrappers.query();
        wrapper.eq("is_deleted", 1);
        wrapper.orderByDesc("id");
        return R.success(mapper.selectList(wrapper));
    }

    public R<Object> restore(Integer id) {
        Shangpinxinxi entity = mapper.selectById(id);
        if (entity != null) {
            entity.setIsDeleted(0);
            mapper.updateById(entity);
            return R.success("恢复成功");
        }
        return R.error("数据不存在");
    }
}
