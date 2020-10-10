package com.feiqu.generator.util;

import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 自动生成代码 tmc 我现在公司的
 */
public class TMCGenerator {

    // Service模板路径
    private static String service_vm = "/tmctemplate/Service.vm";
    // ServiceImpl模板路径
    private static String serviceImpl_vm = "/tmctemplate/ServiceImpl.vm";
    private static String controller_vm = "/tmctemplate/Controller.vm";
    // generatorConfig模板路径
    private static String generatorConfig_vm = "/template1/generatorConfig.vm";
    //页面模板
    private static String page_index_vm = "/template1/html_index.vm";
    private static String page_detail_vm = "/template1/html_detail.vm";
    private static String page_manage_vm = "/template1/html_manage.vm";

//    private static String transfer_vm = "/template1/transfer.vm";


    public static void main(String[] args) {
        try {
            Map<String, String> LAST_INSERT_ID_TABLES = new HashMap<>();
            generator("TCTMCCorporation", "feiqu-generator", "com.feiqu.generator", true,
                    LAST_INSERT_ID_TABLES, false,
                    "PRESTORE_APPLY_REVIEW",//表明
                    "id",
                    "TCTMCCorporation",
                    "f18xbxMltJsUZK9xvjvh","10.100.158.94:3500");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param schemaName         数据库的名称
     * @param project_name       项目名称
     * @param package_name       包名称
     * @param includeService     是否包含service
     * @param lastInsertIdTables 需要insert后返回主键的表配置，key:表名,value:主键名
     * @param includeHtml        是否包含页面生成
     * @param onlyTable          表名称
     * @param keyName            主键名称
     */
    public static void generator(String schemaName,
                                 String project_name, String package_name,
                                 Boolean includeService,
                                 Map<String, String> lastInsertIdTables,
                                 Boolean includeHtml,
                                 String onlyTable,
                                 String keyName,
                                 String username,
                                 String password,
                                 String urlPort) {
        try {
            VelocityEngine ve = new VelocityEngine();
            service_vm = CSSGenerator.class.getResource(service_vm).getPath().replaceFirst("/", "");
            serviceImpl_vm = CSSGenerator.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");
            controller_vm = CSSGenerator.class.getResource(controller_vm).getPath().replaceFirst("/", "");
            generatorConfig_vm = CSSGenerator.class.getResource(generatorConfig_vm).getPath().replaceFirst("/", "");
            page_index_vm = CSSGenerator.class.getResource(page_index_vm).getPath().replaceFirst("/", "");
            page_detail_vm = CSSGenerator.class.getResource(page_detail_vm).getPath().replaceFirst("/", "");
//            transfer_vm = CSSGenerator.class.getResource(transfer_vm).getPath().replaceFirst("/", "");

            String generatorConfigXml = CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "") + "/src/main/resources/mybatis-generator.xml";
            generatorConfigXml = generatorConfigXml.replaceFirst("/", "");
            ve.init();
            String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + schemaName + "' AND table_name LIKE '" + "" + "%';";
            JdbcUtil jdbcUtil = new JdbcUtil("com.mysql.cj.jdbc.Driver", "jdbc:mysql://"+urlPort+"/" + schemaName + "?serverTimezone=UTC",
                    username, password);
            List<Map<String, Object>> tables = new ArrayList<>();
            Map<String, Object> table;
            List<Map> result = jdbcUtil.selectByParams(sql, null);
            for (Map map : result) {
                if (StringUtils.isNotEmpty(onlyTable)) {
                    if (!onlyTable.equalsIgnoreCase(String.valueOf(map.get("TABLE_NAME")))) {
                        continue;
                    }
                }

                System.out.println(map.get("TABLE_NAME"));
                table = MapUtil.newHashMap();
                table.put("table_name", map.get("TABLE_NAME"));
                table.put("model_name", StringUtil.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
                tables.add(table);
                lastInsertIdTables.put(String.valueOf(map.get("TABLE_NAME")), keyName);
            }
            jdbcUtil.release();
            VelocityContext context = new VelocityContext();
            context.put("tables", tables);
            context.put("generator_javaModelGenerator_targetPackage", package_name + ".model");
            context.put("generator_sqlMapGenerator_targetPackage", "mapper");
            context.put("generator_javaClientGenerator_targetPackage", package_name + ".mapper");
            context.put("targetProject", project_name);
            context.put("master_driver", "com.mysql.jdbc.Driver");
            context.put("master_url", "jdbc:mysql://"+urlPort+"/" + schemaName + "?serverTimezone=UTC");
            context.put("master_username", username);
            context.put("master_password", password);
            context.put("last_insert_id_tables", lastInsertIdTables);
            VelocityUtil.generate(generatorConfig_vm, generatorConfigXml, context);

            System.out.println("========== 开始运行MybatisGenerator ==========");
            List<String> warnings = new ArrayList<>();
            File configFile = new File(generatorConfigXml);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                System.out.println(warning);
            }
            System.out.println("========== 结束运行MybatisGenerator ==========");

            if (!includeService) {
                return;
            }

            String basePath = CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "").replaceFirst("/", "");
            basePath = basePath.replace("feiqu-generator", project_name);
            String servicePath = basePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/service";
            String serviceImplPath = basePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/service/impl";
            String controllerBasePath = basePath.replace("feiqu-system", project_name);
            String controllerPath = controllerBasePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/controller";
            String transferPath = controllerBasePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/transfer";
            String pagePath = controllerBasePath + "/src/main/resources/templates/";
            String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());
            for (int i = 0; i < tables.size(); i++) {
                String model = StringUtil.lineToHump(ObjectUtils.toString(tables.get(i).get("table_name")));
                String service = servicePath + "/" + model + "Service.java";
                String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
                String controller = controllerPath + "/" + model + "Controller.java";
                String transfer = transferPath + "/" + model + "Mapper.java";
                String modelLowerCase = StringUtil.toLowerCaseFirstOne(model);
                String pageDir = pagePath + modelLowerCase;
                // 生成service
                File servicePathFile = new File(servicePath);
                if(!servicePathFile.exists()){
                    servicePathFile.mkdir();
                }
                File serviceFile = new File(service);
                if (!serviceFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("ctime", ctime);
                    VelocityUtil.generate(service_vm, service, context);
                    System.out.println(service);
                }
                // 生成serviceImpl
                File serviceImplPathFile = new File(serviceImplPath);
                if(!serviceImplPathFile.exists()){
                    serviceImplPathFile.mkdir();
                }
                File serviceImplFile = new File(serviceImpl);
                if (!serviceImplFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                    context.put("ctime", ctime);
                    VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
                    System.out.println(serviceImpl);
                }
                //生成controller
                File controllerPathFile = new File(controllerPath);
                if(!controllerPathFile.exists()){
                    controllerPathFile.mkdir();
                }
                File controllerFile = new File(controller);
                if (!controllerFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                    context.put("ctime", ctime);
                    VelocityUtil.generate(controller_vm, controller, context);
                    System.out.println(controller);
                }

                /*File transferFile = new File(transfer);
                if (!transferFile.exists()) {
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                    context.put("ctime", ctime);
                    context.put("author", "chenweidong");
                    VelocityUtil.generate(transfer_vm, transfer, context);
                    System.out.println(transfer);
                }*/



                if (!includeHtml) {
                    return;
                }
                File pageDirFile = new File(pageDir);
                if (!pageDirFile.exists()) {
                    pageDirFile.mkdir();
                    String pageIndex = pageDir + "/index.html";
                    String pageDetail = pageDir + "/detail.html";
                    String pageManage = pageDir + "/manage.html";
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("ctime", ctime);
                    context.put("mapper", modelLowerCase);
                    VelocityUtil.generate(page_index_vm, pageIndex, context);
                    VelocityUtil.generate(page_detail_vm, pageDetail, context);
                    VelocityUtil.generate(page_manage_vm, pageManage, context);
                    System.out.println(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
