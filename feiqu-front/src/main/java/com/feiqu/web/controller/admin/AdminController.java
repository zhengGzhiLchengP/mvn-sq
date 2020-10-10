package com.feiqu.web.controller.admin;

import com.feiqu.common.enums.UserRoleEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.pojo.cache.FqUserCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("admin")
@Controller
public class AdminController extends BaseController {

    @GetMapping("index")
    public String index(){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null || !UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())){
            return USER_LOGIN_REDIRECT_URL;
        }
        return "/manage/index";
    }

    @GetMapping("main")
    public String main(){
        return "/manage/main";
    }
}
