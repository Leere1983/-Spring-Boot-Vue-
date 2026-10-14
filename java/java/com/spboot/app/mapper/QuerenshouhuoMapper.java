package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Querenshouhuo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("确认收货")
public interface QuerenshouhuoMapper extends BaseMapper<Querenshouhuo> {}
