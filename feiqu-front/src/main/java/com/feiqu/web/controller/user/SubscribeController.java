package com.feiqu.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.feiqu.common.enums.SubscribeTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.mainData.FqMenu;
import com.feiqu.system.model.mainData.FqMenuExample;
import com.feiqu.system.model.mainData.Subscribe;
import com.feiqu.system.model.mainData.SubscribeExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqMenuService;
import com.feiqu.system.service.mainData.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("subscribe")
public class SubscribeController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(SubscribeController.class);

    @Resource
    private FqMenuService fqMenuService;
    @Resource
    private SubscribeService subscribeService;

    @GetMapping("")
    public String index(Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        if (fqUserCache == null) {
            return loginRedirect("/subscribe");
        }
        FqMenuExample fqMenuExample = new FqMenuExample();


        SubscribeExample subscribeExample = new SubscribeExample();
        subscribeExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andUserIdEqualTo(fqUserCache.getId());
        List<Subscribe> subscribes = subscribeService.selectByExample(subscribeExample);
        List<Integer> ids = subscribes.stream().map(Subscribe::getSubscribeId).collect(Collectors.toList());
        model.addAttribute("sublist", JSON.toJSON(ids));
        fqMenuExample.clear();
        fqMenuExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        ;
        List<FqMenu> fqMenus = fqMenuService.selectByExample(fqMenuExample);
        List<cn.hutool.json.JSONObject> list = new ArrayList<>();
        fqMenus.forEach(fqMenu -> {
            cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
            jsonObject.put("value", fqMenu.getId());
            jsonObject.put("title", fqMenu.getMenuName());
            jsonObject.put("url", fqMenu.getUrl());
            list.add(jsonObject);
        });
        model.addAttribute("fqMenus", JSON.toJSON(list));
        return "/front/subscribe/subscribe";
    }

    @PostMapping("subscribeMenu")
    @ResponseBody
    public Object subscribeMenu(Subscribe subscribe, @RequestParam("menuIds[]") List<Integer> ids) {
        FqUserCache fqUserCache = getCurrentUser();
        if (fqUserCache == null) {
            return error();
        }
        SubscribeExample subscribeExample = new SubscribeExample();
        subscribeExample.createCriteria().andUserIdEqualTo(fqUserCache.getId());
        Subscribe forUpdate = new Subscribe();
        forUpdate.setDelFlag(YesNoEnum.YES.getValue());
        forUpdate.setGmtUpdate(new Date());
        subscribeService.updateByExampleSelective(forUpdate, subscribeExample);

        subscribe.setSubscribeType(SubscribeTypeEnum.USER_MENU.getValue());
        subscribe.setDelFlag(YesNoEnum.NO.getValue());
        subscribe.setUserId(fqUserCache.getId());
        subscribe.setGmtCreate(new Date());
        subscribe.setGmtUpdate(new Date());
        ids.forEach(id -> {
            subscribe.setSubscribeId(id);
            subscribeService.insert(subscribe);
        });
        return success();
    }


}
