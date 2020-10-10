package com.feiqu.admin.controller.front;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.mainData.FqMenu;
import com.feiqu.system.model.mainData.FqMenuExample;
import com.feiqu.system.service.mainData.FqMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front/menu")
public class FqMenuController extends BaseController {
    private String prefix = "front/menu";
    private static Log logger = LogFactory.get();
    @Resource
    private FqMenuService fqMenuService;

    @RequestMapping("")
    public String manage() {
        return prefix + "/menu";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(Integer page, String menuName) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, BizConstant.DEAULT_PAGE_SIZE);
            FqMenuExample example = new FqMenuExample();
            example.setOrderByClause("id desc");
            FqMenuExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(menuName)) {
                criteria.andMenuNameLike("%"+menuName+"%");
            }
            List<FqMenu> users = fqMenuService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(users);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("用户分页出错", e);
            return error("用户分页出错");
        }
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Object changeStatus(FqMenu fqMenu) {
//            FqMenu fqMenuDB = fqMenuService.selectByPrimaryKey(fqMenu.getId());
        if(fqMenu.getId() == null){
            String name = ShiroUtils.getLoginName();
            fqMenu.setCreateBy(name);
            fqMenu.setUpdateBy(name);
            fqMenu.setGmtCreate(new Date());
            fqMenu.setDelFlag(YesNoEnum.NO.getValue());
            fqMenu.setGmtUpdate(new Date());
            fqMenuService.insert(fqMenu);
        }else {
            fqMenu.setGmtUpdate(new Date());
            fqMenuService.updateByPrimaryKeySelective(fqMenu);
        }

        return success();
    }
}
