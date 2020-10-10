package com.feiqu.web.controller.resource;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqChangeLogCollect;
import com.feiqu.system.model.FqChangeLogCollectExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqChangeLogCollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqChangeLogCollectcontroller
 * Created by cwd on 2019/3/23.
 */
@Controller
@RequestMapping("/fqChangeLogCollect")
public class FqChangeLogCollectController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqChangeLogCollectController.class);

    @Resource
    private FqChangeLogCollectService fqChangeLogCollectService;

    /**
     * 跳转到FqChangeLogCollect管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/fqChangeLogCollect/manage";
    }

    /**
     * 跳转到FqChangeLogCollect首页
     */
    @RequestMapping("")
    public String index(Model model,@RequestParam(defaultValue = "1") Integer page) {
        PageHelper.startPage(page,CommonConstant.DEAULT_PAGE_SIZE);
        FqChangeLogCollectExample example = new FqChangeLogCollectExample();
        List<FqChangeLogCollect> fqChangeLogCollects =  fqChangeLogCollectService.selectByExample(example);
        PageInfo pageInfo = new PageInfo(fqChangeLogCollects);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.LIST.getPosition()));
        return "/fqChangeLogCollect/index";
    }

    /**
     * ajax删除FqChangeLogCollect
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            fqChangeLogCollectService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新FqChangeLogCollect页面
     */
    @RequestMapping("/{fqChangeLogCollectId}")
    public Object fqChangeLogCollectEdit(@PathVariable Integer fqChangeLogCollectId, Model model) {
        FqChangeLogCollect fqChangeLogCollect = fqChangeLogCollectService.selectByPrimaryKey(fqChangeLogCollectId);
        fqChangeLogCollect.setContent(HtmlUtils.htmlUnescape(fqChangeLogCollect.getContent()));
        model.addAttribute("fqChangeLogCollect", fqChangeLogCollect);
        model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.DETAIL.getPosition()));
        FqChangeLogCollect fqChangeLogCollectUpdate = new FqChangeLogCollect();
        fqChangeLogCollectUpdate.setId(fqChangeLogCollectId);
        fqChangeLogCollectUpdate.setWatchCount(fqChangeLogCollect.getWatchCount() == null?1:fqChangeLogCollect.getWatchCount()+1);
        fqChangeLogCollectService.updateByPrimaryKeySelective(fqChangeLogCollectUpdate);
        return "/fqChangeLogCollect/detail";
    }

    /**
     * ajax更新FqChangeLogCollect
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(FqChangeLogCollect fqChangeLogCollect) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            int res;
            if (fqChangeLogCollect.getId() == null) {
                fqChangeLogCollect.setGmtCreate(new Date());
                fqChangeLogCollect.setWatchCount(0);
                res = fqChangeLogCollectService.insert(fqChangeLogCollect);
            } else {
                res = fqChangeLogCollectService.updateByPrimaryKeySelective(fqChangeLogCollect);
            }
            if(res == 0){
                return error();
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FqChangeLogCollect首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            FqChangeLogCollectExample example = new FqChangeLogCollectExample();
            example.setOrderByClause("gmt_create desc");
            List<FqChangeLogCollect> list = fqChangeLogCollectService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
        return result;
    }
}