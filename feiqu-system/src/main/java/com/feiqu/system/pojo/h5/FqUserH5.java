package com.feiqu.system.pojo.h5;

import com.feiqu.system.model.FqUser;

public class FqUserH5 extends FqUser {
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
