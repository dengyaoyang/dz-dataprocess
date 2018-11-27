package com.cecgw.cq.util;

/**
 * @Auther:lifuyi
 * @Date: 2018/11/23 11:26
 * @Description:刷新redis的结果类
 */
public class RedisFlushResult {
    /**
     * 编码
     * 1:指定的表在ES中不存在
     * 2:字段不匹配
     * 3:修改成功
     * 4：无需修改
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;

    public RedisFlushResult() {
    }


    public RedisFlushResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
