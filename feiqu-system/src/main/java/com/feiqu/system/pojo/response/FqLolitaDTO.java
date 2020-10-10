package com.feiqu.system.pojo.response;

import com.feiqu.system.model.FqLolita;

/**
 * Created by Administrator on 2019/2/23.
 */
public class FqLolitaDTO extends FqLolita
{
    private String token;

    private String nickname;
    private String icon;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
