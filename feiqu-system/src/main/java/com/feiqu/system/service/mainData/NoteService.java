package com.feiqu.system.service.mainData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.mainData.Note;
import com.feiqu.system.model.mainData.NoteExample;

import java.util.List;

/**
* NoteService接口
* created by cwd on 2019/9/19.
*/
public interface NoteService extends BaseService<Note, NoteExample> {

    List<String> selectLabelList(Integer id);
}