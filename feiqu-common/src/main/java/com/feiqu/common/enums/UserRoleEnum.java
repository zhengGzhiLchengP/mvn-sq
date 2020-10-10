package com.feiqu.common.enums;

/**
 * UserRoleEnum
 *
 * @author chenweidong
 * @date 2017/11/28
 */
public enum  UserRoleEnum {
    NO_ROLE("社区成员",0),
    ADMIN_USER_ROLE("官方人员",1),
    GUEST_ROLE("社区成员",2),
    ADMIN_PLUS("官方人员PLUS",3),
    VIP("VIP用户",4),
    ACTIVE_USER("社区活跃用户",5),
    ;

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

    UserRoleEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }

    public static String getDescByValue(Integer value) {
        if (value == null) return "";
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.value.equals(value)) {
                return userRoleEnum.desc;
            }
        }
        return "";
    }


}
