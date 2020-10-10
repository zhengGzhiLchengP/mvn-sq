package com.feiqu.adminFramework.web;

import com.alibaba.fastjson.JSON;
import com.feiqu.system.model.sysData.SysDictData;
import com.feiqu.system.model.sysData.SysDictDataExample;
import com.feiqu.system.service.sysData.SysDictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现字典读取
 *
 * @author ruoyi
 */
@Service("dict")
public class DictService {
    @Resource
    private SysDictDataService dictDataService;

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 参数键值
     */
    public Object getType(String dictType) {
        SysDictDataExample sysDictDataExample = new SysDictDataExample();
        sysDictDataExample.createCriteria().andDictTypeEqualTo(dictType).andStatusEqualTo("0");
        return JSON.toJSON(dictDataService.selectByExample(sysDictDataExample));
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String getLabel(String dictType, String dictValue) {
        SysDictDataExample sysDictDataExample = new SysDictDataExample();
        sysDictDataExample.createCriteria().andDictTypeEqualTo(dictType)
                .andDictValueEqualTo(dictValue)
                .andStatusEqualTo("0");
        SysDictData sysDictData = dictDataService.selectFirstByExample(sysDictDataExample);
        return sysDictData == null ? "" : sysDictData.getDictLabel();
    }
}
