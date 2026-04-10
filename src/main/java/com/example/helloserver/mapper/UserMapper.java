package com.example.helloserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.helloserver.entity.User;

/**
 * User 数据访问层
 * 注意：不要在此处添加 @Mapper 注解！
 * 因为启动类上已经使用了 @MapperScan
 */
public interface UserMapper extends BaseMapper<User> {
}