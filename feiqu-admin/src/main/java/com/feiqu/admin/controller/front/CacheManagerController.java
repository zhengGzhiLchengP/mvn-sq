package com.feiqu.admin.controller.front;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.feiqu.adminFramework.util.CommonUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.system.pojo.response.IpVisitDTO;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RequestMapping("/front/cache")
@Controller
public class CacheManagerController extends BaseController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final static Logger logger = LoggerFactory.getLogger(CacheManagerController.class);

    @GetMapping("")
    public String index(Model model, @RequestParam(required = false) String queryDate) {
        Set<String> keys = stringRedisTemplate.keys("*");
        model.addAttribute("keys", keys);

        Set<String> ips = stringRedisTemplate.opsForSet().members(BizConstant.FQ_BLACK_LIST_REDIS_KEY);
        model.addAttribute("ips", ips);
        Set<ZSetOperations.TypedTuple<String>> tuples = Sets.newHashSet();
        String key = "";
        if (StringUtils.isNotEmpty(queryDate)) {
            key = "fq_ip_data_" + queryDate;
            tuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 100);
        } else {
            key = BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX + DateUtil.formatDate(new Date());
            tuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 100);
        }
        List<IpVisitDTO> keyValueList = Lists.newArrayList();
        tuples.forEach(tuple -> keyValueList.add(new IpVisitDTO((String) tuple.getValue(), tuple.getScore() + "", CommonUtils.getFullRegionByIp(tuple.getValue()))));
        model.addAttribute("visitIps", keyValueList);
        return "/front/cache/index";
    }

    @ResponseBody
    @PostMapping("list")
    public Object list(String key) {
        Object s = stringRedisTemplate.opsForValue().get(key.trim());
        return success(s);
    }

    @ResponseBody
    @PostMapping("set")
    public Object set(String key, String value) {
        Object s = stringRedisTemplate.opsForValue().getAndSet(key.trim(), value);
        return success(s);
    }

    @ResponseBody
    @PostMapping("deletekey")
    public Object deletekey(String key) {
        Object s = stringRedisTemplate.delete(key);
        return success(s);
    }

    @PostMapping("/blacklist/remove")
    @ResponseBody
    public BaseResult manageList(String ip) {
        BaseResult result = new BaseResult();
        try {
            Long id = stringRedisTemplate.opsForSet().remove(BizConstant.FQ_BLACK_LIST_REDIS_KEY, ip);
            //改为1
            String clickkey = BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX + DateUtil.formatDate(new Date());
            stringRedisTemplate.opsForZSet().add(clickkey,ip, 1);
        } catch (Exception e) {
            logger.error("", e);
        }
        return result;
    }

    @PostMapping("/blacklist/removeAll")
    @ResponseBody
    public BaseResult removeAll() {
        BaseResult result = new BaseResult();
        try {
            Boolean ok = stringRedisTemplate.delete(BizConstant.FQ_BLACK_LIST_REDIS_KEY);
            result.setData(ok);
        } catch (Exception e) {
            logger.error("", e);
        }
        return result;
    }

    @PostMapping("/blacklist/add")
    @ResponseBody
    public BaseResult add(String ip) {
        BaseResult result = new BaseResult();
        try {
            if (!Validator.isIpv4(ip)) {
                result.setResult(ResultEnum.FAIL);
                result.setMessage("IP格式不正确！");
                return result;
            }
            stringRedisTemplate.opsForSet().add(BizConstant.FQ_BLACK_LIST_REDIS_KEY, ip);
        } catch (Exception e) {
            logger.error("", e);
        }
        return result;
    }
}
