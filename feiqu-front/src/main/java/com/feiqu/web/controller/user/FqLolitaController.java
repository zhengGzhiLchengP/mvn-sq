package com.feiqu.web.controller.user;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.base.FqResult;
import com.feiqu.common.enums.ActiveNumEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqLolita;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.GeneralLike;
import com.feiqu.system.model.GeneralLikeExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqLolitaDTO;
import com.feiqu.system.service.mainData.FqLolitaService;
import com.feiqu.system.service.mainData.FqUserService;
import com.feiqu.system.service.mainData.GeneralLikeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FqLolitacontroller
 * Created by cwd on 2019/2/19.
 */
@Controller
@RequestMapping("/fqLolita")
public class FqLolitaController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqLolitaController.class);

    @Resource
    private FqLolitaService fqLolitaService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private GeneralLikeService likeService;

    /**
     * 跳转到FqLolita首页
     */
    @RequestMapping("")
    public String index() {
        return "/fqLolita/index";
    }

    /**
     * 添加FqLolita页面
     */
    @RequestMapping("/fqLolita_add")
    public String fqLolita_add() {
        return "/fqLolita/add";
    }

    /**
     * ajax删除FqLolita
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            fqLolitaService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新FqLolita页面
     */
    @RequestMapping("/edit/{fqLolitaId}")
    public Object fqLolitaEdit(@PathVariable Long fqLolitaId, Model model) {
        FqLolita fqLolita = fqLolitaService.selectByPrimaryKey(fqLolitaId);
        model.addAttribute("fqLolita", fqLolita);
        return "/fqLolita/edit";
    }

    /**
     * ajax更新FqLolita
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(@RequestBody FqLolitaDTO fqLolitaDTO) {
        FqResult result = new FqResult();
        try {
            Integer userId = CommonUtils.getUIdFromToken(fqLolitaDTO.getToken());
            if (userId == null || userId <= 0) {
                return result.fail("用户未登录");
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            if (fqUser == null) {
                return result.fail("用户未查到");
            }
            FqLolita fqLolitaDO = new FqLolita();
            fqLolitaDO.setPicUrl(fqLolitaDTO.getPicUrl());
            fqLolitaDO.setRemark(fqLolitaDTO.getRemark());
            fqLolitaDO.setUserId(userId);
            fqLolitaDO.setId(fqLolitaDTO.getId());
            if (fqLolitaDO.getId() == null) {
                fqLolitaDO.setGmtCreate(new Date());
                fqLolitaDO.setDelFlag(YesNoEnum.NO.getValue());
                fqLolitaDO.setCommentCount(0);
                fqLolitaDO.setLikeCount(0);
                fqLolitaDO.setLink("");
                fqLolitaService.insert(fqLolitaDO);
            } else {
                fqLolitaService.updateByPrimaryKeySelective(fqLolitaDO);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(@RequestBody FqLolitaDTO fqLolitaDTO){
        FqResult result = new FqResult();
        try {
            Integer userId = CommonUtils.getUIdFromToken(fqLolitaDTO.getToken());
            if (userId == null || userId <= 0) {
                return result.fail("用户未登录");
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            if (fqUser == null) {
                return result.fail("用户未查到");
            }
            FqLolita fqLolita = fqLolitaService.selectByPrimaryKey(fqLolitaDTO.getId());
            if(fqLolita == null){
                result.fail("参数不能为空");
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(userId)
                    .andTopicIdEqualTo(fqLolitaDTO.getId().intValue()).andTopicTypeEqualTo(TopicTypeEnum.LOLITA_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.fail("已经点过赞了");
                return result;
            }
            like = new GeneralLike(fqLolitaDTO.getId().intValue(), TopicTypeEnum.LOLITA_TYPE.getValue(),1,userId,new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);
            fqLolita.setLikeCount(fqLolita.getLikeCount() == null? 1: fqLolita.getLikeCount()+1);
            fqLolitaService.updateByPrimaryKey(fqLolita);
            result.setData(fqLolita.getLikeCount());
            CommonUtils.addActiveNum(userId, ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            logger.error("thought like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }


    /**
     * 查询FqLolita首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            CommonCondition commonCondition = new CommonCondition();
            commonCondition.setOrderByClause("GMT_CREATE desc");
            List<FqLolitaDTO> list = fqLolitaService.selectWithUser(commonCondition);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}