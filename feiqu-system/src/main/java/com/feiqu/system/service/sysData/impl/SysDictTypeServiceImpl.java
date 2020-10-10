package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.convert.Convert;
import com.feiqu.common.exception.BusinessException;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysDictDataMapper;
import com.feiqu.system.mapper.sysData.SysDictTypeMapper;
import com.feiqu.system.model.sysData.SysDictData;
import com.feiqu.system.model.sysData.SysDictDataExample;
import com.feiqu.system.model.sysData.SysDictType;
import com.feiqu.system.model.sysData.SysDictTypeExample;
import com.feiqu.system.service.sysData.SysDictTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * SysDictTypeService实现
 * Created by cwd on 2019/6/12.
 */
@Service
@Transactional
@BaseService
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeMapper, SysDictType, SysDictTypeExample> implements SysDictTypeService {

    private static Logger _log = LoggerFactory.getLogger(SysDictTypeServiceImpl.class);

    @Resource
    SysDictTypeMapper sysDictTypeMapper;
    @Resource
    SysDictDataMapper sysDictDataMapper;

    @Override
    public int deleteDictTypeByIds(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            SysDictType dictType = selectByPrimaryKey(dictId);
            SysDictDataExample sysDictDataExample = new SysDictDataExample();
            sysDictDataExample.createCriteria().andDictTypeEqualTo(dictType.getDictType());
            if (sysDictDataMapper.countByExample(sysDictDataExample) > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }

        SysDictTypeExample sysDictTypeExample = new SysDictTypeExample();
        sysDictTypeExample.createCriteria().andDictIdIn(Arrays.asList(dictIds));
        return sysDictTypeMapper.deleteByExample(sysDictTypeExample);
    }
}