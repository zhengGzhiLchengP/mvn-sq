package com.feiqu.admin.controller.monitor;

import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.system.model.collectData.RunLogMessage;
import com.feiqu.system.model.collectData.RunLogMessageExample;
import com.feiqu.system.service.collectData.RunLogMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monitor/runLog")
public class RunLogDataController extends BaseController {
    private String prefix = "/monitor/runLog";
    @Resource
    private RunLogMessageService runLogMessageService;

    @GetMapping()
    @RequiresPermissions("monitor:runLogData:view")
    public String index(ModelMap mmap) throws Exception {
        return prefix + "/runLog";
    }


    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RunLogMessage runLogMessage) {
        startPage();
        RunLogMessageExample logDataExample = new RunLogMessageExample();
        RunLogMessageExample.Criteria criteria = logDataExample.createCriteria();
        if (StrUtil.isNotEmpty(runLogMessage.getLogLevel())) {
            criteria.andLogLevelEqualTo(runLogMessage.getLogLevel());
        }
        if (StrUtil.isNotEmpty(runLogMessage.getAppName())) {
            criteria.andAppNameEqualTo(runLogMessage.getAppName());
        }
        if (StrUtil.isNotEmpty(runLogMessage.getClassName())) {
            criteria.andClassNameEqualTo(runLogMessage.getClassName());
        }
        if (StrUtil.isNotEmpty(runLogMessage.getServerName())) {
            criteria.andServerNameEqualTo(runLogMessage.getServerName());
        }
        if (StrUtil.isNotEmpty(runLogMessage.getDateTime())) {
            String[] strings = StrUtil.split(runLogMessage.getDateTime()," - ");
            criteria.andDateTimeGreaterThan(strings[0].trim());
            criteria.andDateTimeLessThan(strings[1].trim());
        }
        logDataExample.setOrderByClause("id desc");
        List<RunLogMessage> list = runLogMessageService.selectByExampleWithBLOBs(logDataExample);
        return getDataTable(list);
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("monitor:runLogData:remove")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Integer id)
    {
        return toAjax(runLogMessageService.deleteByPrimaryKey(id));
    }

}
