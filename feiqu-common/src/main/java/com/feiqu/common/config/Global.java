package com.feiqu.common.config;

import cn.hutool.setting.dialect.Props;
import com.feiqu.common.utils.YamlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author ruoyi
 */
public class Global {
    private static final Logger log = LoggerFactory.getLogger(Global.class);

    //yml配置文件
    private static String NAME = "application.yml";

    //properties配置文件
    private static Props props = new Props("application.properties");

    /**
     * 当前对象实例
     */
    private static Global global;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<>();

    private Global() {
    }

    /**
     * 静态工厂方法
     */
    public static synchronized Global getInstance() {
        if (global == null) {
            global = new Global();
        }
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            Map<?, ?> yamlMap = null;
            try {
                yamlMap = YamlUtil.loadYaml(NAME);

                Object property = YamlUtil.getProperty(yamlMap, key);
                if(property != null){
                    value = String.valueOf(property);
                    map.put(key, value);
                    return value;
                }

                String env = String.valueOf(YamlUtil.getProperty(yamlMap, "spring.profiles.active"));
                yamlMap = YamlUtil.loadYaml("application-" + env + ".yml");
                property = YamlUtil.getProperty(yamlMap, key);
                value = property == null ? StringUtils.EMPTY : String.valueOf(property);
                map.put(key, value);
            } catch (FileNotFoundException e) {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取配置
     */
    public static String getPropertiesConfig(String key) {
        String value = props.getStr(key);
        return value;
    }

    /**
     * 获取当前开发环境
     *
     * @return
     */
    public static String getEnv() {
        return getConfig("spring.profiles.active");
    }

    /**
     * 获取项目名称
     */
   /* public static String getName()
    {
        return StringUtils.nvl(getConfig("ruoyi.name"), "RuoYi");
    }

    */
    // 获取项目版本
    public static String getVersion() {
        return StringUtils.defaultString(getConfig("feiqu.version"), "3.2.0");
    }

//    获取版权年份

    public static String getCopyrightYear() {
        return StringUtils.defaultString(getConfig("feiqu.copyrightYear"), "2018");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled() {
        return Boolean.valueOf(getConfig("feiqu.addressEnabled"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile() {
        return getConfig("ruoyi.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getConfig("ruoyi.profile") + "avatar/";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getConfig("ruoyi.profile") + "download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getConfig("feiqu.uploadPath");
    }

    /**
     * 获取作者
     */
    public static String getAuthor() {
//        return StringUtils.nvl(getConfig("gen.author"), "ruoyi");
        return null;
    }

    public static String getOssAccessKey() {
        return getPropertiesConfig("aliyun.filesystem.accessKey");
    }

    public static String getOssSecretKey() {
        return getPropertiesConfig("aliyun.filesystem.secretKey");
    }

}
