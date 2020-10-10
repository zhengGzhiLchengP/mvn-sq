package com.feiqu.system.pojo.response;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.system.model.FqGoodPic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FqGoodPicDTO {
    private Long id;

    private String title;

    /**
     * 原始的图片列表
     *
     * @mbg.generated
     */
    private List<String> picUrlList;
    /**
     * 低质量的图片列表
     *
     * @mbg.generated
     */
    private List<String> lowQuaPicUrlList;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private String gmtCreate;

    public FqGoodPicDTO() {    }

    public FqGoodPicDTO(FqGoodPic fqGoodPic) {
        this.setId(fqGoodPic.getId());
        this.setTitle(fqGoodPic.getTitle());
        this.setGmtCreate(DateUtil.format(fqGoodPic.getGmtCreate(), DatePattern.CHINESE_DATE_FORMAT));
        this.setPicUrlList(Arrays.asList(fqGoodPic.getPicUrlList().split(",")));
        this.setLowQuaPicUrlList(this.getPicUrlList().stream().map(e->e+ BizConstant.ALIOSS_IMG_SUFFIX).collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public List<String> getLowQuaPicUrlList() {
        return lowQuaPicUrlList;
    }

    public void setLowQuaPicUrlList(List<String> lowQuaPicUrlList) {
        this.lowQuaPicUrlList = lowQuaPicUrlList;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
