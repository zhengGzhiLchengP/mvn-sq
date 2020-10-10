package com.feiqu.system.model;

import java.io.Serializable;
import java.util.Date;

public class FqUserAuth implements Serializable {
    private Integer id;

    /**
     * 授权的用户
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     * 被授权的用户
     *
     * @mbg.generated
     */
    private Integer authedUserId;

    /**
     * 被授权时间
     *
     * @mbg.generated
     */
    private Date authTime;

    /**
     * 删除标志
     *
     * @mbg.generated
     */
    private Integer delFlag;

    /**
     * 授权类型
     *
     * @mbg.generated
     */
    private Integer authType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuthedUserId() {
        return authedUserId;
    }

    public void setAuthedUserId(Integer authedUserId) {
        this.authedUserId = authedUserId;
    }

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", authedUserId=").append(authedUserId);
        sb.append(", authTime=").append(authTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", authType=").append(authType);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FqUserAuth other = (FqUserAuth) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAuthedUserId() == null ? other.getAuthedUserId() == null : this.getAuthedUserId().equals(other.getAuthedUserId()))
            && (this.getAuthTime() == null ? other.getAuthTime() == null : this.getAuthTime().equals(other.getAuthTime()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getAuthType() == null ? other.getAuthType() == null : this.getAuthType().equals(other.getAuthType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAuthedUserId() == null) ? 0 : getAuthedUserId().hashCode());
        result = prime * result + ((getAuthTime() == null) ? 0 : getAuthTime().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getAuthType() == null) ? 0 : getAuthType().hashCode());
        return result;
    }
}