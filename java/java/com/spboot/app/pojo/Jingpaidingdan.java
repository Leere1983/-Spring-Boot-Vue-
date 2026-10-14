package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("jingpaidingdan")
public class Jingpaidingdan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer shangpinxinxiid;

    private String shangpinbianhao;

    private String shangpinmingcheng;

    private String shangpintupian;

    private Double dangqianjingpaijia;

    private String jingpairen;

    private String faburen;

    private Integer fenlei;

    private String dingdanbianhao;

    private String dingdanzhuangtai;

    private Double zhifujine;

    private Double shouxufei;

    private String lianxiren;

    private String lianxidianhua;

    private String lianxidizhi;

    private String addtime;
    private String issh;
    private String iszf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDingdanpingjiaCount() {
        return DB.name("dingdanpingjia").where("jingpaidingdanid", id).count();
    }

    public Long getDingdanfahuoCount() {
        return DB.name("dingdanfahuo").where("jingpaidingdanid", id).count();
    }

    public Long getQuerenshouhuoCount() {
        return DB.name("querenshouhuo").where("jingpaidingdanid", id).count();
    }

    public Integer getShangpinxinxiid() {
        return shangpinxinxiid;
    }

    public void setShangpinxinxiid(Integer shangpinxinxiid) {
        this.shangpinxinxiid = shangpinxinxiid == null ? 0 : shangpinxinxiid;
    }

    public String getShangpinbianhao() {
        return shangpinbianhao;
    }

    public void setShangpinbianhao(String shangpinbianhao) {
        this.shangpinbianhao = shangpinbianhao == null ? "" : shangpinbianhao.trim();
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

    public Double getDangqianjingpaijia() {
        return dangqianjingpaijia;
    }

    public void setDangqianjingpaijia(Double dangqianjingpaijia) {
        this.dangqianjingpaijia = dangqianjingpaijia == null ? 0.0f : dangqianjingpaijia;
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

    public Integer getFenlei() {
        return fenlei;
    }

    public void setFenlei(Integer fenlei) {
        this.fenlei = fenlei == null ? 0 : fenlei;
    }

    public String getDingdanbianhao() {
        return dingdanbianhao;
    }

    public void setDingdanbianhao(String dingdanbianhao) {
        this.dingdanbianhao = dingdanbianhao == null ? "" : dingdanbianhao.trim();
    }

    public String getDingdanzhuangtai() {
        return dingdanzhuangtai;
    }

    public void setDingdanzhuangtai(String dingdanzhuangtai) {
        this.dingdanzhuangtai = dingdanzhuangtai == null ? "" : dingdanzhuangtai.trim();
    }

    public Double getZhifujine() {
        return zhifujine;
    }

    public void setZhifujine(Double zhifujine) {
        this.zhifujine = zhifujine == null ? 0.0f : zhifujine;
    }

    public Double getShouxufei() {
        return shouxufei;
    }

    public void setShouxufei(Double shouxufei) {
        this.shouxufei = shouxufei == null ? 0.0f : shouxufei;
    }

    public String getLianxiren() {
        return lianxiren;
    }

    public void setLianxiren(String lianxiren) {
        this.lianxiren = lianxiren == null ? "" : lianxiren.trim();
    }

    public String getLianxidianhua() {
        return lianxidianhua;
    }

    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua == null ? "" : lianxidianhua.trim();
    }

    public String getLianxidizhi() {
        return lianxidizhi;
    }

    public void setLianxidizhi(String lianxidizhi) {
        this.lianxidizhi = lianxidizhi == null ? "" : lianxidizhi.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }

    public String getIssh() {
        return issh;
    }

    public void setIssh(String issh) {
        this.issh = issh == null ? "" : issh.trim();
    }

    public String getIszf() {
        return iszf;
    }

    public void setIszf(String iszf) {
        this.iszf = iszf == null ? "" : iszf.trim();
    }
}
