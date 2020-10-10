package com.feiqu.system.pojo.cache;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.system.model.FqUser;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */
public class FqUserCache implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String createTime;
    private String nickname;
    private String icon;
    private Integer sex;
    private Integer isSingle;
    private Integer isMailBind;
    private String sign;
    private String city;
    private String username;
    private Integer qudouNum;
    private String birth;
    private String education;
    private String school;
    private Integer role;
    private String roleName;
    private Integer status;
    private Integer level;
    private String salt;
    /**
     * 趣龄 多少tian
     */
    private String qulin;

    public FqUserCache() {
    }

    public FqUserCache(FqUser fqUser) {
        this.setCity(fqUser.getCity());
        this.setCreateTime(DateUtil.formatDate(fqUser.getCreateTime()));
        this.setIcon(fqUser.getIcon());
        this.setId(fqUser.getId());
        this.setIsMailBind(fqUser.getIsMailBind());
        this.setIsSingle(fqUser.getIsSingle());
        this.setSex(fqUser.getSex());
        this.setSign(fqUser.getSign());
        this.setNickname(fqUser.getNickname());
        this.setUsername(fqUser.getUsername());
        this.setQudouNum(fqUser.getQudouNum());
        this.setBirth(fqUser.getBirth());
        this.setEducation(fqUser.getEducation());
        this.setSchool(fqUser.getSchool());
        this.setRole(fqUser.getRole());
        this.setStatus(fqUser.getStatus());
        this.setLevel(fqUser.getLevel());
        this.setRoleName(UserRoleEnum.getDescByValue(fqUser.getRole()));
        this.setQulin(DateUtil.formatBetween(fqUser.getCreateTime(),new Date(), BetweenFormater.Level.DAY));
        this.setSalt(fqUser.getSalt());
    }

    public FqUser transferFqUser(){
        FqUser fqUser = new FqUser();
        fqUser.setCity(this.getCity());
        fqUser.setCreateTime(DateUtil.parse(this.getCreateTime()));
        fqUser.setIcon(this.getIcon());
        fqUser.setId(this.getId());
        fqUser.setIsMailBind(this.getIsMailBind());
        fqUser.setIsSingle(this.getIsSingle());
        fqUser.setSex(this.getSex());
        fqUser.setSign(this.getSign());
        fqUser.setNickname(this.getNickname());
        fqUser.setUsername(this.getUsername());
        fqUser.setQudouNum(this.getQudouNum());
        fqUser.setBirth(this.getBirth());
        fqUser.setEducation(this.getEducation());
        fqUser.setSchool(this.getSchool());
        fqUser.setRole(this.getRole());
        fqUser.setStatus(this.status);
        fqUser.setLevel(this.level);
        return fqUser;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getQulin() {
        return qulin;
    }

    public void setQulin(String qulin) {
        this.qulin = qulin;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    public Integer getIsMailBind() {
        return isMailBind;
    }

    public void setIsMailBind(Integer isMailBind) {
        this.isMailBind = isMailBind;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getQudouNum() {
        return qudouNum;
    }

    public void setQudouNum(Integer qudouNum) {
        this.qudouNum = qudouNum;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(this.role);
    }
}
