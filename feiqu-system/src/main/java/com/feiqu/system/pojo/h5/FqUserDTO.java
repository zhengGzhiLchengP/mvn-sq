package com.feiqu.system.pojo.h5;

import com.feiqu.system.pojo.cache.FqUserCache;

public class FqUserDTO {
    private Integer id;
    private String createTime;
    private String nickname;
    private String icon;
//    private String sign;
    private String city;

    public FqUserDTO() {
    }

    public FqUserDTO(FqUserCache fqUserCache) {
        this.id=fqUserCache.getId();
        this.createTime=fqUserCache.getCreateTime();
        this.nickname=fqUserCache.getNickname();
        this.icon=fqUserCache.getIcon();
        this.city=fqUserCache.getCity();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
