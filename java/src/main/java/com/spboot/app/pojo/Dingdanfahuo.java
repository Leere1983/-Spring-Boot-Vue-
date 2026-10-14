package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("dingdanfahuo")
public class Dingdanfahuo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer jingpaidingdanid;

    private String shangpinbianhao;

    private String shangpinmingcheng;

    private String shangpintupian;

    private String jingpairen;

    private String faburen;

    private Integer fenlei;

    private String dingdanbianhao;

    private String wuliugongsi;

    private String wuliudanhao;

    private String fahuobeizhu;

    private String addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJingpaidingdanid() {
        return jingpaidingdanid;
    }

    public void setJingpaidingdanid(Integer jingpaidingdanid) {
        this.jingpaidingdanid = jingpaidingdanid == null ? 0 : jingpaidingdanid;
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

    public String getWuliugongsi() {
        return wuliugongsi;
    }

    public void setWuliugongsi(String wuliugongsi) {
        this.wuliugongsi = wuliugongsi == null ? "" : wuliugongsi.trim();
    }

    public String getWuliudanhao() {
        return wuliudanhao;
    }

    public void setWuliudanhao(String wuliudanhao) {
        this.wuliudanhao = wuliudanhao == null ? "" : wuliudanhao.trim();
    }

    public String getFahuobeizhu() {
        return fahuobeizhu;
    }

    public void setFahuobeizhu(String fahuobeizhu) {
        this.fahuobeizhu = fahuobeizhu == null ? "" : fahuobeizhu.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }
}
