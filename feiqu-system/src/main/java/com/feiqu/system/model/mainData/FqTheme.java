package com.feiqu.system.model.mainData;

import java.io.Serializable;
import java.util.Date;

public class FqTheme implements Serializable {
    private Integer id;

    private Integer userId;

    private String title;

    private Date createTime;

    private Integer status;

    private Integer commentCount;
    private Integer likeCount;

    private String label;

    private Integer type;

    private String lastPubNickname;

    private Date lastPubTime;

    private Integer seeCount;

    /**
     * 图片链接，多个逗号隔开
     *
     * @mbg.generated
     */
    private String picUrl = "";

    /**
     * 图片前缀
     *
     * @mbg.generated
     */
    private String picPrefix;

    private String content;

    private String token;

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLastPubNickname() {
        return lastPubNickname;
    }

    public void setLastPubNickname(String lastPubNickname) {
        this.lastPubNickname = lastPubNickname;
    }

    public Date getLastPubTime() {
        return lastPubTime;
    }

    public void setLastPubTime(Date lastPubTime) {
        this.lastPubTime = lastPubTime;
    }

    public Integer getSeeCount() {
        return seeCount;
    }

    public void setSeeCount(Integer seeCount) {
        this.seeCount = seeCount;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicPrefix() {
        return picPrefix;
    }

    public void setPicPrefix(String picPrefix) {
        this.picPrefix = picPrefix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", createTime=").append(createTime);
        sb.append(", status=").append(status);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", label=").append(label);
        sb.append(", type=").append(type);
        sb.append(", lastPubNickname=").append(lastPubNickname);
        sb.append(", lastPubTime=").append(lastPubTime);
        sb.append(", seeCount=").append(seeCount);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", picPrefix=").append(picPrefix);
        sb.append(", content=").append(content);
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
        FqTheme other = (FqTheme) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCommentCount() == null ? other.getCommentCount() == null : this.getCommentCount().equals(other.getCommentCount()))
            && (this.getLabel() == null ? other.getLabel() == null : this.getLabel().equals(other.getLabel()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getLastPubNickname() == null ? other.getLastPubNickname() == null : this.getLastPubNickname().equals(other.getLastPubNickname()))
            && (this.getLastPubTime() == null ? other.getLastPubTime() == null : this.getLastPubTime().equals(other.getLastPubTime()))
            && (this.getSeeCount() == null ? other.getSeeCount() == null : this.getSeeCount().equals(other.getSeeCount()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getPicPrefix() == null ? other.getPicPrefix() == null : this.getPicPrefix().equals(other.getPicPrefix()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCommentCount() == null) ? 0 : getCommentCount().hashCode());
        result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLastPubNickname() == null) ? 0 : getLastPubNickname().hashCode());
        result = prime * result + ((getLastPubTime() == null) ? 0 : getLastPubTime().hashCode());
        result = prime * result + ((getSeeCount() == null) ? 0 : getSeeCount().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getPicPrefix() == null) ? 0 : getPicPrefix().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }
}