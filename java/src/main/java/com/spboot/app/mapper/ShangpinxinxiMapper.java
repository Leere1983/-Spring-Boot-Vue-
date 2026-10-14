package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Shangpinxinxi;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("商品信息")
public interface ShangpinxinxiMapper extends BaseMapper<Shangpinxinxi> {}
