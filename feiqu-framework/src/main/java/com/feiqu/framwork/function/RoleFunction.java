package com.feiqu.framwork.function;

import com.feiqu.common.enums.UserRoleEnum;
import org.beetl.core.Context;
import org.beetl.core.Function;

public class RoleFunction implements Function {
    @Override
    public Object call(Object[] params, Context context) {
        if(params[0] == null){
            return null;
        }
        for(UserRoleEnum roleEnum : UserRoleEnum.values()){
            if(roleEnum.getValue().equals(params[0])){
                return roleEnum.getDesc();
            }
        }
        return "";
    }
}
