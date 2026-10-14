package com.spboot.app.utils;

import cn.hutool.json.JSONObject;
import com.jntoo.db.utils.StringUtil;
import java.util.Map;

/**
 * 列表页面提交的基础参数
 */
public class SelectPage {

    // 获取列表显示行数
    private Integer pagesize;
    // 排序字段
    private String orderby;
    // 升序还是降序
    private String sort;
    //
    private Integer page;

    /**
     *
     * @param map 前端参数
     * @param nPagesize 分页数
     * @param nOrderBy 默认分页
     * @param nSort 默认倒序
     */
    public SelectPage(Map map, Integer nPagesize, String nOrderBy, String nSort) {
        JSONObject object = new JSONObject(map);
        page = object.getInt("page", 1);
        pagesize = object.getInt("pagesize", nPagesize);

        orderby = object.getStr("orderby", nOrderBy);
        sort = object.getStr("sort", nSort);

        if (StringUtil.isNullOrEmpty(orderby)) {
            orderby = "id";
        }
        if (StringUtil.isNullOrEmpty(sort)) {
            sort = "DESC";
        }
        sort = sort.toUpperCase();
        if (!sort.equals("DESC") && !sort.equals("ASC")) {
            sort = "DESC";
        }
        page = Math.max(page, 1);
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public boolean isDesc() {
        return sort.equals("DESC");
    }

    public boolean isAsc() {
        return sort.equals("ASC");
    }
}
