package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("shangpinxinxi")
public class Shangpinxinxi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String shangpinbianhao;

    private Integer fenlei;

    private String shangpinmingcheng;

    private String shangpintupian;

    private Double qipaijia;

    private Double jiajiafudu;

    private Double dangqianjingpaijia;

    private String qipaishijian;

    private String jieshushijian;

    private Integer jingpairenshu;

    private String shangpinxiangqing;

    private String shangpinzhuangtai;

    private String jingpairen;

    private String faburen;
    private String issh;

    @TableLogic
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getJingpaijiluCount() {
        return DB.name("jingpaijilu").where("shangpinxinxiid", id).count();
    }

    public Long getJingpaidingdanCount() {
        return DB.name("jingpaidingdan").where("shangpinxinxiid", id).count();
    }

    public String getShangpinbianhao() {
        return shangpinbianhao;
    }

    public void setShangpinbianhao(String shangpinbianhao) {
        this.shangpinbianhao = shangpinbianhao == null ? "" : shangpinbianhao.trim();
    }

    public Integer getFenlei() {
        return fenlei;
    }

    public void setFenlei(Integer fenlei) {
        this.fenlei = fenlei == null ? 0 : fenlei;
    }

    public String getShangpinmingcheng() {
        return shangpinmingcheng;
    }

    public void setShangpinmingcheng(String shangpinmingcheng) {
        this.shangpinmingcheng = shangpinmingcheng == null ? "" : shangpinmingcheng.trim();
    }

    public String getShangpintupian() {
        return shangpintupian;
    }

    public void setShangpintupian(String shangpintupian) {
        this.shangpintupian = shangpintupian == null ? "" : shangpintupian.trim();
    }

    public Double getQipaijia() {
        return qipaijia;
    }

    public void setQipaijia(Double qipaijia) {
        this.qipaijia = qipaijia == null ? 0.0f : qipaijia;
    }

    public Double getJiajiafudu() {
        return jiajiafudu;
    }

    public void setJiajiafudu(Double jiajiafudu) {
        this.jiajiafudu = jiajiafudu == null ? 0.0f : jiajiafudu;
    }

    public Double getDangqianjingpaijia() {
        return dangqianjingpaijia;
    }

    public void setDangqianjingpaijia(Double dangqianjingpaijia) {
        this.dangqianjingpaijia = dangqianjingpaijia == null ? 0.0f : dangqianjingpaijia;
    }

    public String getQipaishijian() {
        return qipaishijian;
    }

    public void setQipaishijian(String qipaishijian) {
        this.qipaishijian = qipaishijian == null ? "" : qipaishijian.trim();
    }

    public String getJieshushijian() {
        return jieshushijian;
    }

    public void setJieshushijian(String jieshushijian) {
        this.jieshushijian = jieshushijian == null ? "" : jieshushijian.trim();
    }

    public Integer getJingpairenshu() {
        return jingpairenshu;
    }

    public void setJingpairenshu(Integer jingpairenshu) {
        this.jingpairenshu = jingpairenshu == null ? 0 : jingpairenshu;
    }

    public String getShangpinxiangqing() {
        return shangpinxiangqing;
    }

    public void setShangpinxiangqing(String shangpinxiangqing) {
        this.shangpinxiangqing = shangpinxiangqing == null ? "" : shangpinxiangqing.trim();
    }

    public String getShangpinzhuangtai() {
        return shangpinzhuangtai;
    }

    public void setShangpinzhuangtai(String shangpinzhuangtai) {
        this.shangpinzhuangtai = shangpinzhuangtai == null ? "" : shangpinzhuangtai.trim();
    }

    public String getJingpairen() {
        return jingpairen;
    }

    public void setJingpairen(String jingpairen) {
        this.jingpairen = jingpairen == null ? "" : jingpairen.trim();
    }

    public String getFaburen() {
        return faburen;
    }

    public void setFaburen(String faburen) {
        this.faburen = faburen == null ? "" : faburen.trim();
    }

    public String getIssh() {
        return issh;
    }

    public void setIssh(String issh) {
        this.issh = issh == null ? "" : issh.trim();
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
