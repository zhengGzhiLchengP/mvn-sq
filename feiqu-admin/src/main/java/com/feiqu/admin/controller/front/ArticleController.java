package com.feiqu.admin.controller.front;

import com.feiqu.adminFramework.util.CommonUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.ArticleExample;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.service.mainData.ArticleService;
import com.feiqu.system.service.mainData.CMessageService;
import com.feiqu.system.service.mainData.FqUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/15.
 */
@Controller
@RequestMapping("article")
public class ArticleController extends BaseController {
    private String prefix = "front/article";
    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    @Resource
    private ArticleService articleService;
    @Resource
    private CMessageService messageService;
    @Resource
    private FqUserService fqUserService;
    @RequestMapping("")
    public String manage() {
        return prefix + "/index";
    }

    @GetMapping("/list")
    @ResponseBody
    public Object list() {
        try {
            startPage();
            ArticleExample example = new ArticleExample();
            example.setOrderByClause("create_time desc");
            List<Article> articles = articleService.selectByExample(example);
            return getDataTable(articles);
        } catch (Exception e) {
            LOGGER.error("文章分页出错", e);
            return error("文章分页出错");
        }
    }

    @RequestMapping("/examine")
    @ResponseBody
    public Object examine(Integer articleId) {
        try {
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                return error("不能为空");
            }
            if (article.getDelFlag().equals(YesNoEnum.YES.getValue())) {
                return error("文章已经被删除");
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(article.getUserId());
            if (fqUser == null) {
                return error("不能为空");
            }
            article.setDelFlag(YesNoEnum.NO.getValue());
            articleService.updateByPrimaryKeySelective(article);
            LOGGER.info("审核文章，当前用户：{}，被审核用户：{}，状态：{}", 0, article.getUserId(), 0);
//            CommonUtils.addActiveNum(article.getUserId(), ActiveNumEnum.POST_ARTICLE.getValue());
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(new Date());
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(article.getUserId());
            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
            message.setContent("您发表的<a class=\"c-fly-link\" href=\"" + BizConstant.FRONT_DOMAIN_URL + "/article/" + article.getId() + "\" target=\"_blank\">文章</a>已经审核通过！您将获得20趣豆 ");
            messageService.insert(message);
            CommonUtils.addOrDelUserQudouNum(fqUser, 20);
        } catch (Exception e) {
            LOGGER.error("文章审核出错", e);
            return error("文章审核出错");
        }
        return success();
    }

    @PostMapping("/recommend/{articleId}")
    @ResponseBody
    public Object recommend(@PathVariable Integer articleId,Integer type) {
        BaseResult result = new BaseResult();
        try {
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(1== type){
                if (YesNoEnum.YES.getValue().equals(article.getIsRecommend())) {
                    return result;
                }
                Article toUpdate = new Article();
                toUpdate.setId(article.getId());
                toUpdate.setIsRecommend(YesNoEnum.YES.getValue());
                articleService.updateByPrimaryKeySelective(toUpdate);
                CommonUtils.sendOfficialMsg(article.getUserId(),new Date(),
                        "系统消息通知：恭喜您，您发表的<a class=\"c-fly-link\" href=\"" + BizConstant.FRONT_DOMAIN_URL
                                + "/article/" + article.getId() + "\" target=\"_blank\">"+article.getArticleTitle()+"</a>被推荐！");
            }else {
                if (YesNoEnum.NO.getValue().equals(article.getIsRecommend())) {
                    return result;
                }
                Article toUpdate = new Article();
                toUpdate.setId(article.getId());
                toUpdate.setIsRecommend(YesNoEnum.NO.getValue());
                articleService.updateByPrimaryKeySelective(toUpdate);
            }
        } catch (Exception e) {
            LOGGER.error("文章推荐出错", e);
            result.setCode("1");
            result.setMessage("文章推荐出错");
        }
        return result;
    }

    @PostMapping("delete")
    @ResponseBody
    @Log(title = "删除文章", businessType = BusinessType.DELETE)
    public Object delete(Integer articleId) {
        try {
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article != null) {
                article.setDelFlag(YesNoEnum.YES.getValue());
                articleService.updateByPrimaryKeySelective(article);
//                    CommonUtils.addActiveNum(article.getUserId(), -ActiveNumEnum.POST_ARTICLE.getValue());

            } else {
                return error("文章为空！");
            }
        } catch (Exception e) {
            LOGGER.error("article comment delete error", e);
            return error();
        }
        return success();
    }
}
