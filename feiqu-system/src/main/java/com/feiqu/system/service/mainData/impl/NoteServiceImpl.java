package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.NoteMapper;
import com.feiqu.system.model.mainData.Note;
import com.feiqu.system.model.mainData.NoteExample;
import com.feiqu.system.service.mainData.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * NoteService实现
 * Created by cwd on 2019/9/19.
 */
@Service
@Transactional
@BaseService
public class NoteServiceImpl extends BaseServiceImpl<NoteMapper, Note, NoteExample> implements NoteService {

    private static Logger _log = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Resource
    NoteMapper noteMapper;

    @Override
    public List<String> selectLabelList(Integer id) {
        return noteMapper.selectLabelList(id);
    }
}