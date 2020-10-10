package com.feiqu.common.core.page;

import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.core.domain.PageDomain;
import com.feiqu.common.utils.ServletUtils;

/**
 * 表格数据处理
 *
 * @author ruoyi
 */
public class TableSupport {
    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(BizConstant.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(BizConstant.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(BizConstant.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(BizConstant.IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
