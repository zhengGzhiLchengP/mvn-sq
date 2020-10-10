package com.feiqu.admin.controller.sys;


import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.quartz.model.SysJobLog;
import com.feiqu.quartz.model.SysJobLogExample;
import com.feiqu.quartz.service.SysJobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * SysJobLogcontroller
 * Created by cwd on 2019/3/13.
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysJobLogController.class);

    @Resource
    private SysJobLogService sysJobLogService;

    /**
     * 跳转到SysJobLog首页
     */
    @RequestMapping("")
    public String manage() {
        return "/monitor/sysJobLog/manage";
    }

    /**
     * ajax删除SysJobLog
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            sysJobLogService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * ajax更新SysJobLog
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(SysJobLog sysJobLog) {
        try {
            if (sysJobLog.getJobLogId() == null) {
                sysJobLogService.insert(sysJobLog);
            } else {
                sysJobLogService.updateByPrimaryKeySelective(sysJobLog);
            }
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
        return success();
    }


    /**
     * 查询SysJobLog首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list() {
        try {
            startPage();
            SysJobLogExample example = new SysJobLogExample();
            example.setOrderByClause("job_log_id desc");
            List<SysJobLog> list = sysJobLogService.selectByExample(example);
            return getDataTable(list);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }
}