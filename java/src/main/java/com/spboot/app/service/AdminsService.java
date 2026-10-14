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
import com.spboot.app.mapper.AdminsMapper;
import com.spboot.app.pojo.Admins;
import com.spboot.app.utils.*;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminsService {

    // 获取数据库操作类mapper
    @Resource
    private AdminsMapper mapper;

    // 获取密码加密类
    @Resource
    private PasswordEncoder pwdEncoder;

    /**
     * 将数据转换成Session对象
     */
    public Session toSession(Admins user) {
        // 创建Session 对象
        Session session = new Session();
        // 设置相应数据进session 对象
        session.setId(user.getId());
        session.setUsername(user.getUsername());
        session.setTable("admins");
        session.setRoles("管理员");
        session.setCx(session.getRoles());

        session.setObject(BeanUtil.beanToMap(user));
        return session;
    }

    /**
     * 管理员进行登录
     * @param username 用户名
     * @param pwd      密码
     * @return 管理员对象实体类
     */
    public Admins login(String username, String pwd) {
        Admins row = findByUsername(username);

        // row为null的则是没有账号匹配成功
        if (row == null) {
            return null;
        }

        // 新建密码匹配类进行密码匹配，是否正确
        if (!pwdEncoder.matches(pwd, row.getPwd())) {
            return null;
        }
        return row;
    }

    /**
     * 修改密码
     * @param id 管理员id
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 处理是否成功
     */
    public R<Object> editPassword(int id, String oldPassword, String newPassword) {
        String encodePassword = pwdEncoder.encode(newPassword);
        Admins admins = mapper.selectById(id);
        if (!pwdEncoder.matches(oldPassword, admins.getPwd())) {
            return R.error("原密码不正确，请重新输入");
        }
        admins.setPwd(encodePassword);
        mapper.updateById(admins);
        return R.ok();
    }

    /**
     *   根据Username字段参数获取一行数据
     */
    public Admins findByUsername(String username) {
        // 新建管理员模块实体类Admins
        Admins pojo = new Admins();
        // 设置参数
        pojo.setUsername(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Admins> queryWrapper = Wrappers.query(pojo);
        Admins row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *   根据Username字段参数获取一行数据，并不包含某uid 参数得行
     */
    public Admins findByUsername(String username, Integer uid) {
        // 新建管理员模块实体类Admins
        Admins pojo = new Admins();
        // 设置参数
        pojo.setUsername(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Admins> queryWrapper = Wrappers.query(pojo);
        // 设置参数 id != uid变量
        queryWrapper.ne("id", uid);
        // 根据queryWrapper 查询
        Admins row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *  根据id 获取一行数据
     */
    public R<Admins> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    /**
     *  根据Wrapper 对象进行数据筛选
     */
    public R<List<Admins>> selectAll(Wrapper<Admins> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     *  直接筛选所有数据
     */
    public R<List<Admins>> selectAll() {
        QueryWrapper<Admins> wrapper = Wrappers.query();
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
        QueryWrapper<Admins> wrapper = mapToWrapper(map);
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
        QueryWrapper<Admins> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        // 设置分页数据
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *   将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象
     */
    public QueryWrapper<Admins> mapToWrapper(Map<String, Object> map) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Admins> wrapper = Wrappers.query();

        String where = " 1=1 ";
        // 以下是判断搜索框中是否有输入内容，判断是否前台是否有填写相关条件，符合则写入sql搜索语句

        if (!StringUtil.isNullOrEmpty(map.get("username"))) {
            wrapper.like("username", map.get("username"));
        }

        if (map.containsKey("session_name")) {
            wrapper.eq(map.get("session_name").toString(), SessionFactory.getUsername());
        }

        wrapper.apply(where);
        return wrapper;
    }

    public R selectPages(QueryWrapper<Admins> wrapper, IPage page) {
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
    public R insert(Admins entityData, Map post) {
        // 判断是否有填写帐号。
        if (StringUtil.isNullOrEmpty(post.get("username"))) {
            return R.error("请填写帐号");
        }
        // 判断是否有填写帐号,有则判断是否在数据中已经存在，存在则报错。
        if (findByUsername(entityData.getUsername()) != null) {
            return R.error("帐号已经存在");
        }
        // 判断是否有填写密码。
        if (StringUtil.isNullOrEmpty(post.get("pwd"))) {
            return R.error("请填写密码");
        }

        Info.handlerNullEntity(entityData);

        // 对密码字段进行加密操作
        String pwd = pwdEncoder.encode(entityData.getPwd());
        entityData.setPwd(pwd);

        entityData.setId(null);
        mapper.insert(entityData);
        if (entityData.getId() != null) {
            return findById(entityData.getId());
        } else {
            return R.error("插入错误");
        }
    }

    /**
     * 根据id进行更新管理员数据
     * @param entityData 更新的数据
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R<Object> update(Admins entityData, Map post) {
        // 判断是否有填写帐号。
        if (StringUtil.isNullOrEmpty(post.get("username"))) {
            return R.error("请填写帐号");
        }
        // 判断是否有填写帐号,有则判断是否在数据中已经存在，存在则报错。
        if (findByUsername(entityData.getUsername(), entityData.getId()) != null) {
            return R.error("帐号已经存在");
        }

        String currentPwd = entityData.getPwd();
        if (!StringUtil.isNullOrEmpty(currentPwd)) {
            // 不等于空，设置密码
            String pwd = pwdEncoder.encode(currentPwd);
            entityData.setPwd(pwd);
        } else {
            Admins old = mapper.selectById(entityData.getId());
            entityData.setPwd(old.getPwd());
        }

        mapper.updateById(entityData);

        return R.success(mapper.selectById(entityData.getId()));
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

    public R<List<Admins>> selectDeleted() {
        QueryWrapper<Admins> wrapper = Wrappers.query();
        wrapper.eq("is_deleted", 1);
        wrapper.orderByDesc("id");
        return R.success(mapper.selectList(wrapper));
    }

    public R<Object> restore(Integer id) {
        Admins entity = mapper.selectById(id);
        if (entity != null) {
            entity.setIsDeleted(0);
            mapper.updateById(entity);
            return R.success("恢复成功");
        }
        return R.error("数据不存在");
    }
}
