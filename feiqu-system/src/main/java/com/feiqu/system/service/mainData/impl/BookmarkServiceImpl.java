package com.feiqu.system.service.mainData.impl;

import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.BookmarkMapper;
import com.feiqu.system.model.mainData.Bookmark;
import com.feiqu.system.model.mainData.BookmarkExample;
import com.feiqu.system.service.mainData.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* BookmarkService实现
* Created by cwd on 2020/1/16.
*/
@Service
@Transactional
@BaseService
public class BookmarkServiceImpl extends BaseServiceImpl<BookmarkMapper, Bookmark, BookmarkExample> implements BookmarkService {

    private static Logger logger = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    @Resource
    BookmarkMapper bookmarkMapper;

    @Override
    public void saveBatch(List<Bookmark> bookmarks, Integer userId) {
        BookmarkExample bookmarkExample = new BookmarkExample();
        bookmarkExample.createCriteria().andUserIdEqualTo(userId);
        Bookmark bookmark = new Bookmark();
        bookmark.setDelFlag(YesNoEnum.YES.getValue());
        bookmarkMapper.updateByExampleSelective(bookmark,bookmarkExample);
        bookmarkMapper.saveBatch(bookmarks);
    }
}