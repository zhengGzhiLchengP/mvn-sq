package com.feiqu.web.controller.user;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ActiveNumEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqSign;
import com.feiqu.system.model.FqSignExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.SignRes;
import com.feiqu.system.pojo.response.SignUserResponse;
import com.feiqu.system.service.mainData.FqSignService;
import com.feiqu.system.service.mainData.FqUserService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SignController
 *
 * @author chenweidong
 * @date 2017/11/17
 */
@Controller
@RequestMapping("sign")
public class SignController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(SignController.class);
    @Resource
    private WebUtil webUtil;
    @Resource
    private FqSignService signService;
    @Resource
    private FqUserService userService;

    @ResponseBody
    @PostMapping(value = "in")
    public Object signin() {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqSignExample example = new FqSignExample();
            example.createCriteria().andUserIdEqualTo(user.getId());
            FqSign sign = signService.selectFirstByExample(example);
            Date now = new Date();
            if(sign == null){
                sign = new FqSign();
                sign.setDays(1);
                sign.setSignTime(new Date());
                sign.setUserId(user.getId());
                sign.setSignDays(DateUtil.date().toString("dd"));
                sign.setMaxDays(1);
                signService.insert(sign);
            }else {
                if(sign.getSignTime().after(DateUtil.beginOfDay(now))){
                    result.setResult(ResultEnum.SIGN_ALREADY_TODAY);
                    return result;
                }
                //如果昨天签过到了
                if(sign.getSignTime().after(DateUtil.beginOfDay(DateUtil.yesterday()))){
                    sign.setDays(sign.getDays()+1);
                    sign.setMaxDays(sign.getMaxDays()+1);
                }else {
                    if(sign.getMaxDays()<sign.getDays()){
                        sign.setMaxDays(sign.getDays());
                    }
                    sign.setDays(1);
                }
                //将signdays更新1,2,3,5,.....
                if(DateUtil.date().dayOfMonth() == 1){//每月第一天清空为1
                    sign.setSignDays("1");
                } else if(sign.getSignTime().before(DateUtil.beginOfMonth(now))){
                    sign.setSignDays(DateUtil.date().toString("dd"));
                } else {
                    sign.setSignDays(StringUtils.isBlank(sign.getSignDays())?
                            DateUtil.date().toString("dd") : sign.getSignDays() + "," + DateUtil.date().toString("dd"));
                }
                sign.setSignTime(now);
                signService.updateByPrimaryKey(sign);
            }
            SignRes signStatus = new SignRes(sign,now);
            CommonUtils.addOrDelUserQudouNum(user,signStatus.getExperience());
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.SIGN_IN.getValue());
            result.setData(signStatus);
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "status")
    public Object status(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setData(null);
                return result;
            }
            FqSignExample example = new FqSignExample();
            Date now = new Date();
            example.createCriteria().andUserIdEqualTo(user.getId());
            FqSign sign = signService.selectFirstByExample(example);
            if(sign == null){
                result.setData(null);
                return result;
            }else {
                SignRes signStatus = new SignRes(sign,now);
                result.setData(signStatus);
            }
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
     *
     * @param forgetDay
     * @return
     */
    @ResponseBody
    @PostMapping(value = "makeUp")
    public Object status(Integer forgetDay) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqSignExample example = new FqSignExample();
            Date now = new Date();
            example.createCriteria().andUserIdEqualTo(user.getId());
            FqSign sign = signService.selectFirstByExample(example);
            if(sign == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                result.setMessage("用户没有签到记录");
                return result;
            }else {
                if(user.getQudouNum() < 5){
                    result.setResult(ResultEnum.PARAM_NULL);
                    result.setMessage("用户Q豆不足！");
                    return result;
                }
                SignRes signStatus = new SignRes(sign,now);
                //
                if(signStatus.getForgetDays().contains(forgetDay.toString())){
                    FqSign signUpdate = new FqSign();
                    signUpdate.setMaxDays(sign.getMaxDays() + 1);
                    signUpdate.setId(sign.getId());
                    signUpdate.setSignDays(sign.getSignDays()+","+forgetDay);
                    signUpdate.setDays(sign.getDays()+1);
                    signService.updateByPrimaryKeySelective(signUpdate);
                }else {
                    result.setResult(ResultEnum.FAIL);
                    result.setMessage("这一天你已经签过到了");
                    return result;
                }
                sign = signService.selectByPrimaryKey(sign.getId());
                signStatus = new SignRes(sign,now);
                result.setData(signStatus);
                CommonUtils.addOrDelUserQudouNum(user,-5);
            }
        } catch (Exception e) {
            logger.error("补签失败，补签天数"+forgetDay,e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "top")
    public Object top(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            PageHelper.startPage(0, 20,false);
            FqSignExample example = new FqSignExample();
            example.setOrderByClause("sign_time desc");
            List<SignUserResponse> signs = signService.selectWithUserByExample(example);//最新签到
            List<List<SignUserResponse>> lists = new ArrayList<List<SignUserResponse>>();
            lists.add(signs);
            example.clear();
            example.setOrderByClause("sign_time asc");
            example.createCriteria().andSignTimeGreaterThan(DateUtil.beginOfDay(new Date()));
            PageHelper.startPage(0, 20,false);
            List<SignUserResponse> signs2 = signService.selectWithUserByExample(example);//今日最快
            lists.add(signs2);
            example.clear();
            example.setOrderByClause("max_days desc");
            PageHelper.startPage(0, 20,false);
            List<SignUserResponse> signs3 = signService.selectWithUserByExample(example);//总签到榜
            lists.add(signs3);
            result.setData(lists);
        } catch (Exception e) {
            logger.error("签到失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }




}
