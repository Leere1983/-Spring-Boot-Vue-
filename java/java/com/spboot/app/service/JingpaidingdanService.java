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
import com.spboot.app.mapper.JingpaidingdanMapper;
import com.spboot.app.pojo.Jingpaidingdan;
import com.spboot.app.pojo.Shangpinxinxi;
import com.spboot.app.utils.*;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class JingpaidingdanService {

    // 获取数据库操作类mapper
    @Resource
    private JingpaidingdanMapper mapper;

    /**
     *  根据id 获取一行数据
     */
    public R<Jingpaidingdan> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    /**
     *  根据Wrapper 对象进行数据筛选
     */
    public R<List<Jingpaidingdan>> selectAll(Wrapper<Jingpaidingdan> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     *  直接筛选所有数据
     */
    public R<List<Jingpaidingdan>> selectAll() {
        QueryWrapper<Jingpaidingdan> wrapper = Wrappers.query();
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
        QueryWrapper<Jingpaidingdan> wrapper = mapToWrapper(map);
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
        QueryWrapper<Jingpaidingdan> wrapper = mapToWrapper(map);
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
        QueryWrapper<Jingpaidingdan> wrapper = mapToWrapper(map);
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
        QueryWrapper<Jingpaidingdan> wrapper = mapToWrapper(map);
        // 设置为当前角色
        wrapper.eq("faburen", SessionFactory.getUsername());
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *   将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象
     */
    public QueryWrapper<Jingpaidingdan> mapToWrapper(Map<String, Object> map) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Jingpaidingdan> wrapper = Wrappers.query();

        String where = " 1=1 ";
        // 以下是判断搜索框中是否有输入内容，判断是否前台是否有填写相关条件，符合则写入sql搜索语句

        // 判断URL 参数shangpinxinxiid是否大于0
        if (!StringUtil.isNullOrEmpty(map.get("shangpinxinxiid")) && Convert.toInt(map.get("shangpinxinxiid")) > 0) {
            // 大于0 则写入条件
            wrapper.eq("shangpinxinxiid", map.get("shangpinxinxiid"));
        }

        if (!StringUtil.isNullOrEmpty(map.get("shangpinmingcheng"))) {
            wrapper.like("shangpinmingcheng", map.get("shangpinmingcheng"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("fenlei"))) {
            wrapper.eq("fenlei", map.get("fenlei"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("dingdanbianhao"))) {
            wrapper.like("dingdanbianhao", map.get("dingdanbianhao"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("dingdanzhuangtai"))) {
            wrapper.eq("dingdanzhuangtai", map.get("dingdanzhuangtai"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("lianxiren"))) {
            wrapper.like("lianxiren", map.get("lianxiren"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("lianxidianhua"))) {
            wrapper.like("lianxidianhua", map.get("lianxidianhua"));
        }

        if (!StringUtil.isNullOrEmpty(map.get("issh"))) {
            wrapper.eq("issh", map.get("issh"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("iszf"))) {
            wrapper.eq("iszf", map.get("iszf"));
        }

        if (map.containsKey("session_name")) {
            wrapper.eq(map.get("session_name").toString(), SessionFactory.getUsername());
        }

        wrapper.apply(where);
        return wrapper;
    }

    public R selectPages(QueryWrapper<Jingpaidingdan> wrapper, IPage page) {
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
    public R insert(Jingpaidingdan entityData, Map post) {
        // 判断是否有填写联系人。
        if (StringUtil.isNullOrEmpty(post.get("lianxiren"))) {
            return R.error("请填写联系人");
        }
        // 判断是否有填写联系电话。
        if (StringUtil.isNullOrEmpty(post.get("lianxidianhua"))) {
            return R.error("请填写联系电话");
        }
        // 判断是否有填写联系电话,有则判断是否为手机号码或者座机。
        if (!StringUtil.isNullOrEmpty(entityData.getLianxidianhua())) {
            if (!(Validator.isMatchRegex(PatternPool.TEL, entityData.getLianxidianhua()) || Validator.isMobile(entityData.getLianxidianhua()))) {
                return R.error("请输入正确的联系电话");
            }
        }
        // 判断是否有填写联系地址。
        if (StringUtil.isNullOrEmpty(post.get("lianxidizhi"))) {
            return R.error("请填写联系地址");
        }

        // 下单前业务校验：商品存在、仅最高竞拍人可下单、禁止重复下单
        Shangpinxinxi shangpin = DB.name(Shangpinxinxi.class).find(entityData.getShangpinxinxiid());
        if (shangpin == null || shangpin.getId() == null) {
            return R.error("商品不存在或已下架");
        }
        if (!StringUtil.isNullOrEmpty(shangpin.getJingpairen()) && !shangpin.getJingpairen().equals(entityData.getJingpairen())) {
            return R.error("仅当前最高竞拍人可下单");
        }
        long existsCount = DB.name("jingpaidingdan")
                .where("shangpinxinxiid", entityData.getShangpinxinxiid())
                .where("jingpairen", entityData.getJingpairen())
                .count();
        if (existsCount > 0) {
            return R.error("该商品您已下单，请勿重复提交");
        }

        Info.handlerNullEntity(entityData);
        entityData.setAddtime(Info.getDateStr());
        // 订单编号由后端统一生成，防止前端篡改/重复
        entityData.setDingdanbianhao(generateOrderNo());

        entityData.setId(null);
        mapper.insert(entityData);
        if (entityData.getId() != null) {
            updateFeeAndPayment(entityData.getId(), entityData.getDangqianjingpaijia());
            return findById(entityData.getId());
        } else {
            return R.error("插入错误");
        }
    }

    private String generateOrderNo() {
        String orderNo;
        do {
            int random = (int) (Math.random() * 9000) + 1000;
            orderNo = "JP" + System.currentTimeMillis() + random;
        } while (DB.name("jingpaidingdan").where("dingdanbianhao", orderNo).count() > 0);
        return orderNo;
    }

    /**
     * 根据id进行更新竞拍订单数据
     * @param entityData 更新的数据
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R<Object> update(Jingpaidingdan entityData, Map post) {
        // 判断是否有填写订单编号。
        if (StringUtil.isNullOrEmpty(post.get("dingdanbianhao"))) {
            return R.error("请填写订单编号");
        }
        // 判断是否有填写联系人。
        if (StringUtil.isNullOrEmpty(post.get("lianxiren"))) {
            return R.error("请填写联系人");
        }
        // 判断是否有填写联系电话。
        if (StringUtil.isNullOrEmpty(post.get("lianxidianhua"))) {
            return R.error("请填写联系电话");
        }
        // 判断是否有填写联系电话,有则判断是否为手机号码或者座机。
        if (!StringUtil.isNullOrEmpty(entityData.getLianxidianhua())) {
            if (!(Validator.isMatchRegex(PatternPool.TEL, entityData.getLianxidianhua()) || Validator.isMobile(entityData.getLianxidianhua()))) {
                return R.error("请输入正确的联系电话");
            }
        }
        // 判断是否有填写联系地址。
        if (StringUtil.isNullOrEmpty(post.get("lianxidizhi"))) {
            return R.error("请填写联系地址");
        }

        mapper.updateById(entityData);
        updateFeeAndPayment(entityData.getId(), entityData.getDangqianjingpaijia());
        return R.success(mapper.selectById(entityData.getId()));
    }

    /**
     * 计算并更新订单的手续费和支付金额
     * @param orderId       订单ID
     * @param jingpaijia    当前竞拍价
     */
    private void updateFeeAndPayment(Integer orderId, Double jingpaijia) {
        double shouxufei = (jingpaijia == null ? 0D : jingpaijia) * 0.01;
        double zhifujine = (jingpaijia == null ? 0D : jingpaijia) * 1.01;
        DB.name("jingpaidingdan").where("id", orderId).setField("shouxufei", shouxufei);
        DB.name("jingpaidingdan").where("id", orderId).setField("zhifujine", zhifujine);
    }

    /**
     * 更新审核数据
     * @param id      竞拍订单id
     * @param value   更新的值
     * @return 是否成功
     */
    public R<Object> checkIssh(Integer id, String value) {
        Jingpaidingdan row = mapper.selectById(id);
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
}
