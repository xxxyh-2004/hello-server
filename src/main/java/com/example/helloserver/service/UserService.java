package com.example.helloserver.service;

import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO dto);
    Result<String> login(UserDTO dto);
}