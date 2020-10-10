package com.feiqu.adminFramework.web.base;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.PageDomain;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.common.core.page.TableSupport;
import com.feiqu.system.model.sysData.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected SysUser getUser() {
        return ShiroUtils.getSysUser();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

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

    protected boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StrUtil.containsAnyIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StrUtil.containsAnyIgnoreCase(ajax, "json", "xml")) {
            return true;
        }
        return false;
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
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回成功
     */
    public AjaxResult success(Object obj) {
        return AjaxResult.success(obj);
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


    protected String getIP() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = servletRequestAttributes
                .getRequest();
        return ServletUtil.getClientIP(request);
    }

    /**
     * 页面跳转
     */
    protected String redirect(String url) {
        return StrUtil.format("redirect:{}", url);
    }
}
