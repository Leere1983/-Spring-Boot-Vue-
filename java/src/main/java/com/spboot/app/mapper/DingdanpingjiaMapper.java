package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Dingdanpingjia;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("订单评价")
public interface DingdanpingjiaMapper extends BaseMapper<Dingdanpingjia> {}
