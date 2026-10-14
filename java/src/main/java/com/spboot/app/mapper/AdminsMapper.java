package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Admins;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("管理员")
public interface AdminsMapper extends BaseMapper<Admins> {}
