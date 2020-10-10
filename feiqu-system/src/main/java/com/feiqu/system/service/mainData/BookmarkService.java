package com.feiqu.system.service.mainData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.mainData.Bookmark;
import com.feiqu.system.model.mainData.BookmarkExample;

import java.util.List;

/**
* BookmarkService接口
* created by cwd on 2020/1/16.
*/
public interface BookmarkService extends BaseService<Bookmark, BookmarkExample> {

    void saveBatch(List<Bookmark> bookmarks, Integer userId);
}