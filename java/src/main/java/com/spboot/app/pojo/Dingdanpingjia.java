package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

@TableName("dingdanpingjia")
public class Dingdanpingjia implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer jingpaidingdanid;

    private String shangpinbianhao;

    private String shangpinmingcheng;

    private Integer fenlei;

    private String faburen;

    private String dingdanbianhao;

    private String jingpairen;

    private Integer pingfen;

    private String pingjia;

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

    public Integer getFenlei() {
        return fenlei;
    }

    public void setFenlei(Integer fenlei) {
        this.fenlei = fenlei == null ? 0 : fenlei;
    }

    public String getFaburen() {
        return faburen;
    }

    public void setFaburen(String faburen) {
        this.faburen = faburen == null ? "" : faburen.trim();
    }

    public String getDingdanbianhao() {
        return dingdanbianhao;
    }

    public void setDingdanbianhao(String dingdanbianhao) {
        this.dingdanbianhao = dingdanbianhao == null ? "" : dingdanbianhao.trim();
    }

    public String getJingpairen() {
        return jingpairen;
    }

    public void setJingpairen(String jingpairen) {
        this.jingpairen = jingpairen == null ? "" : jingpairen.trim();
    }

    public Integer getPingfen() {
        return pingfen;
    }

    public void setPingfen(Integer pingfen) {
        this.pingfen = pingfen == null ? 0 : pingfen;
    }

    public String getPingjia() {
        return pingjia;
    }

    public void setPingjia(String pingjia) {
        this.pingjia = pingjia == null ? "" : pingjia.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }
}
