package com.example.helloserver.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.User;
import com.example.helloserver.entity.UserInfo;
import com.example.helloserver.mapper.UserInfoMapper;
import com.example.helloserver.mapper.UserMapper;
import com.example.helloserver.service.UserService;
import com.example.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * UserService 实现类（包含Redis缓存 + 多表联查 + 数据更新/删除）
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    // Redis缓存Key前缀
    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @Override
    public Result<String> register(UserDTO dto) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User exist = this.getOne(wrapper);

        if (exist != null) {
            return Result.error("用户名已存在");
        }

        // 2. 组装实体并保存到数据库
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        this.save(user);
        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = this.getOne(wrapper);

        if (user == null) {
            return Result.error("用户不存在");
        }

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

    // 分页查询用户列表
    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        Page<User> resultPage = this.page(pageParam);
        return Result.success(resultPage);
    }

    // 多表联查 + Redis缓存查询用户详情
    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先查Redis缓存
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isBlank()) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(cacheVO);
            } catch (Exception e) {
                // 缓存反序列化失败，删除脏数据
                redisTemplate.delete(key);
            }
        }

        // 2. 缓存未命中，查数据库
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error("用户不存在");
        }

        // 3. 写回Redis缓存（10分钟过期）
        redisTemplate.opsForValue().set(
                key,
                JSONUtil.toJsonStr(detail),
                10, TimeUnit.MINUTES);

        return Result.success(detail);
    }

    // 更新用户信息 + 删除Redis缓存（已修复更新失败问题）
    @Override
    public Result<String> updateUserInfo(UserInfo userInfo) {
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error("参数不能为空");
        }

        // 关键修改：按user_id更新（替代原updateById）
        int count = userInfoMapper.updateByUserId(userInfo);
        boolean success = count > 0;

        if (success) {
            // 删除缓存，保证数据一致性
            String key = CACHE_KEY_PREFIX + userInfo.getUserId();
            redisTemplate.delete(key);
            return Result.success("用户信息更新成功！");
        }
        return Result.error("更新失败");
    }

    // 删除用户 + 事务 + 删除Redis缓存
    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        // 删除数据库用户
        boolean success = this.removeById(userId);

        if (success) {
            // 删除Redis缓存
            String key = CACHE_KEY_PREFIX + userId;
            redisTemplate.delete(key);
            return Result.success("用户删除成功！");
        }
        return Result.error("删除失败");
    }
}