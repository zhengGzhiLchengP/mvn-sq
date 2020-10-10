package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysUserOnline;
import com.feiqu.system.model.sysData.SysUserOnlineExample;

import java.util.Date;
import java.util.List;

/**
* SysUserOnlineService接口
* created by cwd on 2019/6/10.
*/
public interface SysUserOnlineService extends BaseService<SysUserOnline, SysUserOnlineExample> {
    /**
     * 通过会话序号删除信息
     *
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    void batchDeleteOnline(List<String> sessions);

    /**
     * 查询会话集合
     *
     * @param expiredDate 有效期
     * @return 会话集合
     */
    List<SysUserOnline> selectOnlineByExpired(Date expiredDate);

    int saveOnline(SysUserOnline online);
}