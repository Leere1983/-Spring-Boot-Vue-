package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Shangjia;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("商家")
public interface ShangjiaMapper extends BaseMapper<Shangjia> {}
