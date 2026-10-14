package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Shangpinfenlei;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("商品分类")
public interface ShangpinfenleiMapper extends BaseMapper<Shangpinfenlei> {}
