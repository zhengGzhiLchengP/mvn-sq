package com.feiqu.web.controller.resource;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.XiaomiCity;
import com.feiqu.system.model.basicData.XiaomiCityExample;
import com.feiqu.system.service.basicData.XiaomiCityService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("weather")
public class WeatherController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(WallPaperController.class);

    @Resource
    private XiaomiCityService xiaomiCityService;

    @RequestMapping("")
    public String index(Model model,String cityName){
        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        XiaomiCityExample example = new XiaomiCityExample();
        if(StringUtils.isNotEmpty(cityName)){
            example.createCriteria().andCityNameLike("%"+cityName+"%");
        }
        List<XiaomiCity> xiaomiCities = xiaomiCityService.selectByExample(example);
        model.addAttribute("cities",xiaomiCities);
        model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.LIST.getPosition()));
        return "/front/weather/index";
    }

    @RequestMapping("/city/{cityNum}")
    public String city(@PathVariable String cityNum, @RequestParam(required = false,defaultValue = "7") Integer days,Model model){
        String s = "";
            s = HttpUtil.get("https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude=110&longitude=112&locationKey=weathercn:"
                    +cityNum+"&days="+days+"&appKey=weather20151024&sign=zUFJoAR2ZVrDy1vF3D07&isGlobal=false&locale=zh_cn");
        logger.debug(s);
        JSONObject wjsonObject = new JSONObject(s);
        JSONObject current = wjsonObject.getJSONObject("current");
        JSONObject forecastDaily = wjsonObject.getJSONObject("forecastDaily");
        JSONObject aqi = wjsonObject.getJSONObject("aqi");
        JSONArray alerts = wjsonObject.getJSONArray("alerts");
        model.addAttribute("current",current);
        model.addAttribute("forecastDaily",forecastDaily);
        model.addAttribute("alerts",alerts);
        model.addAttribute("alerts",alerts);
        model.addAttribute("aqi",aqi);
        return "/front/weather/cityWeather";
    }
}
