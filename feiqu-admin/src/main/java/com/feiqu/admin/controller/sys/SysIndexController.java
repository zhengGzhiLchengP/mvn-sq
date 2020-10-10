package com.feiqu.admin.controller.sys;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.feiqu.adminFramework.constant.AdminConstants;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.config.Global;
import com.feiqu.system.model.sysData.SysMenu;
import com.feiqu.system.model.sysData.SysUser;
import com.feiqu.system.service.sysData.SysMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Resource
    private SysMenuService menuService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", Global.getVersion());
        return "main";
    }

    /**
     *
     * @param request
     * @param response
     * @param type 1 pc 2 h5
     */
    @GetMapping(value = "/captcha")
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1",required = false) Integer type) {
        try {
            LineCaptcha lineCaptcha = null;
            lineCaptcha = CaptchaUtil.createLineCaptcha(200,50);
            lineCaptcha.createCode();
            String code = lineCaptcha.getCode();
            request.getSession().setAttribute(AdminConstants.KAPTCHA_SESSION_KEY,code);
            ImageIO.write(lineCaptcha.getImage(), "JPEG", response.getOutputStream());
        } catch (Exception e) {
            logger.error("获取验证码出错",e);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(SysIndexController.class);
}
