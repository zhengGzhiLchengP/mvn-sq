package com.feiqu.system.pojo.response;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.system.model.FqSign;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * SignRes
 *
 * @author chenweidong
 * @date 2017/11/17
 */
public class SignRes {
    private Integer days;
    private Integer maxDays;

    private Integer userId;

    private Date signTime;

    private Boolean signed;

    private Integer experience;

    private List<String> forgetDays;

    private String[] signDays;

    public SignRes() {
    }

    public SignRes(FqSign sign) {
        this.setDays(sign.getDays());
        this.setSignTime(sign.getSignTime());
        this.setUserId(sign.getUserId());

    }

    public SignRes(FqSign sign,Date now) {
        this.setMaxDays(sign.getMaxDays());
        this.setDays(sign.getDays());
        this.setSignTime(sign.getSignTime());
        this.setUserId(sign.getUserId());
        String[] signDays = StringUtils.split(sign.getSignDays(),",");
        if(StringUtils.isBlank(sign.getSignDays())){
            this.setForgetDays(getForgetDaysWay(DateUtil.dayOfMonth(now)));
        }else {
            this.setForgetDays(getForgetDaysWay(DateUtil.dayOfMonth(now),signDays));
        }
        this.setSignDays(signDays);
        if(sign.getSignTime().before(DateUtil.beginOfDay(now))){
            this.setSigned(false);
        }else {
            this.setSigned(true);
        }
        if(this.getMaxDays() < 5){
            this.setExperience(BizConstant.SIGN_DAYS_QUDOU_NUM_5);
        }else if(this.getMaxDays() < 15){
            this.setExperience(BizConstant.SIGN_DAYS_QUDOU_NUM_15);
        }else if(this.getMaxDays() < 30){
            this.setExperience(BizConstant.SIGN_DAYS_QUDOU_NUM_30);
        }else {
            this.setExperience(BizConstant.SIGN_DAYS_QUDOU_NUM_30_MORE);
        }
    }

    private List<String> getForgetDaysWay(int dayOfMonth, String[] signDays) {
        List<String> list = Lists.newArrayList();
        List<String> signDaysStr = Arrays.asList(signDays);
        for(int i = 1;i < dayOfMonth;i++){
            if(signDaysStr.contains(String.valueOf(i)) || signDaysStr.contains("0"+ i)){
                continue;
            }
            list.add(String.valueOf(i));
        }
        return list;
    }

    private List<String> getForgetDaysWay(int dayOfMonth) {
        List<String> list = Lists.newArrayList();
        for(int i = 1;i<=dayOfMonth;i++){
            list.add(String.valueOf(i));
        }
        return list;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public List<String> getForgetDays() {
        return forgetDays;
    }

    public void setForgetDays(List<String> forgetDays) {
        this.forgetDays = forgetDays;
    }

    public String[] getSignDays() {
        return signDays;
    }

    public void setSignDays(String[] signDays) {
        this.signDays = signDays;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(Integer maxDays) {
        this.maxDays = maxDays;
    }
}
