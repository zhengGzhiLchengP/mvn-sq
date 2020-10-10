package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.collectData.StockHlMapper;
import com.feiqu.system.model.collectData.StockHl;
import com.feiqu.system.model.collectData.StockHlExample;
import com.feiqu.system.service.collectData.StockHlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
* StockHlService实现
* Created by cwd on 2019/10/10.
*/
@Service
@Transactional
@BaseService
public class StockHlServiceImpl extends BaseServiceImpl<StockHlMapper, StockHl, StockHlExample> implements StockHlService {

    private static Logger _log = LoggerFactory.getLogger(StockHlServiceImpl.class);

@Resource
    StockHlMapper stockHlMapper;

}