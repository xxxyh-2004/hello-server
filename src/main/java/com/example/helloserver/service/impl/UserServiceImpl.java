package com.example.helloserver.service.impl;

import com.example.helloserver.common.Result;
import com.example.helloserver.common.ResultCode;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.service.UserService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<String, String> userDb = new HashMap<>();

    @Override
    public Result<String> register(UserDTO dto) {
        if (userDb.containsKey(dto.getUsername())) {
            return Result.error(ResultCode.USER_EXIST);
        }
        userDb.put(dto.getUsername(), dto.getPassword());
        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO dto) {
        if (!userDb.containsKey(dto.getUsername())) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        if (!userDb.get(dto.getUsername()).equals(dto.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        return Result.success("登录成功");
    }
}