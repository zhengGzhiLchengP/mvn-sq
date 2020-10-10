package com.feiqu.admin.controller.front;

import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.system.model.FqTopic;
import com.feiqu.system.model.FqTopicExample;
import com.feiqu.system.service.mainData.FqTopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqTopiccontroller
 * Created by cwd on 2018/11/29.
 */
@Controller
@RequestMapping("/front/fqTopic")
public class FqTopicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqTopicController.class);
    private String prefix = "front/fqTopic";
    @Resource
    private FqTopicService fqTopicService;


    @ResponseBody
    @PostMapping("/save")
    public Object save(FqTopic fqTopic) {
        BaseResult result = new BaseResult();
        try {
            if (fqTopic.getId() == null) {
                fqTopic.setGmtCreate(new Date());
                fqTopicService.insert(fqTopic);
            } else {
                fqTopicService.updateByPrimaryKeySelective(fqTopic);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 跳转到FqTopic首页
     */
    @RequestMapping("")
    public String index() {
        return prefix + "/index";
    }

    /**
     * ajax删除FqTopic
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        fqTopicService.deleteData(id);
        return result;
    }

    /**
     * 查询FqTopic首页
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer size, String title) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(page, size);
        FqTopicExample example = new FqTopicExample();
        example.setOrderByClause("id desc");
        if (StrUtil.isNotEmpty(title)) {
            example.createCriteria().andTitleLike("%" + title + "%");
        }
        List<FqTopic> list = fqTopicService.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        result.setData(pageInfo);
        return result;
    }
}