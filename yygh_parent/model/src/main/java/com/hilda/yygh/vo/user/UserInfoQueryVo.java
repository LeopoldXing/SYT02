package com.hilda.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description="会员搜索对象")
public class UserInfoQueryVo {

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "认证状态")
    private Integer authStatus;

    @ApiModelProperty(value = "创建时间")
    private String createTimeBegin;

    @ApiModelProperty(value = "创建时间")
    private String createTimeEnd;

    public UserInfoQueryVo() {
    }

    public UserInfoQueryVo(String keyword, Integer status, Integer authStatus, String createTimeBegin, String createTimeEnd) {
        this.keyword = keyword;
        this.status = status;
        this.authStatus = authStatus;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public String toString() {
        return "UserInfoQueryVo{" +
                "keyword='" + keyword + '\'' +
                ", status=" + status +
                ", authStatus=" + authStatus +
                ", createTimeBegin='" + createTimeBegin + '\'' +
                ", createTimeEnd='" + createTimeEnd + '\'' +
                '}';
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoQueryVo that = (UserInfoQueryVo) o;
        return Objects.equals(keyword, that.keyword) && Objects.equals(status, that.status) && Objects.equals(authStatus, that.authStatus) && Objects.equals(createTimeBegin, that.createTimeBegin) && Objects.equals(createTimeEnd, that.createTimeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, status, authStatus, createTimeBegin, createTimeEnd);
    }
}
