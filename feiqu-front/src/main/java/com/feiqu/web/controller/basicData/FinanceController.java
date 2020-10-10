package com.feiqu.web.controller.basicData;

import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.FinanceKnowledge;
import com.feiqu.system.model.basicData.FinanceKnowledgeExample;
import com.feiqu.system.model.collectData.StockHl;
import com.feiqu.system.model.collectData.StockHlExample;
import com.feiqu.system.service.basicData.FinanceKnowledgeService;
import com.feiqu.system.service.collectData.StockHlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("finance")
public class FinanceController extends BaseController {

    private String prefix = "/finance";
    @Resource
    private FinanceKnowledgeService financeKnowledgeService;
    @Resource
    private StockHlService stockHlService;

    @GetMapping("")
    public String index(Model model) {

        StockHlExample stockHlExample = new StockHlExample();
        stockHlExample.setOrderByClause("id desc");
        StockHl stockHl = stockHlService.selectFirstByExample(stockHlExample);
        model.addAttribute("stockHl",stockHl);

        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        FinanceKnowledgeExample financeKnowledgeExample = new FinanceKnowledgeExample();
        financeKnowledgeExample.setOrderByClause("rand()");
        List<FinanceKnowledge> financeKnowledgeList = financeKnowledgeService.selectByExample(financeKnowledgeExample);
        model.addAttribute("list",financeKnowledgeList);
        return prefix + "/index";
    }

    /**
     * 股票
     *
     * @return
     */
    @GetMapping("stock")
    public String stock() {
        return prefix + "/stock";
    }

    @GetMapping("corporation")
    public String corporation() {
        return prefix + "/corporation";
    }

    /**
     * 金融知识学习
     *
     * @return
     */
    @GetMapping("knowledge")
    public String knowledge(Model model, @RequestParam(defaultValue = "1") Integer pageNum, String keyword) {
        PageHelper.startPage(pageNum, CommonConstant.DEAULT_PAGE_SIZE);
        FinanceKnowledgeExample financeKnowledgeExample = new FinanceKnowledgeExample();
        financeKnowledgeExample.setOrderByClause("id desc");
        if (StringUtils.isNotEmpty(keyword)) {
            financeKnowledgeExample.createCriteria().andTagsLike("%" + keyword + "%");
            model.addAttribute("keyword",keyword);
        }
        List<FinanceKnowledge> financeKnowledgeList = financeKnowledgeService.selectByExample(financeKnowledgeExample);
        PageInfo pageInfo = new PageInfo(financeKnowledgeList);
        model.addAttribute("pageInfo", pageInfo);
        return prefix + "/knowledge";
    }
}
