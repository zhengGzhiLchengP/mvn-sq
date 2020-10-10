package com.feiqu.web.controller.monitor;

import com.feiqu.framwork.web.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cwd
 * @version BlackListController, v 0.1 2018/12/26 cwd 1049766
 */
@RequestMapping("blackList")
@Controller
public class BlackListController extends BaseController {
//    private final static Logger logger = LoggerFactory.getLogger(BlackListController.class);

    @GetMapping("denyService")
    public String denyService(){
        return "/blackList/denyService";
    }

}
