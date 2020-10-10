package com.feiqu.framwork.web.base;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.PageDomain;
import com.feiqu.common.core.page.TableSupport;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.system.model.FqAdvertisement;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.github.pagehelper.PageHelper;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author chenweidong
 */
public class BaseController {
//    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected static final String USER_LOGIN_REDIRECT_URL = "redirect:/u/login";
    protected static final String GENERAL_ERROR_URL = "/error";
    protected static final String GENERAL_NOT_FOUNF_404_URL = "/404";
    protected static final String GENERAL_CUSTOM_ERROR_URL = "/error/generalCustomError";
    protected static final String GENERAL_CUSTOM_ERROR_CODE = "errorMsg";//

    private static final String LOCAL_IP_V6 = "0:0:0:0:0:0:0:1";

    /*protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }*/

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (ObjectUtil.isNotNull(pageNum) && ObjectUtil.isNotNull(pageSize)) {
            String orderBy = "";
            if (Validator.isGeneral(pageDomain.getOrderBy())) {
                orderBy = pageDomain.getOrderBy();
            }
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success(Object o) {
        return AjaxResult.success(o);
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult notLoginError() {
        return AjaxResult.error("用户未登陆！");
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String errorMsg) {
        return AjaxResult.error(errorMsg);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    protected void addErrorMsg(Model model,String msg){
        model.addAttribute(GENERAL_CUSTOM_ERROR_CODE,msg);
    }



    protected FqUserCache getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        WebUtil webUtil = SpringUtils.getBean(WebUtil.class);
        return webUtil.currentUser(request, response);
    }

    protected FqUserCache getCurrentUser() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = servletRequestAttributes
                .getRequest();
        HttpServletResponse response = servletRequestAttributes
                .getResponse();
        WebUtil webUtil = SpringUtils.getBean(WebUtil.class);
        return webUtil.currentUser(request, response);
    }

    protected String getIP() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = servletRequestAttributes
                .getRequest();
        String ip = ServletUtil.getClientIP(request);
        if(LOCAL_IP_V6.equals(ip)){
            ip = NetUtil.LOCAL_IP;
        }
        return ip;
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StrUtil.format("redirect:{}", url);
    }
    /**
     * 页面跳转
     */
    protected String loginRedirect(String url) {
        return StrUtil.format("redirect:{}", "/u/login?rsUrl="+url);
    }

    protected List<FqAdvertisement> getAdvertisements(AdvertisementPositionEnum advertisementPositionEnum) {
        return CommonConstant.FQ_ADVERTISEMENTS.get(advertisementPositionEnum.getPosition());
    }

}
