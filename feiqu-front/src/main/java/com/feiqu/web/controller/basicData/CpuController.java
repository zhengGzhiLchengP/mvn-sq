package com.feiqu.web.controller.basicData;


import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.Cpu;
import com.feiqu.system.model.basicData.CpuExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.basicData.CpuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * Cpucontroller
 * Created by cwd on 2020/7/29.
 */
@Controller
@RequestMapping("/cpu")
public class CpuController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CpuController.class);

    @Resource
    private CpuService cpuService;

    /**
     * add
     */
    @RequestMapping("/add")
    public String add(@RequestParam(required = false) Integer cloneId,
                      Model model) {
        if(cloneId != null){
            Cpu cpu = cpuService.selectByPrimaryKey(cloneId);
            model.addAttribute("cpu",cpu);
        }
        return "/diy/cpu/add";
    }
    /**
     * 跳转到Cpu首页
     */
    @RequestMapping("")
    public String index() {
        return "/diy/cpu/index";
    }


    /**
     * ajax删除Cpu
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(fqUserCache.getRole() != UserRoleEnum.ADMIN_USER_ROLE.getValue()){
                return error("你没有权限");
            }
            cpuService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * ajax更新Cpu
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(Cpu cpu) {
        BaseResult result = new BaseResult();
        try {
            logger.info("cpu:{}", JSON.toJSONString(cpu));
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(fqUserCache.getRole() != UserRoleEnum.ADMIN_USER_ROLE.getValue()){
                return error("你没有权限");
            }
            if (cpu.getId() == null) {
                cpuService.insert(cpu);
            } else {
                cpuService.updateByPrimaryKeySelective(cpu);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询Cpu首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       String name) {
        try {
            PageHelper.startPage(page, limit);
            CpuExample example = new CpuExample();
            example.setOrderByClause("id desc");
            if(StringUtils.isNotEmpty(name)){
                example.createCriteria().andNameLike("%"+name+"%");
            }
            List<Cpu> list = cpuService.selectByExample(example);
            if(list == null){
                return success();
            }
            PageInfo pageInfo = new PageInfo(list);
            return success(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            return error("查询失败");
        }
    }
}