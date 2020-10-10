package com.feiqu.admin.controller.front;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front/user")
public class FqUserController extends BaseController {
    private String prefix = "front/user";
    private static Log logger = LogFactory.get();
    @Resource
    private FqUserService userService;

    @RequestMapping("")
    public String manage() {
        return prefix + "/manage";
    }

    @RequestMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page, String nickname, String userId, String username) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, BizConstant.DEAULT_PAGE_SIZE);
            FqUserExample example = new FqUserExample();
            example.setOrderByClause("create_time desc");
            FqUserExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(nickname)) {
                criteria.andNicknameLike("%" + nickname + "%");
            }
            if (StringUtils.isNotEmpty(username)) {
                criteria.andUsernameLike("%" + username + "%");
            }
            if (StringUtils.isNotEmpty(userId)) {
                criteria.andIdEqualTo(Integer.valueOf(userId));
            }
            List<FqUser> users = userService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(users);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("用户分页出错", e);
            return error("用户分页出错");
        }
        return result;
    }

    @RequestMapping("/manage/save")
    @ResponseBody
    public Object changeStatus(FqUser fqUser) {
        BaseResult result = new BaseResult();
        try {
            FqUser user = userService.selectByPrimaryKey(fqUser.getId());
            if (UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())) {
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            userService.updateByPrimaryKeySelective(fqUser);
//            CacheManager.refreshUserCacheByUid(fqUser.getId());
        } catch (Exception e) {
            logger.error("改变用户状态出错", e);
            return error();
        }
        return success();
    }
}
