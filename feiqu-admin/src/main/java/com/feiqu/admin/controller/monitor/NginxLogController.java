package com.feiqu.admin.controller.monitor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.NginxLog;
import com.feiqu.system.model.NginxLogExample;
import com.feiqu.system.service.mainData.NginxLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monitor/nginxLog")
public class NginxLogController extends BaseController {
    private String prefix = "/monitor/nginxLog";
    @Resource
    private NginxLogService nginxLogService;

    @RequiresPermissions("monitor:nginxLog:view")
    @GetMapping()
    public String nginxLog(ModelMap mmap) throws Exception {
        return prefix + "/nginxLog";
    }

    @RequiresPermissions("monitor:nginxLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(NginxLog nginxLog,String beginTime,String endTime) {
        startPage();
        NginxLogExample nginxLogExample = new NginxLogExample();
        NginxLogExample.Criteria criteria = nginxLogExample.createCriteria();
        if (StrUtil.isNotEmpty(nginxLog.getIp())) {
            criteria.andIpLike("%" + nginxLog.getIp() + "%");
        }
        if (StrUtil.isNotEmpty(nginxLog.getUrl())) {
            criteria.andUrlLike("%" + nginxLog.getUrl() + "%");
        }
        if (StrUtil.isNotEmpty(nginxLog.getReferer())) {
            criteria.andRefererLike("%" + nginxLog.getReferer() + "%");
        }
        if (StrUtil.isNotEmpty(beginTime)) {
            criteria.andCreateTimeGreaterThan(DateUtil.parse(beginTime).toJdkDate());
        }
        if (StrUtil.isNotEmpty(endTime)){
            criteria.andCreateTimeLessThan(DateUtil.parse(endTime).toJdkDate());
        }
        if(nginxLog.getSpiderType() != null){
            criteria.andSpiderTypeEqualTo(nginxLog.getSpiderType());
        }
        nginxLogExample.setOrderByClause("id desc");
        List<NginxLog> list = nginxLogService.selectByExample(nginxLogExample);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(nginxLogService.deleteByPrimaryKeys(ids));
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        nginxLogService.deleteByExample(new NginxLogExample());
        return success();
    }
}
