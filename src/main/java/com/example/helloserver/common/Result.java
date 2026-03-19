package com.example.helloserver.common;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 无参构造（Spring 序列化需要）
    public Result() {}

    // 成功：带数据
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(data);
        return r;
    }

    // 成功：无数据（重载）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败：自定义状态码（推荐）
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> r = new Result<>();
        r.setCode(resultCode.getCode());
        r.setMsg(resultCode.getMsg());
        r.setData(null);
        return r;
    }

    // 失败：兼容你原来的写法（传字符串 msg，状态码固定 500）
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.ERROR.getCode());
        r.setMsg(msg);
        r.setData(null);
        return r;
    }

    // getter & setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}