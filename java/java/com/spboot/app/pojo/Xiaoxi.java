package com.spboot.app.pojo;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.*;
import com.spboot.app.controller.ChatController;

import java.io.Serializable;
import java.util.*;

@TableName("xiaoxi")
public class Xiaoxi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer siliaoid;
    private String bianhao;
    private String neirong;
    private String fasongren;
    private String fasongshijian;
    private String shifouzhakan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiliaoid() {
        return siliaoid;
    }

    public void setSiliaoid(Integer siliaoid) {
        this.siliaoid = siliaoid == null ? 0 : siliaoid;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao == null ? "" : bianhao.trim();
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong == null ? "" : neirong.trim();
    }

    public String getFasongren() {
        return fasongren;
    }

    public void setFasongren(String fasongren) {
        this.fasongren = fasongren == null ? "" : fasongren.trim();
    }

    public String getFasongshijian() {
        return fasongshijian;
    }

    public void setFasongshijian(String fasongshijian) {
        this.fasongshijian = fasongshijian == null ? "" : fasongshijian.trim();
    }

    public String getShifouzhakan() {
        return shifouzhakan;
    }

    public void setShifouzhakan(String shifouzhakan) {
        this.shifouzhakan = shifouzhakan == null ? "" : shifouzhakan.trim();
    }

    public Object getFasongrenObj(){
        return ChatController.getUser(fasongren); //DB.name(Xuesheng.class).where("yonghuming",fasongren).find();
    }
}
