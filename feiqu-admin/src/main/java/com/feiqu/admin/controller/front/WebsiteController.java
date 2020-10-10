package com.feiqu.admin.controller.front;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;
import com.feiqu.system.service.mainData.FqWebsiteDirService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front/website")
public class WebsiteController {
    private static Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Resource
    private FqWebsiteDirService fqWebsiteDirService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("")
    public String manage() {
        return "/front/websiteDir/manage";
    }

    @GetMapping("/manage/list")
    @ResponseBody
    public Object manageList(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit, FqWebsiteDir websiteDir) {
        BaseResult result = new BaseResult();
        try {
            if (limit > 20) {
                limit = 20;
            }
            PageHelper.startPage(page, limit);
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            FqWebsiteDirExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(websiteDir.getName())) {
                criteria.andNameLike("%" + websiteDir.getName() + "%");
            }
            if (null != websiteDir.getId()) {
                criteria.andIdEqualTo(websiteDir.getId());
            }
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<FqWebsiteDir> fqWebsiteDirs = fqWebsiteDirService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(fqWebsiteDirs);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("网址分页出错", e);
            result.setCode("1");
            result.setMessage("网址分页出错");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/add")
    public Object add(FqWebsiteDir fqWebsiteDir, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(fqWebsiteDir.getName()) || StringUtils.isBlank(fqWebsiteDir.getType())) {
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }
        if (!ReUtil.isMatch(PatternPool.URL, fqWebsiteDir.getUrl())) {
            result.setResult(ResultEnum.WEBSITE_URL_ERROR);
            return result;
        }
        try {
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            example.createCriteria().andUrlEqualTo(fqWebsiteDir.getUrl()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqWebsiteDir fqWebsiteDirDB = fqWebsiteDirService.selectFirstByExample(example);
            if (fqWebsiteDirDB != null) {
                result.setResult(ResultEnum.WEBSITE_URL_EXISTS);
                return result;
            }
            fqWebsiteDir.setDelFlag(YesNoEnum.NO.getValue());
            fqWebsiteDir.setUserId(1);
            fqWebsiteDir.setCreateTime(new Date());
            fqWebsiteDirService.insert(fqWebsiteDir);
            delWebsiteCache();
        } catch (Exception e) {
            logger.error("发生系统错误", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    private void delWebsiteCache() {
        stringRedisTemplate.delete(BizConstant.FQ_WEBSITE_ALL);
    }

    /**
     * ajax删除FqWebsiteDir
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqWebsiteDir fqWebsiteDir = fqWebsiteDirService.selectByPrimaryKey(id);
            Assert.notNull(fqWebsiteDir, "网址不能为空");
            fqWebsiteDir.setDelFlag(YesNoEnum.YES.getValue());
            fqWebsiteDirService.updateByPrimaryKey(fqWebsiteDir);
            delWebsiteCache();
        } catch (Exception e) {
            logger.error("删除网址报错", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * ajax更新FqWebsiteDir
     */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqWebsiteDir fqWebsiteDir) {
        BaseResult result = new BaseResult();
        fqWebsiteDirService.updateByPrimaryKeySelective(fqWebsiteDir);
        delWebsiteCache();
        return result;
    }
}
