package com.feiqu.admin.controller.front;

import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.SuperBeautyExample;
import com.feiqu.system.service.mainData.SuperBeautyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cwd
 * @create 2017-10-9:30
 **/
@Controller
@RequestMapping("beauty")
public class BeautyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BeautyController.class);

    @Resource
    private SuperBeautyService superBeautyService;

    private String prefix = "front/beauty";

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        BaseResult result = new BaseResult();
        try {
            SuperBeauty superBeauty = superBeautyService.selectByPrimaryKey(id);
            Assert.notNull(superBeauty, "图片不能为空！");
            SuperBeauty superBeautyToUpdate = new SuperBeauty();
            superBeautyToUpdate.setId(id);
            superBeautyToUpdate.setDelFlag(YesNoEnum.YES.getValue());
            superBeautyService.updateByPrimaryKeySelective(superBeautyToUpdate);
        } catch (Exception e) {
            logger.error("删除图片出错", e);
            result.setCode("1");
            result.setMessage("删除图片出错");
        }
        return result;
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        return prefix + "/manage";
    }


    @GetMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page, Integer limit, SuperBeauty superBeauty) {
        BaseResult result = new BaseResult();
        try {
            if (limit > 20) {
                limit = 20;
            }
            PageHelper.startPage(page, limit);
            SuperBeautyExample example = new SuperBeautyExample();
            example.setOrderByClause("id desc");
            SuperBeautyExample.Criteria criteria = example.createCriteria();
            if (superBeauty.getId() != null) {
                criteria.andIdEqualTo(superBeauty.getId());
            }
            if (StringUtils.isNotEmpty(superBeauty.getTitle())) {
                criteria.andTitleLike("%" + superBeauty.getTitle() + "%");
            }
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<SuperBeauty> beautyList = superBeautyService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(beautyList);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("图片分页出错", e);
            result.setCode("1");
            result.setMessage("图片分页出错");
        }
        return result;
    }
}
