package com.feiqu.admin.controller.front;

import cn.hutool.core.lang.Assert;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.model.FqFriendLinkExample;
import com.feiqu.system.service.mainData.FqFriendLinkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqFriendLinkcontroller
 * Created by cwd on 2019/1/9.
 */
@Controller
@RequestMapping("/front/friendLink")
public class FqFriendLinkController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqFriendLinkController.class);

    @Resource
    private FqFriendLinkService fqFriendLinkService;


    @GetMapping("")
    public String manage() {
        return "/front/friendLink/manage";
    }

    @GetMapping("/manage/list")
    @ResponseBody
    public Object manageList(Integer page, Integer limit) {
        BaseResult result = new BaseResult();
        try {
            if (limit > 20) {
                limit = 20;
            }
            PageHelper.startPage(page, limit);
            FqFriendLinkExample example = new FqFriendLinkExample();
            example.setOrderByClause("create_time desc");
            List<FqFriendLink> fqFriendLinks = fqFriendLinkService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(fqFriendLinks);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("友链分页出错", e);
            result.setCode("1");
            result.setMessage("友链分页出错");
        }
        return result;
    }

    /**
     * ajax删除
     */
    @ResponseBody
    @PostMapping("/manage/delete")
    @Log(title = "删除友链", businessType = BusinessType.DELETE)
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqFriendLink fqFriendLink = fqFriendLinkService.selectByPrimaryKey(id);
            Assert.notNull(fqFriendLink, "网址不能为空");
            fqFriendLinkService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("删除友链报错", e);
            result.setCode("1");
            result.setMessage(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * ajax更新FqWebsiteDir
     */
    @ResponseBody
    @PostMapping("/manage/save")
    @Log(title = "保存友链", businessType = BusinessType.INSERT)
    public Object edit(FqFriendLink fqFriendLink) {
        BaseResult result = new BaseResult();
        if (fqFriendLink.getId() == null) {
            fqFriendLink.setCreateTime(new Date());
            fqFriendLinkService.insert(fqFriendLink);
        } else {
            fqFriendLinkService.updateByPrimaryKeySelective(fqFriendLink);
        }
        return result;
    }
}