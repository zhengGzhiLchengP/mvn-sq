package com.feiqu.admin.controller.monitor;

import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.system.model.collectData.LogData;
import com.feiqu.system.model.collectData.LogDataExample;
import com.feiqu.system.service.collectData.LogDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monitor/logData")
public class LogDataController extends BaseController {
    private String prefix = "/monitor/logData";
    @Resource
    private LogDataService logDataService;

    @GetMapping()
    public String index(ModelMap mmap) throws Exception {
        return prefix + "/logData";
    }

    @GetMapping("/detail/{id}")
    public String index(@PathVariable Integer id, Model model) throws Exception {
        LogData logData = logDataService.selectByPrimaryKey(id);
        model.addAttribute("logData",logData);
        return prefix + "/detail";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogData logData) {
        startPage();
        LogDataExample logDataExample = new LogDataExample();
        LogDataExample.Criteria criteria = logDataExample.createCriteria();
        if (StrUtil.isNotEmpty(logData.getGenerateDate())) {
            criteria.andGenerateDateLike("%"+logData.getGenerateDate()+"%");
        }
        if (StrUtil.isNotEmpty(logData.getLogType())) {
            criteria.andLogTypeEqualTo(logData.getLogType());
        }
        logDataExample.setOrderByClause("id desc");
        List<LogData> list = logDataService.selectByExample(logDataExample);
        return getDataTable(list);
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("monitor:logData:remove")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Integer id)
    {
        return toAjax(logDataService.deleteByPrimaryKey(id));
    }

}
