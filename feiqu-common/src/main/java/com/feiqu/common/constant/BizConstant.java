package com.feiqu.common.constant;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.config.Global;

import java.util.Date;

/**
 *
 */
public class BizConstant {
    public static String LOG_KEY = "feiqu_log_list";
    //日志一次消费多少条
    public static Integer maxLogConsumeSize = 5000;

    public static String FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX = "fq_ip_data:";

    public static String FQ_BLACK_LIST_REDIS_KEY = "fq_black_list_key";

    public static Integer DEAULT_PAGE_SIZE = 20;
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";
    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";
    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };
    public final static int TIMEOUT = 10000;
    public static String FILE_SYSTEM_ALIYUN = "aliyun";

    public static Integer SIGN_DAYS_QUDOU_NUM_5 = 1;
    public static Integer SIGN_DAYS_QUDOU_NUM_15 = 5;
    public static Integer SIGN_DAYS_QUDOU_NUM_30 = 10;
    public static Integer SIGN_DAYS_QUDOU_NUM_30_MORE = 20;
    public static String ALIOSS_IMG_SUFFIX = "?x-oss-process=style/low_qua";
    public static String MOBILE_ALIOSS_IMG_SUFFIX = "-mobile";


    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";
    public static String FILE_NAME_PREFIX = "fq_";
    //图片前缀
    public static String QINIU_IMG_PREFIX = "http://static.f2qu.com/";
    public static String ALIYUN_OSS_IMG_PREFIX = "https://cwd-res2.oss-cn-shanghai.aliyuncs.com/";

    public static String SEVEN_DAYS_HOT_THOUGHT_LIST = "fq_7_days_hot_thoughts_" + DateUtil.formatDate(new Date());//7天最热的
    public static String FQ_IP_DATA_THIS_DAY_FORMAT = "fq_ip_data_" + DateUtil.formatDate(new Date());
    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    public static Integer INIT_QUDOU_NUM = 20;
    //每个月的 最大活跃度 限制
    public static Integer USER_MAX_ACTIVE_NUM = 1000;
    //未登陆情况下每个ip每天访问的限制
    public static Integer USER_IP_VISIT_MAX_COUNT = 100;
    //5分钟ws发出消息的次数 一条评论 6s
    public static Integer WS_SEND_MAX_COUNT = 50;

    public static String FRONT_DOMAIN_URL = Global.getConfig("feiqu.frontDomainUrl");
    public static String FQ_ACTIVE_USER_SORT = "fq_active_user_sort_";
    public static String FQ_USER_TOTAL_COUNT = "user_total_count";
    //缓存的key
    public static String CACHE_NEW_THOUGHT_LIST = "new_thought_list";
    public static String CACHE_NEW_SIMPLE_USERS = "new_simple_users";

    public static String FQ_WEBSITE_ALL = "fq_website_all";
    public static Integer MAX_NEW_USER_COUNT = 20;//最大新用户数量

    public static int ALIYUN_OSS_EXPIRE = 300;
    public static long ALIYUN_OSS_MAX_SIZE = 100;//100M

}
