package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.TaobaoProductMapper;
import com.feiqu.system.model.basicData.TaobaoProduct;
import com.feiqu.system.model.basicData.TaobaoProductExample;
import com.feiqu.system.service.basicData.TaobaoProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * TaobaoProductService实现
 * Created by cwd on 2019/7/30.
 */
@Service
@Transactional
@BaseService
public class TaobaoProductServiceImpl extends BaseServiceImpl<TaobaoProductMapper, TaobaoProduct, TaobaoProductExample> implements TaobaoProductService {

    private static Logger _log = LoggerFactory.getLogger(TaobaoProductServiceImpl.class);

    @Resource
    TaobaoProductMapper taobaoProductMapper;

}