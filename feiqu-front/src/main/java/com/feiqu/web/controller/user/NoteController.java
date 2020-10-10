package com.feiqu.web.controller.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ActiveNumEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.mainData.Note;
import com.feiqu.system.model.mainData.NoteExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.NoteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *笔记服务
 */
@Controller
@RequestMapping("note")
public class NoteController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Resource
    private NoteService noteService;

    private String labelCacheKey = "label_cache";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") Integer page, String keyword,
                        Model model,
                        @RequestParam(required = false) String label) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return loginRedirect("/note");
        }
        if(fqUserCache.getLevel() == null || fqUserCache.getLevel() < 2){
            addErrorMsg(model,"暂时只支持2级以上用户访问");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        List<String> finalLabelList = null;
        String key = (labelCacheKey + fqUserCache.getId());
        if (stringRedisTemplate.hasKey(key)) {
            finalLabelList = JSON.parseArray(stringRedisTemplate.opsForValue().get(key), String.class);
        } else {
            List<String> labelList = noteService.selectLabelList(fqUserCache.getId());
            if (CollectionUtil.isEmpty(labelList)) {

            } else {
                finalLabelList = CollectionUtil.newArrayList();
                for (String l : labelList) {
                    if (StrUtil.isNotEmpty(l)) {
                        String[] strings = l.split(",");
                        finalLabelList.addAll(Arrays.asList(strings));
                    }
                }
            }
            stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(finalLabelList), 60, TimeUnit.SECONDS);
        }
        model.addAttribute("labelList", finalLabelList);
        NoteExample noteExample = new NoteExample();

        PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE);
        noteExample.clear();
        noteExample.setOrderByClause("gmt_update desc,id desc");
        NoteExample.Criteria criteria = noteExample.createCriteria();
        criteria.andUserIdEqualTo(fqUserCache.getId());
        if (StringUtils.isNotEmpty(keyword)) {
            criteria.andSummaryLike("%" + keyword + "%");
        }
        if (StrUtil.isNotEmpty(label)) {
            criteria.andLabelLike("%" + label + "%");
        }
        List<Note> notes = noteService.selectByExample(noteExample);
        PageInfo pageInfo = null;
        if(CollectionUtil.isEmpty(notes)){
            pageInfo = new PageInfo();
        }else {
            pageInfo = new PageInfo(notes);
        }
        model.addAttribute("pageInfo", pageInfo);
        return "/front/note/index";
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/{id}")
    public Object save(@PathVariable Integer id) {
        try {
            Note note = noteService.selectByPrimaryKey(id);
            if(note != null){
                note.setContent(HtmlUtils.htmlUnescape(note.getContent()));
                return success(note);
            }
        }catch (Exception e){
            return error();
        }
        return error();
    }

    /**
     * 保存
     * @param note
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(Note note) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(StrUtil.isBlank(note.getContent())){
                return error("笔记不能为空！");
            }
            String escapedHtml = HtmlUtil.unescape(note.getContent());
            escapedHtml = HtmlUtil.cleanHtmlTag(escapedHtml);
            if(escapedHtml.length() > 100){
                note.setSummary(StrUtil.sub(escapedHtml,0,100)+"...");
            }else {
                note.setSummary(escapedHtml);
            }
            if (note.getId() == null || note.getId() == 0) {
                note.setGmtCreate(new Date());
                note.setGmtUpdate(new Date());
                note.setUserId(fqUserCache.getId());

                note.setSize(StrUtil.byteLength(note.getContent(), CharsetUtil.CHARSET_UTF_8));
                int code = noteService.insert(note);
                CommonUtils.addActiveNum(fqUserCache.getId(), ActiveNumEnum.POST_NOTE.getValue());
                if(code == 0){
                    return error("保存失败");
                }
            } else {
                note.setSize(StrUtil.byteLength(note.getContent(), CharsetUtil.CHARSET_UTF_8));
                note.setGmtUpdate(new Date());
                noteService.updateByPrimaryKeySelective(note);
            }
            removeLabelCache(fqUserCache.getId());
            return success(note);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }

    private void removeLabelCache(Integer userId) {
        stringRedisTemplate.delete(labelCacheKey + userId);
    }

    @ResponseBody
    @PostMapping("/del/{id}")
    public Object del(@PathVariable Integer id) {
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                return error("用户未登陆");
            }
            Note noteDB = noteService.selectByPrimaryKey(id);
            if(noteDB == null){
                return error("笔记不存在");
            }
            if(!noteDB.getUserId().equals(fqUserCache.getId())){
                return error("你无权删除别人的笔记");
            }
            noteService.deleteByPrimaryKey(id);
            removeLabelCache(fqUserCache.getId());
            return success();
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }
}
