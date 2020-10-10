package com.feiqu.admin.controller.front;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.FqNotice;
import com.feiqu.system.model.FqNoticeExample;
import com.feiqu.system.service.mainData.FqNoticeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqNoticecontroller
 * Created by cwd on 2017/11/9.
 */
@Controller
@RequestMapping("/front/fqNotice")
public class FqNoticeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqNoticeController.class);

    @Resource
    private FqNoticeService fqNoticeService;


    /**
     * 跳转到FqNotice首页
     */
    @RequestMapping("")
    public String index() {
        return "/front/fqNotice/index";
    }


    /**
     * 添加FqNotice页面
     */
    @RequestMapping("/add")
    public String fqNotice_add() {
        return "/front/fqNotice/add";
    }

    /**
     * ajax添加FqNotice
     */
    @ResponseBody
    @PostMapping("/add")
    @Log(title = "新增通知", businessType = BusinessType.INSERT)
    public Object add(FqNotice fqNotice) {
        BaseResult result = new BaseResult();
        try {
            Assert.notEmpty(fqNotice.getContent());
            Assert.notEmpty(fqNotice.getTitle());
            Assert.notNull(fqNotice.getIsShow());
            fqNotice.setCreateTime(new Date());
            fqNotice.setIcon("http://www.f2qu.com/img/c-logo39.png");
            fqNotice.setCommentNum(0);
            fqNotice.setType("");
            fqNoticeService.insert(fqNotice);
            FqNoticeExample noticeExample = new FqNoticeExample();
            noticeExample.createCriteria().andIdNotEqualTo(fqNotice.getId()).andIsShowEqualTo(YesNoEnum.YES.getValue());
            List<FqNotice> notices = fqNoticeService.selectByExample(noticeExample);
            if (CollectionUtil.isNotEmpty(notices)) {
                for (FqNotice fqNotice1 : notices) {
                    fqNotice1.setFqOrder(fqNotice1.getFqOrder() == null ? 1 : fqNotice1.getFqOrder() + 1);
                    fqNoticeService.updateByPrimaryKey(fqNotice1);
                }
            }
        } catch (Exception e) {
            logger.error("notice add error", e);
            return error("添加失败");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/update")
    @Log(title = "更新通知", businessType = BusinessType.UPDATE)
    public Object update(FqNotice fqNotice) {
        BaseResult result = new BaseResult();
        try {
            fqNoticeService.updateByPrimaryKeySelective(fqNotice);
        } catch (Exception e) {
            logger.error("notice add error", e);
        }
        return result;
    }

    /**
     * ajax删除FqNotice
     */
    @ResponseBody
    @RequestMapping("/delete")
    @Log(title = "删除通知", businessType = BusinessType.DELETE)
    public Object delete(@RequestParam Integer id) {
        fqNoticeService.deleteByPrimaryKey(id);
        return success();
    }

    /**
     * 更新FqNotice页面
     */
    @RequestMapping("/edit/{fqNoticeId}")
    public Object fqNoticeEdit(@PathVariable Integer fqNoticeId, Model model) {
        FqNotice fqNotice = fqNoticeService.selectByPrimaryKey(fqNoticeId);
        model.addAttribute("fqNotice", fqNotice);
        return "/FqNotice/edit";
    }


    /**
     * 查询FqNotice首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        BaseResult result = new BaseResult();
        FqNoticeExample example = new FqNoticeExample();
        example.setOrderByClause("ID desc");
        List<FqNotice> list = fqNoticeService.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        result.setData(pageInfo);
        return result;
    }
}