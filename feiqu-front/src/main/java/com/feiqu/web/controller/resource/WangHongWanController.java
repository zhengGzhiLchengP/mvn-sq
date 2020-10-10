package com.feiqu.web.controller.resource;

import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.WangHongWan;
import com.feiqu.system.model.WangHongWanExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.WangHongWanDTO;
import com.feiqu.system.service.mainData.WangHongWanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * WangHongWancontroller
 * Created by cwd on 2018/12/22.
 */
@Controller
@RequestMapping("/wangHong")
public class WangHongWanController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WangHongWanController.class);

    @Resource
    private WangHongWanService wangHongWanService;

    /**
     * 跳转到WangHongWan首页
     */
    @RequestMapping("")
    public String index(@RequestParam(defaultValue = "1") Integer p, Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return loginRedirect("/wangHong");
        }
        PageHelper.startPage(p, CommonConstant.DEAULT_PAGE_SIZE);
        WangHongWanExample example = new WangHongWanExample();
        example.setOrderByClause("ID desc");
        List<WangHongWan> wangHongWans = wangHongWanService.selectByExample(example);
        PageInfo page = new PageInfo(wangHongWans);
        model.addAttribute("pageIndex",p);
        model.addAttribute("pageSize",CommonConstant.DEAULT_PAGE_SIZE);
        model.addAttribute("count",page.getTotal());
        List<WangHongWanDTO> wangHongWanDTOS = Lists.newArrayList();
        wangHongWans.forEach(wangHongWan -> {
            WangHongWanDTO wangHongWanDTO = new WangHongWanDTO();
            wangHongWanDTO.setArea(wangHongWan.getArea());
            wangHongWanDTO.setAuthor(wangHongWan.getAuthor());
            wangHongWanDTO.setContent(wangHongWan.getContent());
            wangHongWanDTO.setId(wangHongWan.getId());
            List<String> imgs = Arrays.asList(wangHongWan.getPicList().split(","));
            imgs = imgs.stream().map(e->e+"?x-oss-process=style/img_thum3").collect(Collectors.toList());
            wangHongWanDTO.setImgs(imgs);
            wangHongWanDTOS.add(wangHongWanDTO);
        });
        model.addAttribute("wangHongWans",wangHongWanDTOS);
        return "/wh/index";
    }


    /**
     * 更新WangHongWan页面
     */
    @RequestMapping("/{wangHongWanId}")
    public Object wangHongWanEdit(@PathVariable Long wangHongWanId, Model model) {
        WangHongWan wangHongWan = wangHongWanService.selectByPrimaryKey(wangHongWanId);
        WangHongWanDTO wangHongWanDTO = new WangHongWanDTO();
        wangHongWanDTO.setArea(wangHongWan.getArea());
        wangHongWanDTO.setAuthor(wangHongWan.getAuthor());
        wangHongWanDTO.setContent(wangHongWan.getContent());
        wangHongWanDTO.setId(wangHongWan.getId());
        List<String> imgs = Arrays.asList(wangHongWan.getPicList().split(","));
        wangHongWanDTO.setImgs(imgs);
        model.addAttribute("fqTopic", wangHongWanDTO);
        return "/wh/detail";
    }


    /**
     * 查询WangHongWan首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        WangHongWanExample example = new WangHongWanExample();
        example.setOrderByClause("create_time desc");
        List<WangHongWan> list = wangHongWanService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }
}