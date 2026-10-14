package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Gonggao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("公告")
public interface GonggaoMapper extends BaseMapper<Gonggao> {}
