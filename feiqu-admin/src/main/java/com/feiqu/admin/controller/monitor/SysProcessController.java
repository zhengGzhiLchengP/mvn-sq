package com.feiqu.admin.controller.monitor;

import com.alibaba.fastjson.JSONObject;
import com.feiqu.adminFramework.commander.AbstractSystemCommander;
import com.feiqu.adminFramework.manager.TopManager;
import com.feiqu.adminFramework.model.system.ProcessModel;
import com.feiqu.adminFramework.web.base.BaseController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping("/monitor/sysProcess")
public class SysProcessController extends BaseController {

    @GetMapping()
    public String index(){

        return "monitor/sysProcess/index";
    }

    @RequestMapping(value = "getTop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getTop() {
        JSONObject topInfo = TopManager.getTopMonitor();
        return success(topInfo);
    }

    @RequestMapping(value = "processList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getProcessList() {
        List<ProcessModel> array = AbstractSystemCommander.getInstance().getProcessList();
        if (array != null && !array.isEmpty()) {
            return success(array);
        }
        return error("没有获取到进程信息");
    }
}
