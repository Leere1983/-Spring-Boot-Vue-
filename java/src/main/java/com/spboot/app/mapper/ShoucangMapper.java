package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Shoucang;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("收藏")
public interface ShoucangMapper extends BaseMapper<Shoucang> {}
