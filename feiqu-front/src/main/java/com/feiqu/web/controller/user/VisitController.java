package com.feiqu.web.controller.user;

import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqVisitRecordExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FqVisitRecordResponse;
import com.feiqu.system.service.mainData.FqVisitRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */
@Controller
@RequestMapping("visit")
public class VisitController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(VisitController.class);
    @Resource
    private FqVisitRecordService visitRecordService;
    @ResponseBody
    @GetMapping("/records/{page}")
    public Object visitRecords(@PathVariable Integer page){
        BaseResult result = new BaseResult();
        FqUserCache fqUser = getCurrentUser();
        if(fqUser == null){
            return result;
        }
        FqVisitRecordExample example = new FqVisitRecordExample();
        example.createCriteria().andVisitedUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        example.setOrderByClause("visit_time desc");
        PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE);
        List<FqVisitRecordResponse> list = visitRecordService.selectVisitsByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        result.setData(pageInfo);
        return result;
    }

    @ResponseBody
    @PostMapping("/count")
    public Object count() {
        FqUserCache fqUser = getCurrentUser();
        if (fqUser == null) {
            return notLoginError();
        }
        FqVisitRecordExample example = new FqVisitRecordExample();
        example.createCriteria().andVisitedUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        int count = visitRecordService.countByExample(example);
        return success(count);
    }
}
