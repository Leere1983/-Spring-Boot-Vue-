package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Siliao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("私聊")
public interface SiliaoMapper extends BaseMapper<Siliao> {}
