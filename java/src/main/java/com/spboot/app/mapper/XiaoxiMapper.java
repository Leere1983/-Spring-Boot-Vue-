package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Xiaoxi;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("消息")
public interface XiaoxiMapper extends BaseMapper<Xiaoxi> {}
