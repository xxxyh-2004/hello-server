package com.example.helloserver.controller;

import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.UserInfo;
import com.example.helloserver.service.UserService;
import com.example.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 对应任务5要求的所有接口 + 任务6分页查询 + 任务7多表联查+Redis缓存
 */
@RestController
@RequestMapping("/api/users")     // 任务书要求的路径
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping
    public Result<String> register(@RequestBody UserDTO dto) {
        return userService.register(dto);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO dto) {
        return userService.login(dto);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<String> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ==================== 新增：任务6 - 分页查询用户列表 ====================
    /**
     * 分页查询用户列表
     * GET /api/users/page?pageNum=1&pageSize=5
     */
    @GetMapping("/page")
    public Result<Object> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }

    // ==================== 任务7：多表联查 + Redis 缓存相关接口 ====================
    /**
     * 5. 查询用户详情（多表联查 + Redis）
     */
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long userId) {
        return userService.getUserDetail(userId);
    }

    /**
     * 6. 更新用户扩展信息
     */
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(@PathVariable("id") Long userId,
                                         @RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }

    /**
     * 7. 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }
}