package com.feiqu.web.controller.basicData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.WsLog;
import com.feiqu.system.model.basicData.WsLogExample;
import com.feiqu.system.service.basicData.WsLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


/**
 * WsLogcontroller
 * Created by cwd on 2019/6/24.
 */
@Controller
@RequestMapping("/wsLog")
public class WsLogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(WsLogController.class);

    @Resource
    private WsLogService wsLogService;

    /**
     * 查询WsLog首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "20") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, size);
            WsLogExample example = new WsLogExample();
            example.setOrderByClause("id desc");
            List<WsLog> list = wsLogService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}