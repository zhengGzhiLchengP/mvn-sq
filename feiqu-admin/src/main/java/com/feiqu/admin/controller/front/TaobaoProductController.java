package com.feiqu.admin.controller.front;


import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.system.model.basicData.TaobaoProduct;
import com.feiqu.system.model.basicData.TaobaoProductExample;
import com.feiqu.system.service.basicData.TaobaoProductService;
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
 * TaobaoProductcontroller
 * Created by cwd on 2019/7/30.
 */
@Controller
@RequestMapping("/front/taobaoProduct")
public class TaobaoProductController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TaobaoProductController.class);

    @Resource
    private TaobaoProductService taobaoProductService;

    /**
     * 跳转到TaobaoProduct首页
     */
    @RequestMapping("")
    public String index() {
        return "/front/taobaoProduct/manage";
    }

    /**
     * ajax删除TaobaoProduct
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            taobaoProductService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * ajax更新TaobaoProduct
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(TaobaoProduct taobaoProduct) {
        BaseResult result = new BaseResult();
        try {
            if (taobaoProduct.getId() == null) {
                taobaoProduct.setCreateTime(new Date());
                taobaoProductService.insert(taobaoProduct);
            } else {
                taobaoProductService.updateByPrimaryKeySelective(taobaoProduct);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询TaobaoProduct首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            TaobaoProductExample example = new TaobaoProductExample();
            example.setOrderByClause("id desc");
            List<TaobaoProduct> list = taobaoProductService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}