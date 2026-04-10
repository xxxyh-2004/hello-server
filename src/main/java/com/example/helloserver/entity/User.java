package com.example.helloserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户实体类
 * 对应数据库表：sys_user
 */
@Data
@TableName("sys_user")           // 必须和数据库表名完全一致！
public class User {

    @TableId(type = IdType.AUTO)  // MySQL 自增主键
    private Long id;

    private String username;
    private String password;
}