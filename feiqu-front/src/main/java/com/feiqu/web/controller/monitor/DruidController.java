package com.feiqu.web.controller.monitor;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.pojo.cache.FqUserCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * druid 监控
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/druidData")
public class DruidController extends BaseController
{

    @GetMapping()
    public String index()
    {
        return redirect("/druid/index");
    }

    @ResponseBody
    @GetMapping("/stat")
    public Object druidStat(){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null || !UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())){
            return ResultEnum.USER_NOT_AUTHORIZED;
        }
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
