package com.feiqu.adminFramework.web;

import com.feiqu.system.model.sysData.SysConfig;
import com.feiqu.system.model.sysData.SysConfigExample;
import com.feiqu.system.service.sysData.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RuoYi首创 html调用 thymeleaf 实现参数管理
 *
 * @author ruoyi
 */
@Service("config")
public class ConfigService {
    @Resource
    private SysConfigService configService;

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数名称
     * @return 参数键值
     */
    public String getKey(String configKey) {
        SysConfigExample sysConfigExample = new SysConfigExample();
        sysConfigExample.createCriteria().andConfigKeyEqualTo(configKey);
        SysConfig sysConfig = configService.selectFirstByExample(sysConfigExample);
        return sysConfig == null ? "" : sysConfig.getConfigValue();
    }
}
