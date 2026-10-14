package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("jingpaijilu")
public class Jingpaijilu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer shangpinxinxiid;

    private String shangpinbianhao;

    private Integer fenlei;

    private String shangpinmingcheng;

    private String shangpintupian;

    private Double qipaijia;

    private String faburen;

    private Double jiajiafudu;

    private Double jingpaijia;

    private String jingpairen;

    private String addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFaburen() {
        return faburen;
    }

    public void setFaburen(String faburen) {
        this.faburen = faburen == null ? "" : faburen.trim();
    }

    public Double getJiajiafudu() {
        return jiajiafudu;
    }

    public void setJiajiafudu(Double jiajiafudu) {
        this.jiajiafudu = jiajiafudu == null ? 0.0f : jiajiafudu;
    }

    public Double getJingpaijia() {
        return jingpaijia;
    }

    public void setJingpaijia(Double jingpaijia) {
        this.jingpaijia = jingpaijia == null ? 0.0f : jingpaijia;
    }

    public String getJingpairen() {
        return jingpairen;
    }

    public void setJingpairen(String jingpairen) {
        this.jingpairen = jingpairen == null ? "" : jingpairen.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }
}
