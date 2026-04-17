package com.example.helloserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.helloserver.common.Result;
import com.example.helloserver.dto.UserDTO;
import com.example.helloserver.entity.User;
import com.example.helloserver.entity.UserInfo;
import com.example.helloserver.vo.UserDetailVO;

/**
 * UserService 接口
 * 继承 MyBatis-Plus 的 IService<T>，自动获得大量基础 CRUD 方法
 */
public interface UserService extends IService<User> {

    Result<String> register(UserDTO dto);

    Result<String> login(UserDTO dto);

    Result<String> getUserById(Long id);

    // ==================== 新增：分页查询用户列表 ====================
    /**
     * 获取用户分页数据
     * @param pageNum  当前页码（从1开始）
     * @param pageSize 每页显示条数
     * @return 分页结果（包含 records、total、current、pages 等信息）
     */
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);

    // ==================== 任务7 新增方法（多表联查 + Redis 缓存） ====================
    /**
     * 查询用户详情（sys_user + user_info 联查 + Redis 缓存）
     */
    Result<UserDetailVO> getUserDetail(Long userId);

    /**
     * 更新用户扩展信息（同时删除 Redis 缓存）
     */
    Result<String> updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户（同时删除 Redis 缓存）
     */
    Result<String> deleteUser(Long userId);
}