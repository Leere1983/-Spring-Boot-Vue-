package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Lunbotu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("轮播图")
public interface LunbotuMapper extends BaseMapper<Lunbotu> {}
