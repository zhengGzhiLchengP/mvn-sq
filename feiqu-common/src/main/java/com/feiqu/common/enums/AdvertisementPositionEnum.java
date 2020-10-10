package com.feiqu.common.enums;

/**
 * @author cwd
 * @version AdvertisementPositionEnum, v 0.1 2019/2/22 cwd 1049766 广告位置
 */
public enum AdvertisementPositionEnum {
    INDEX_BANNER(0,"首页"),
    LIST(1,"列表页"),
    DETAIL(2,"详情页"),;

    AdvertisementPositionEnum(Integer position, String desc) {
        this.position = position;
        this.desc = desc;
    }

    private Integer position;
    private String desc;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
