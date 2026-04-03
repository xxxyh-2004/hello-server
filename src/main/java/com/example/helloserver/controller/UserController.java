package com.example.helloserver.controller;

import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.User;
import com.example.helloserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. 查询
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable Long id) {
        return Result.success("查询成功，用户ID：" + id);
    }

    // 2. 新增
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        return Result.success("新增成功：" + user.getName());
    }

    // 3. 修改
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        return Result.success("修改ID：" + id + " → " + user.getName());
    }

    // 4. 删除
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return Result.success("删除成功，用户ID：" + id);
    }

    // 5. 登录
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO dto) {
        return userService.login(dto);
    }

    // 6. 注册
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserDTO dto) {
        return userService.register(dto);
    }
}