package com.feiqu.admin.controller.front;


import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.system.model.FqAdvertisement;
import com.feiqu.system.model.FqAdvertisementExample;
import com.feiqu.system.service.mainData.FqAdvertisementService;
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
 * FqAdvertisementcontroller
 * Created by cwd on 2019/2/21.
 */
@Controller
@RequestMapping("/front/advertisement")
public class FqAdvertisementController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqAdvertisementController.class);

    @Resource
    private FqAdvertisementService fqAdvertisementService;


    @GetMapping("")
    public String manage() {
        return "/front/fqAdvertisement/manage";
    }


    /**
     * ajax删除FqAdvertisement
     */
    @ResponseBody
    @RequestMapping("/delete")
    @Log(title = "删除广告", businessType = BusinessType.DELETE)
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            fqAdvertisementService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * ajax更新FqAdvertisement
     */
    @ResponseBody
    @PostMapping("/save")
    @Log(title = "保存广告", businessType = BusinessType.INSERT)
    public Object save(FqAdvertisement fqAdvertisement) {
        BaseResult result = new BaseResult();
        try {
            if (fqAdvertisement.getId() == null) {
                fqAdvertisement.setGmtCreate(new Date());
                fqAdvertisementService.insert(fqAdvertisement);
            } else {
                fqAdvertisementService.updateByPrimaryKeySelective(fqAdvertisement);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }


    /**
     * 查询FqAdvertisement首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,FqAdvertisement advertisement) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            FqAdvertisementExample example = new FqAdvertisementExample();
            example.setOrderByClause("id desc");
            List<FqAdvertisement> list = fqAdvertisementService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}