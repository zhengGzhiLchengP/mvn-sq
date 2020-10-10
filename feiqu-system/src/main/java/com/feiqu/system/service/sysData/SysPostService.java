package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysPost;
import com.feiqu.system.model.sysData.SysPostExample;

import java.util.List;

/**
 * SysPostService接口
 * created by cwd on 2019/7/15.
 */
public interface SysPostService extends BaseService<SysPost, SysPostExample> {

    List<SysPost> selectPostsByUserId(Long userId);

    String checkPostNameUnique(SysPost post);

    String checkPostCodeUnique(SysPost post);
}