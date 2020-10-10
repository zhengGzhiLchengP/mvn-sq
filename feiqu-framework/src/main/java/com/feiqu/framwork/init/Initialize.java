package com.feiqu.framwork.init;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.setting.dialect.Props;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.GirlCategoryEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.base.BaseInterface;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.response.*;
import com.feiqu.system.service.mainData.*;
import com.feiqu.system.service.mainData.impl.FqBackgroundImgServiceImpl;
import com.feiqu.system.service.mainData.impl.FqNoticeServiceImpl;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Initialize implements BaseInterface {

    private static Logger logger = LoggerFactory.getLogger(Initialize.class);

    public void init() {
        try {
            CommonConstant.DOMAIN_URL = Global.getConfig("feiqu.domainUrl");
            CommonConstant.MAX_UPLOAD_SIZE = Integer.valueOf(Global.getConfig("feiqu.maxUploadSize"));
            CommonConstant.FILE_UPLOAD_TEMP_PATH = Global.getConfig("feiqu.uploadPath");

            Props props = new Props("application.properties");
            CommonConstant.ALIYUN_OSS_BUCKET_NAME = props.getStr("aliyun.filesystem.bucketName");
            CommonConstant.ALIYUN_OSS_ENDPOINT = props.getStr("aliyun.filesystem.endpoint");
            CommonConstant.ALIYUN_OSS_ACTION_URL = "https://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT;

            Date now = new Date();

            Date oneMonthDateBefore = DateUtil.offsetDay(now, -31);

            FqNoticeExample example = new FqNoticeExample();
            example.setOrderByClause("fq_order asc");
            example.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
            FqNoticeService fqNoticeService = SpringUtils.getBean(FqNoticeServiceImpl.class);
            List<FqNotice> list = fqNoticeService.selectByExample(example);
            if (CollectionUtil.isNotEmpty(list)) {
                CommonConstant.FQ_NOTICE_LIST = list;
            }
            ArticleExample articleExample = new ArticleExample();
            articleExample.setOrderByClause("browse_count desc");
            articleExample.createCriteria().andCreateTimeGreaterThan(oneMonthDateBefore).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(0, 10, false);
            ArticleService articleService = SpringUtils.getBean("articleServiceImpl");
            CommonConstant.HOT_ARTICLE_LIST = articleService.selectByExample(articleExample);

            articleExample.clear();
            articleExample.setOrderByClause("browse_count desc");
            articleExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(0, 10, false);
            CommonConstant.ALL_HOT_ARTICLE_LIST = articleService.selectByExample(articleExample);

            CommonConstant.ALIOSS_URL_PREFIX = props.getStr("aliyun.filesystem.urlprefix");
            CommonConstant.ALIYUN_OSS_CALLBACK = props.getStr("aliyun.filesystem.callbackUrl");

            FqBackgroundImgService fqBackgroundImgService = SpringUtils.getBean(FqBackgroundImgServiceImpl.class);
            FqBackgroundImgExample backgroundImgExample = new FqBackgroundImgExample();
            backgroundImgExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(backgroundImgExample);

            CommonConstant.bgImgUrl = fqBackgroundImg == null ? "https://img.t.sinajs.cn/t6/skin/skinvip805/images/body_bg.jpg?id=1410943047113" : fqBackgroundImg.getImgUrl();

            for (GirlCategoryEnum categoryEnum : GirlCategoryEnum.values()) {
                CommonConstant.CATEGORIES.add(new KeyValue(categoryEnum.getCode(), categoryEnum.getDesc()));
            }
            FqAdvertisementService fqAdvertisementService = SpringUtils.getBean(FqAdvertisementService.class);
            FqAdvertisementExample fqAdvertisementExample = new FqAdvertisementExample();
            fqAdvertisementExample.setOrderByClause("id DESC");
            List<FqAdvertisement> advertisements = fqAdvertisementService.selectByExample(fqAdvertisementExample);
            if (CollectionUtils.isNotEmpty(advertisements)) {
                Map<Integer, List<FqAdvertisement>> map = advertisements.stream().collect(Collectors.groupingBy(FqAdvertisement::getPosition));
                CommonConstant.FQ_ADVERTISEMENTS = map;
            }
        } catch (Exception e) {
            logger.error("初始化报错", e);
            throw e;
        }

        logger.info(">>>>>飞趣社区 系统初始化完成！<<<<<<");
    }


}
