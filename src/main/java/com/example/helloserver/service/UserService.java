package com.example.helloserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.User;

/**
 * UserService 接口
 * 继承 MyBatis-Plus 的 IService<T>，自动获得大量基础 CRUD 方法
 */
public interface UserService extends IService<User> {

    Result<String> register(UserDTO dto);

    Result<String> login(UserDTO dto);

    Result<String> getUserById(Long id);
}