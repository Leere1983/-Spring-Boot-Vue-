package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 留言板 表 liuyanban.
 */
@TableName("liuyanban")
public class Liuyanban implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 姓名.
    private String xingming;
    // 联系电话.
    private String lianxidianhua;
    // 反馈内容.
    private String fankuineirong;
    // 反馈用户.
    private String fankuiyonghu;
    // 反馈状态.
    // 可选项:待回复,已回复。
    private String fankuizhuangtai;
    // 反馈时间.
    private String addtime;
    // 反馈回复.
    private String fankuihuifu;
    // 回复时间.
    private String huifushijian;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming == null ? "" : xingming.trim();
    }

    public String getLianxidianhua() {
        return lianxidianhua;
    }

    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua == null ? "" : lianxidianhua.trim();
    }

    public String getFankuineirong() {
        return fankuineirong;
    }

    public void setFankuineirong(String fankuineirong) {
        this.fankuineirong = fankuineirong == null ? "" : fankuineirong.trim();
    }

    public String getFankuiyonghu() {
        return fankuiyonghu;
    }

    public void setFankuiyonghu(String fankuiyonghu) {
        this.fankuiyonghu = fankuiyonghu == null ? "" : fankuiyonghu.trim();
    }

    public String getFankuizhuangtai() {
        return fankuizhuangtai;
    }

    public void setFankuizhuangtai(String fankuizhuangtai) {
        this.fankuizhuangtai = fankuizhuangtai == null ? "" : fankuizhuangtai.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }

    public String getFankuihuifu() {
        return fankuihuifu;
    }

    public void setFankuihuifu(String fankuihuifu) {
        this.fankuihuifu = fankuihuifu == null ? "" : fankuihuifu.trim();
    }

    public String getHuifushijian() {
        return huifushijian;
    }

    public void setHuifushijian(String huifushijian) {
        this.huifushijian = huifushijian == null ? "" : huifushijian.trim();
    }

    @TableLogic
    private Integer isDeleted;

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
