package com.feiqu.admin.controller.monitor;

import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.sysData.SysOperLog;
import com.feiqu.system.model.sysData.SysOperLogExample;
import com.feiqu.system.service.sysData.SysOperLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    private String prefix = "monitor/operlog";

    @Resource
    private SysOperLogService operLogService;

    @RequiresPermissions("monitor:operlog:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/operlog";
    }

    @RequiresPermissions("monitor:operlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        SysOperLogExample example = new SysOperLogExample();
        example.setOrderByClause("oper_id desc");
        List<SysOperLog> list = operLogService.selectByExample(example);
        return getDataTable(list);
    }

    /*@Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:operlog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOperLog operLog)
    {
        SysOperLogExample example = new SysOperLogExample();
        List<SysOperLog> list = operLogService.selectByExample(example);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }*/

    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(operLogService.deleteByPrimaryKeys(ids));
    }

    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping("/detail/{operId}")
    public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
        mmap.put("operLog", operLogService.selectByPrimaryKey(operId));
        return prefix + "/detail";
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return success();
    }
}
