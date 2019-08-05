package com.shiyang.pojo;

/**
 * @title: Result
 * @description:
 * @author: ShiYang
 * @date: 2019/07/20
 */
public class Result {

    /**
     * 操作是否成功
     */
    private Boolean success;
    /**
     * 返回的消息
     */
    private String message;
    /**
     * 返回的数据
     */
    private Object result;

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(Boolean success, String message, Object result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
