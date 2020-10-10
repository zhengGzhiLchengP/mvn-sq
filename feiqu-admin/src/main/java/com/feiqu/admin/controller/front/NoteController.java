package com.feiqu.admin.controller.front;

import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.mainData.Note;
import com.feiqu.system.model.mainData.NoteExample;
import com.feiqu.system.service.mainData.NoteService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 笔记服务
 */
@Controller
@RequestMapping("/front/note")
public class NoteController extends BaseController {
    private String prefix = "front/note";
    private static Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Resource
    private NoteService noteService;

    @RequiresPermissions("front:note:view")
    @GetMapping("")
    public String index() {
        return prefix + "/note";
    }

    /**
     * 查询参数配置列表
     */
    @RequiresPermissions("front:note:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Note note) {
        startPage();
        NoteExample noteExample = new NoteExample();
        NoteExample.Criteria criteria = noteExample.createCriteria();
        if (note.getUserId() != null) {
            criteria.andUserIdEqualTo(note.getUserId());
        }
        if (StrUtil.isNotEmpty(note.getSummary())) {
            criteria.andSummaryLike("%" + note.getSummary() + "%");
        }
        noteExample.setOrderByClause("id desc");
        List<Note> list = noteService.selectByExample(noteExample);
        return getDataTable(list);
    }

    @RequiresPermissions("front:note:remove")
    @ResponseBody
    @Log(title = "移除笔记", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public Object remove(String ids) {
        try {
            return toAjax(noteService.deleteByPrimaryKeys(ids));
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }
}
