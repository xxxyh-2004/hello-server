package com.example.helloserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.helloserver.entity.UserInfo;
import com.example.helloserver.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update; // 新增导入

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 多表联查：sys_user + user_info（任务7核心）
     */
    @Select("""
            SELECT 
                u.id AS userId,
                u.username,
                i.real_name AS realName,
                i.phone,
                i.address
            FROM sys_user u
            LEFT JOIN user_info i ON u.id = i.user_id
            WHERE u.id = #{userId}
            """)
    UserDetailVO getUserDetail(@Param("userId") Long userId);

    // 新增：按user_id更新用户信息（解决更新失败的核心）
    @Update("""
            UPDATE user_info 
            SET real_name = #{realName}, phone = #{phone}, address = #{address}
            WHERE user_id = #{userId}
            """)
    int updateByUserId(UserInfo userInfo);
}