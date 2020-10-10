package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysPostMapper;
import com.feiqu.system.model.sysData.SysPost;
import com.feiqu.system.model.sysData.SysPostExample;
import com.feiqu.system.service.sysData.SysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysPostService实现
 * Created by cwd on 2019/7/15.
 */
@Service
@Transactional
@BaseService
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost, SysPostExample> implements SysPostService {

    private static Logger _log = LoggerFactory.getLogger(SysPostServiceImpl.class);

    @Resource
    SysPostMapper sysPostMapper;

    @Override
    public List<SysPost> selectPostsByUserId(Long userId) {
        List<SysPost> userPosts = sysPostMapper.selectPostsByUserId(userId);
        List<SysPost> posts = sysPostMapper.selectByExample(new SysPostExample());
        for (SysPost post : posts) {
            for (SysPost userRole : userPosts) {
                if (post.getPostId().longValue() == userRole.getPostId().longValue()) {
                    post.setFlag(true);
                    break;
                }
            }
        }
        return posts;
    }

    @Override
    public String checkPostNameUnique(SysPost post) {
        Long postId = ObjectUtil.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPostExample sysPostExample = new SysPostExample();
        sysPostExample.createCriteria().andPostNameEqualTo(post.getPostName());
        SysPost info = selectFirstByExample(sysPostExample);
        if (ObjectUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    @Override
    public String checkPostCodeUnique(SysPost post) {
        Long postId = ObjectUtil.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPostExample sysPostExample = new SysPostExample();
        sysPostExample.createCriteria().andPostCodeEqualTo(post.getPostCode());
        SysPost info = selectFirstByExample(sysPostExample);
        if (ObjectUtil.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }
}