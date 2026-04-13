package com.example.helloserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.User;
import com.example.helloserver.mapper.UserMapper;
import com.example.helloserver.service.UserService;
import org.springframework.stereotype.Service;

/**
 * UserService 实现类
 * 继承 ServiceImpl 后自动获得大量基础 CRUD 方法
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Result<String> register(UserDTO dto) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User exist = this.getOne(wrapper);

        if (exist != null) {
            return Result.error("用户名已存在");     // 或使用 ResultCode.USER_HAS_EXISTED（推荐）
        }

        // 2. 组装实体并保存到数据库
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        this.save(user);        // ServiceImpl 提供的 save 方法

        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO dto) {
        // 1. 根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = this.getOne(wrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 3. 校验密码
        if (!user.getPassword().equals(dto.getPassword())) {
            return Result.error("密码错误");
        }

        return Result.success("登录成功！");
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success("查询成功，用户名为：" + user.getUsername());
    }

    // ==================== 新增：分页查询用户列表 ====================
    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象（当前页码，每页条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询（this.page() 是 ServiceImpl 提供的方法，自动生成 COUNT + LIMIT）
        Page<User> resultPage = this.page(pageParam);

        // 3. 返回分页结果（里面包含 records、total、current、pages 等完整信息）
        return Result.success(resultPage);
    }
}