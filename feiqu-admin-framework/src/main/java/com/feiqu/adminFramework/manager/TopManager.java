package com.feiqu.adminFramework.manager;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.adminFramework.commander.AbstractSystemCommander;

public class TopManager {
    public static JSONObject getTopMonitor() {
        JSONObject topInfo = AbstractSystemCommander.getInstance().getAllMonitor();
        if (topInfo != null) {
            String time = DateUtil.formatTime(DateUtil.date());
            topInfo.put("time", time);
            return topInfo;}
        return null;
    }
}
