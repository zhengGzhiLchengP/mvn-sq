package com.feiqu.web.controller.basicData;


import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.TaobaoProduct;
import com.feiqu.system.model.basicData.TaobaoProductExample;
import com.feiqu.system.service.basicData.TaobaoProductService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;


/**
 * TaobaoProductcontroller
 * Created by cwd on 2019/7/30.
 */
@Controller
@RequestMapping("/taobaoProduct")
public class TaobaoProductController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TaobaoProductController.class);

    @Resource
    private TaobaoProductService taobaoProductService;

    /**
     * 跳转到TaobaoProduct首页
     */
    @RequestMapping("")
    public String index(Model model, String keyword) {
        startPage();
        TaobaoProductExample taobaoProductExample = new TaobaoProductExample();
        if (StringUtils.isNotEmpty(keyword)) {
            logger.info(keyword);
            taobaoProductExample.createCriteria().andProductDescLike("%" + keyword + "%");
        }
        taobaoProductExample.setOrderByClause("id desc");
        List<TaobaoProduct> taobaoProducts = taobaoProductService.selectByExample(taobaoProductExample);
        model.addAttribute("pageInfo", new PageInfo(taobaoProducts));
        return "/front/taobaoProduct/index";
    }

}