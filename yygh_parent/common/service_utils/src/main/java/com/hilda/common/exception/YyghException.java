package com.hilda.common.exception;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class YyghException extends RuntimeException{

    @ApiModelProperty(value = "自定义状态码")
    private Integer code;

    @ApiModelProperty(value = "自定义状态信息")
    private String msg;

    public YyghException() {
    }

    public YyghException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public YyghException(String message, Integer code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public YyghException(String message, Throwable cause, Integer code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public YyghException(Throwable cause, Integer code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public YyghException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "YyghException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YyghException that = (YyghException) o;
        return Objects.equals(code, that.code) && Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg);
    }
}
