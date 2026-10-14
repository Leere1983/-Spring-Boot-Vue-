package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Jingpaijilu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("竞拍记录")
public interface JingpaijiluMapper extends BaseMapper<Jingpaijilu> {}
