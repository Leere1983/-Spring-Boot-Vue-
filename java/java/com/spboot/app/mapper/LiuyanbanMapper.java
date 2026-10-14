package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Liuyanban;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("留言板")
public interface LiuyanbanMapper extends BaseMapper<Liuyanban> {}
