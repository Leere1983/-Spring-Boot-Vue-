package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("shangjia")
public class Shangjia implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String zhanghao;

    //@JsonIgnore // 这个字段不返回前端
    private String mima;

    private String dianpumingcheng;

    private Double qianbaoyue;

    private String fuzeren;

    private String shenfenzheng;

    private String dianhua;

    private String youxiang;

    private String dizhi;

    private String jianjie;

    private String zizhizhengshu;
    private String issh;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getShangjiatixianCount() {
        return DB.name("shangjiatixian").where("shangjiaid", id).count();
    }

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao == null ? "" : zhanghao.trim();
    }

    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima == null ? "" : mima.trim();
    }

    public String getDianpumingcheng() {
        return dianpumingcheng;
    }

    public void setDianpumingcheng(String dianpumingcheng) {
        this.dianpumingcheng = dianpumingcheng == null ? "" : dianpumingcheng.trim();
    }

    public Double getQianbaoyue() {
        return qianbaoyue;
    }

    public void setQianbaoyue(Double qianbaoyue) {
        this.qianbaoyue = qianbaoyue == null ? 0.0f : qianbaoyue;
    }

    public String getFuzeren() {
        return fuzeren;
    }

    public void setFuzeren(String fuzeren) {
        this.fuzeren = fuzeren == null ? "" : fuzeren.trim();
    }

    public String getShenfenzheng() {
        return shenfenzheng;
    }

    public void setShenfenzheng(String shenfenzheng) {
        this.shenfenzheng = shenfenzheng == null ? "" : shenfenzheng.trim();
    }

    public String getDianhua() {
        return dianhua;
    }

    public void setDianhua(String dianhua) {
        this.dianhua = dianhua == null ? "" : dianhua.trim();
    }

    public String getYouxiang() {
        return youxiang;
    }

    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang == null ? "" : youxiang.trim();
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi == null ? "" : dizhi.trim();
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie == null ? "" : jianjie.trim();
    }

    public String getZizhizhengshu() {
        return zizhizhengshu;
    }

    public void setZizhizhengshu(String zizhizhengshu) {
        this.zizhizhengshu = zizhizhengshu == null ? "" : zizhizhengshu.trim();
    }

    public String getIssh() {
        return issh;
    }

    public void setIssh(String issh) {
        this.issh = issh == null ? "" : issh.trim();
    }

    @TableLogic
    private Integer isDeleted;

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
