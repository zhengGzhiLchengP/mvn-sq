package com.feiqu.generator.service.sysData;

import com.feiqu.generator.model.sysData.GenTable;
import com.feiqu.generator.model.sysData.GenTableExample;
import com.feiqu.system.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * GenTableService接口
 * created by cwd on 2020/3/18.
 */
public interface GenTableService extends BaseService<GenTable, GenTableExample> {

    List<GenTable> selectDbTableList(GenTable genTable);

    List<GenTable> selectDbTableListByNames(String[] tableNames);

    void importGenTable(List<GenTable> tableList, String operName);

    void validateEdit(GenTable genTable);

    Map<String, String> previewCode(Long tableId);

    byte[] generatorCode(String[] tableName);

    byte[] generatorCode(String tableName);
}