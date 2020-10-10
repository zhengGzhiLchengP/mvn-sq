package com.feiqu.web.controller.support;


import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqAdvertisement;
import com.feiqu.system.model.FqAdvertisementExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqAdvertisementService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * FqAdvertisementcontroller
 * Created by cwd on 2019/2/21.
 */
@Controller
@RequestMapping("/fqAdvertisement")
public class FqAdvertisementController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqAdvertisementController.class);

    @Resource
    private FqAdvertisementService fqAdvertisementService;

    /**
     * 跳转到FqAdvertisement首页
     */
    @RequestMapping("")
    public String index() {
        return "/fqAdvertisement/index";
    }

    @GetMapping("/manage")
    public String manage(Model model){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(fqUserCache.getRole() != 1){
            model.addAttribute("errorMsg","用户权限不足！");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/fqAdvertisement/manage";
    }

    /**
     * 添加FqAdvertisement页面
     */
    @RequestMapping("/update")
    @ResponseBody
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    public Object fqAdvertisement_add() {
        FqAdvertisementExample fqAdvertisementExample = new FqAdvertisementExample();
        fqAdvertisementExample.setOrderByClause("GMT_CREATE DESC");
        List<FqAdvertisement> advertisements = fqAdvertisementService.selectByExample(fqAdvertisementExample);
        if (CollectionUtils.isNotEmpty(advertisements)) {
            Map<Integer, List<FqAdvertisement>> map = advertisements.stream().collect(Collectors.groupingBy(FqAdvertisement::getPosition));
            CommonConstant.FQ_ADVERTISEMENTS = map;
        }
        return success();
    }

}