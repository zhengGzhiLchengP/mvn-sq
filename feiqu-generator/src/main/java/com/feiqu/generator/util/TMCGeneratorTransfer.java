package com.feiqu.generator.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.assertj.core.util.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TMCGeneratorTransfer {

    private static String transfer_vm = "/tmctemplate/transfer.vm";


    public static void main(String[] args) {
        try {
            Map<String, String> LAST_INSERT_ID_TABLES = new HashMap<>();
            generator("TCTMCCorporation", "feiqu-generator", "com.feiqu.generator", true,
                    LAST_INSERT_ID_TABLES, false, "EMPLOYEE","id","TCTMCCorporation",
                    "f18xbxMltJsUZK9xvjvh","10.100.158.94:3500");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
            transfer_vm = CSSGenerator.class.getResource(transfer_vm).getPath().replaceFirst("/", "");
            ve.init();
            VelocityContext context = new VelocityContext();
            String basePath = CSSGenerator.class.getResource("/").getPath().replace("/target/classes/", "").replaceFirst("/", "");
            basePath = basePath.replace("feiqu-generator", project_name);
            String controllerBasePath = basePath.replace("feiqu-system", project_name);
            String transferPath = controllerBasePath + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/transfer";
            String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());

                String model = StringUtil.lineToHump(ObjectUtils.toString("PRESTORE_APPLY_REVIEW"));
                String transfer = transferPath + "/" + model + "Mapper.java";
                String modelLowerCase = StringUtil.toLowerCaseFirstOne(model);
                File transferFile = new File(transfer);
                if (!transferFile.exists()) {
                    //这边生成vodto的转换
                    Field[] fields = null;
//                    fields=    ReflectUtil.getFields(PrestoreApplyReview.class);
                    List<String> filedNames = Lists.newArrayList();
                    for(Field field : fields){
                        if("serialVersionUID".equals(field.getName())){
                            continue;
                        }
                        filedNames.add(StrUtil.upperFirst(field.getName()));
                    }
                    context = new VelocityContext();
                    context.put("package_name", package_name);
                    context.put("model", model);
                    context.put("mapper", modelLowerCase);
                    context.put("ctime", ctime);
                    context.put("author", "chenweidong");
                    context.put("filedNames", filedNames);
                    VelocityUtil.generate(transfer_vm, transfer, context);
                    System.out.println(transfer);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
