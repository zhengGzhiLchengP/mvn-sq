package com.feiqu.admin.controller.front;

import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.Thought;
import com.feiqu.system.model.ThoughtExample;
import com.feiqu.system.service.mainData.ThoughtService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front/thought")
public class ThoughtController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(ThoughtController.class);
    @Resource
    private ThoughtService thoughtService;

    @GetMapping("")
    public String manage() {
        return "/front/thought/index";
    }

    @GetMapping("/list")
    @ResponseBody
    public Object list(Integer page, Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            ThoughtExample example = new ThoughtExample();
            example.setOrderByClause("id desc");
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<Thought> messages = thoughtService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(messages);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("分页出错", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        BaseResult result = new BaseResult();
        try {
            Thought thought = new Thought();
            thought.setId(id);
            thought.setDelFlag(YesNoEnum.YES.getValue());
            thoughtService.updateByPrimaryKeySelective(thought);
        } catch (Exception e) {
            logger.error("想法删除出错", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }
}
