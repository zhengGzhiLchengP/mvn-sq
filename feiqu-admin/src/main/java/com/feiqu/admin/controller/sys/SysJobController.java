package com.feiqu.admin.controller.sys;


import cn.hutool.cron.pattern.CronPatternUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import com.feiqu.quartz.service.SysJobService;
import com.feiqu.quartz.util.ScheduleUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * SysJobcontroller
 * Created by cwd on 2019/3/13.
 */
@Controller
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysJobController.class);

    @Resource
    private Scheduler scheduler;

    @Resource
    private SysJobService sysJobService;

    /**
     * 跳转到SysJob首页
     */
    @RequestMapping("")
    public String manage() {
        return "/monitor/sysJob/manage";
    }


    /**
     * 任务调度立即执行一次
     */
    @PostMapping("/run")
    @ResponseBody
    public AjaxResult run(SysJob job) throws SchedulerException {
        sysJobService.run(job);
        return success();
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(String ids, String status) {
        try {
            logger.info("ids:{},status:{}", ids, status);
            sysJobService.changeStatus(ids, status);
        } catch (SchedulerException e) {
            logger.error("changeStatus", e);
            return error();
        }
        return success();
    }

    @PostMapping("/remove")
    @ResponseBody
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    public AjaxResult remove(String ids) throws SchedulerException {
        sysJobService.deleteJobByIds(ids);
        return success();
    }

    @ResponseBody
    @PostMapping("/matchedDates")
    public Object matchedDates(String cronExpression) {
        try {
            List<Date> dates = CronPatternUtil.matchedDates(cronExpression, new Date(), 3, false);
            return success(dates);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * ajax更新SysJob
     */
    @ResponseBody
    @PostMapping("/save")
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    public Object save(SysJob sysJob) {
        try {
            boolean ok = checkCronExpressionIsValid(sysJob.getCronExpression());
            if (!ok) {
                return error("定时任务表达式不对");
            }
            if (sysJob.getJobId() == null) {
                sysJob.setStatus("0");
                sysJob.setCreateBy("");
                sysJob.setUpdateBy("");
                sysJob.setCreateTime(new Date());
                sysJob.setUpdateTime(new Date());
                sysJob.setMethodParams(StringUtils.trimToEmpty(sysJob.getMethodParams()));
                sysJob.setRemark(StringUtils.trimToEmpty(sysJob.getRemark()));
                sysJobService.insert(sysJob);
                ScheduleUtils.createScheduleJob(scheduler, sysJob);
            } else {
                sysJobService.updateJob(sysJob);
            }
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
        return success();
    }


    /**
     * 查询SysJob首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, size);
            SysJobExample example = new SysJobExample();
            example.setOrderByClause("create_time desc");
            List<SysJob> list = sysJobService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    private boolean checkCronExpressionIsValid(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (ParseException var2) {
            logger.error("正则表达式有问题:"+cronExpression,var2);
            return false;
        }
    }
}