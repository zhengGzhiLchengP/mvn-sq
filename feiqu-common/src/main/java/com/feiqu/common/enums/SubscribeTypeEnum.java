package com.feiqu.common.enums;

//订阅类型
public enum SubscribeTypeEnum {
    USER_MENU("用户菜单", 1);

    String desc;
    Integer value;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    SubscribeTypeEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
