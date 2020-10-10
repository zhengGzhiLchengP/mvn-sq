package com.feiqu.web.controller.resource;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqGoodPic;
import com.feiqu.system.model.FqGoodPicExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FqGoodPicDTO;
import com.feiqu.system.service.mainData.FqGoodPicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * FqGoodPiccontroller
 * Created by cwd on 2019/3/10.
 */
@Controller
@RequestMapping("/fqGoodPic")
public class FqGoodPicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqGoodPicController.class);

    @Resource
    private FqGoodPicService fqGoodPicService;

    /**
     * 跳转到FqGoodPic首页
     */
    @RequestMapping("")
    public String index(Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())
                || UserRoleEnum.VIP.getValue().equals(fqUserCache.getRole())
                || UserRoleEnum.ACTIVE_USER.getValue().equals(fqUserCache.getRole())){
            model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.LIST.getPosition()));
            return "/fqGoodPic/index";
        }else {
            model.addAttribute(GENERAL_CUSTOM_ERROR_CODE,"暂时只支持VIP用户访问，你可以尝试联系官方人员申请VIP。");
            return GENERAL_CUSTOM_ERROR_URL;
        }
    }

    /**
     * 添加FqGoodPic页面
     */
    @RequestMapping("/fqGoodPic_add")
    public String fqGoodPic_add() {
        return "/fqGoodPic/add";
    }

    /**
     * ajax删除FqGoodPic
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            fqGoodPicService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 详情页面
     */
    @RequestMapping("/{fqGoodPicId}")
    public Object fqGoodPicEdit(@PathVariable Long fqGoodPicId, Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())
                || UserRoleEnum.VIP.getValue().equals(fqUserCache.getRole())
                || UserRoleEnum.ACTIVE_USER.getValue().equals(fqUserCache.getRole())
        ){
            FqGoodPic fqGoodPic = fqGoodPicService.selectByPrimaryKey(fqGoodPicId);
            FqGoodPicDTO fqGoodPicDTO = new FqGoodPicDTO(fqGoodPic);
            model.addAttribute("goodPic", JSON.toJSON(fqGoodPicDTO));
            model.addAttribute("advertisements", CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.DETAIL.getPosition()));
            return "/fqGoodPic/detail";
        }else {
            model.addAttribute(GENERAL_CUSTOM_ERROR_CODE,"暂时只支持VIP用户访问，你可以尝试联系官方人员申请VIP。");
            return GENERAL_CUSTOM_ERROR_URL;
        }

    }

    /**
     * ajax更新FqGoodPic
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(FqGoodPic fqGoodPic) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (fqGoodPic.getId() == null) {
                fqGoodPicService.insert(fqGoodPic);
            } else {
                fqGoodPicService.updateByPrimaryKeySelective(fqGoodPic);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FqGoodPic首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            FqGoodPicExample example = new FqGoodPicExample();
            example.setOrderByClause("GMT_CREATE desc");
            List<FqGoodPic> list = fqGoodPicService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            if(CollectionUtil.isEmpty(list)){
                page = new PageInfo(Lists.newArrayList());
            }else {
                List<FqGoodPicDTO> fqGoodPicDTOS = list.stream().map(FqGoodPicDTO::new).collect(Collectors.toList());
                page.setList(fqGoodPicDTOS);
            }
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}