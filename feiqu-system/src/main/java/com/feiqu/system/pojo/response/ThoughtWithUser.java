package com.feiqu.system.pojo.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.system.model.Thought;
import com.feiqu.system.pojo.cache.FqUserCache;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */
public class ThoughtWithUser extends Thought {

    private String username;

    private String nickname;

    private String icon;

    private Integer role;

    private String roleName;

    private boolean collected = false;

    private List<String> pictures;
    //图片
    private int imgPercent = 50;

    private String createDate;

    public ThoughtWithUser() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ThoughtWithUser(Thought thought) {
        super();
        this.setArea(thought.getArea());
        this.setCommentCount(thought.getCommentCount());
        this.setClickCount(thought.getCommentCount());
        this.setCreateTime(thought.getCreateTime());
        this.setId(thought.getId());
        this.setLikeCount(thought.getLikeCount());
        this.setThoughtContent(thought.getThoughtContent());
        this.setUserId(thought.getUserId());
        if(StringUtils.isNotEmpty(thought.getPicList())){
            this.setPictures(Arrays.asList(thought.getPicList().split(",")));
        }
        this.setCreateDate(getNiceDate(thought.getCreateTime(),null));
    }

    public void bindUser(FqUserCache user){
        this.setRoleName(UserRoleEnum.getDescByValue(user.getRole()));
        this.setNickname(user.getNickname());
        this.setIcon(user.getIcon());
    }


    public void dealCreateDate(Date now) {
        this.setCreateDate(getNiceDate(this.getCreateTime(),now));
    }

    private String getNiceDate(Date date,Date now) {
        if (null == date) return "";
        String result;
        now = now == null?new Date():now;
        long currentTime =  now.getTime() - date.getTime();
        int time = (int)(currentTime / 1000);
        if(time < 60) {
            result = "刚刚";
        } else if(time < 3600) {
            result = time/60 + "分钟前";
        } else if(time < 86400) {
            result = time/3600 + "小时前";
        } else if(time < 864000) {
            result = time/86400 + "天前";
        } else{
            result = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);
        }
        return result;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
        if(CollectionUtil.isNotEmpty(this.getPictures())){
            int size = this.getPictures().size();
            if(size == 2){
                this.setImgPercent(48);
            }else if(size >= 3){
                this.setImgPercent(31);
            }
        }
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public int getImgPercent() {
        return imgPercent;
    }

    public void setImgPercent(int imgPercent) {
        this.imgPercent = imgPercent;
    }

}
