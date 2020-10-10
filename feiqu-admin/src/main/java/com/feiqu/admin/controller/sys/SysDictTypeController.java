package com.feiqu.admin.controller.sys;

import cn.hutool.core.util.ObjectUtil;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.common.core.domain.TableDataInfo;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.sysData.SysDictType;
import com.feiqu.system.model.sysData.SysDictTypeExample;
import com.feiqu.system.service.sysData.SysDictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {
    private String prefix = "system/dict/type";

    @Resource
    private SysDictTypeService dictTypeService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictType() {
        return prefix + "/type";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectByExample(new SysDictTypeExample());
        return getDataTable(list);
    }

    /*@Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDictType dictType)
    {

        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }*/

    /**
     * 新增字典类型
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDictType dict) {
        dict.setCreateBy(ShiroUtils.getLoginName());
        dict.setCreateTime(new Date());
        return toAjax(dictTypeService.insert(dict));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        mmap.put("dict", dictTypeService.selectByPrimaryKey(dictId));
        return prefix + "/edit";
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDictType dict) {
        dict.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(dictTypeService.updateByPrimaryKeySelective(dict));
    }

    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(dictTypeService.deleteDictTypeByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 查询字典详细
     */
    @RequiresPermissions("system:dict:list")
    @GetMapping("/detail/{dictId}")
    public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        mmap.put("dict", dictTypeService.selectByPrimaryKey(dictId));
        mmap.put("dictList", dictTypeService.selectByExample(new SysDictTypeExample()));
        return "system/dict/data/data";
    }

    /**
     * 校验字典类型
     */
    @PostMapping("/checkDictTypeUnique")
    @ResponseBody
    public String checkDictTypeUnique(SysDictType dictType) {
        Long dictId = ObjectUtil.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
        SysDictTypeExample sysDictTypeExample = new SysDictTypeExample();
        sysDictTypeExample.createCriteria().andDictTypeEqualTo(dictType.getDictType());
        SysDictType temp = dictTypeService.selectFirstByExample(sysDictTypeExample);
        if (ObjectUtil.isNotNull(temp) && temp.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }
}
