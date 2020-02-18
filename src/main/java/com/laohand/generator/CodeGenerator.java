package com.laohand.generator;

import com.laohand.generator.pojo.ClassInfo;
import com.laohand.generator.pojo.Column;
import com.laohand.generator.util.StringUtil;
import com.laohand.generator.util.TableParseUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author dennis
 */
public class CodeGenerator {
    private CodeGenerator() {
    }

    public static void main(String[] args) throws IOException {
        String sql = "CREATE TABLE 'user' (\n" +
                "  'user_id' int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',\n" +
                "  'username' varchar(255) NOT NULL COMMENT '用户名',\n" +
                "  'addtime' datetime NOT NULL COMMENT '创建时间',\n" +
                "  PRIMARY KEY ('user_id')\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息'\n" +
                "        ";
        gen(sql, "com.laohand", "user", "F:\\github\\foundao\\code-generator\\src\\main\\java\\com\\laohand\\");
    }

    /**
     * 生成模块
     *
     * @param sql         创建数据表文件
     * @param packageName 模块基本包命
     * @param moduleName  模块名称
     * @param savePath    模块保存路径
     * @throws IOException
     */
    public static void gen(String sql, String packageName, String moduleName, String savePath) throws IOException {
        gen(sql, packageName, moduleName, savePath, new TemplateSourceDefault(), null);
    }

    /**
     * 生成模块
     *
     * @param sql         创建数据表文件
     * @param packageName 模块基本包命
     * @param moduleName  模块名称
     * @param savePath    模块保存路径
     * @param userReplace 用户自定义替换字符串
     * @throws IOException
     */
    public static void gen(String sql, String packageName, String moduleName, String savePath, Map<String, String> userReplace) throws IOException {
        gen(sql, packageName, moduleName, savePath, new TemplateSourceDefault(), userReplace);
    }

    /**
     * 生成文件
     *
     * @param sql            创建数据表文件
     * @param packageName    模块基本包命，生成的模块将会放在该包下面
     * @param moduleName     模块名称
     * @param savePath       模块保存路径，需要传绝地路径
     * @param templateSource 模板字符串内容
     * @throws IOException
     */
    public static void gen(String sql, String packageName, String moduleName, String savePath, TemplateSource templateSource, Map<String, String> userReplace) throws IOException {
        ClassInfo classInfo = TableParseUtil.parseSql(sql);
        classInfo.setPackageName(packageName);
        classInfo.setModuleName(moduleName);
        classInfo.setBaseSavePath(savePath);
        System.out.println("【INFO】classInfo => " + classInfo.toString());
        createFile(classInfo, "controller", classInfo.getClassName() + "Controller.java", templateSource.getControllerTemplate(), userReplace);
        createFile(classInfo, "pojo", classInfo.getClassName() + ".java", templateSource.getPojoTemplate(), userReplace);
        createFile(classInfo, "service" + File.separator + "impl", classInfo.getClassName() + "ServiceImpl.java", templateSource.getServiceImplTemplate(), userReplace);
        createFile(classInfo, "service", classInfo.getClassName() + "Service.java", templateSource.getServiceTemplate(), userReplace);
        createFile(classInfo, "mapper", classInfo.getClassName() + "Mapper.java", templateSource.getMapperTemplate(), userReplace);
        createFile(classInfo, "mapper", classInfo.getClassName() + "SqlBuilder.java", templateSource.getSqlBuilderTemplate(), userReplace);
    }

    /**
     * 根据模板创建并替换文件
     *
     * @param classInfo
     * @param path        文件存放相对于模块的路径
     * @param fileName    保存文件名称
     * @param tpl         模板内容
     * @param userReplace 用户自定义需要替换的内容
     * @throws IOException
     */
    public static void createFile(ClassInfo classInfo, String path, String fileName, String tpl, Map<String, String> userReplace) throws IOException {
        if (tpl == null | "".equals(tpl)) {
            return;
        }
        String saveFileName = classInfo.getBaseSavePath() + File.separator + classInfo.getModuleName() + File.separator + path + File.separator + fileName;
        tpl = tpl.replaceAll("CLASS_NAME_UPPER", classInfo.getClassName());
        tpl = tpl.replaceAll("CLASS_NAME_LOWER", classInfo.getClassNameLowerFirst());
        tpl = tpl.replaceAll("TABLE_NAME", classInfo.getTableName());
        tpl = tpl.replaceAll("TABLE_COMMENT", classInfo.getTableName());
        tpl = tpl.replaceAll("PACKAGE", classInfo.getPackageName() + "." + classInfo.getModuleName());
        //用户自定义变量替换
        if (userReplace != null) {
            for (Map.Entry<String, String> entry : userReplace.entrySet()) {
                tpl = tpl.replaceAll(entry.getKey(), entry.getValue());
            }
        }
        //以下代码用于替换pojo对象
        StringBuilder stringBuffer = new StringBuilder();
        for (Column column : classInfo.getFieldList()) {
            StringBuilder row = new StringBuilder();
            if (column.getFieldComment() != null && !"".equals(column.getFieldComment())) {
                row.append("    /**\n" + "     * ").append(column.getFieldComment()).append("\n").append("     */\n");
            }
            String javaFieldName = StringUtil.lowerCaseFirst(StringUtil.underlineToCamelCase(column.getFieldName()));
            if (!column.getFieldName().equals(javaFieldName)) {
                row.append("    @Column(name = \"").append(column.getFieldName()).append("\")\n");
            }
            row.append("    private ").append(dbTypeToJava(column.getFieldType())).append(" ").append(javaFieldName).append(";\n");
            stringBuffer.append(row);
        }
        tpl = tpl.replaceAll("POJO_FIELD", stringBuffer.toString());
        writeToFile(saveFileName, tpl);
    }

    /**
     * 数据库类型转成Java类型
     *
     * @param type
     * @return
     */
    private static String dbTypeToJava(String type) {
        String t = type.toLowerCase();
        if (!t.contains("char") && !t.contains("text")) {
            if (t.contains("bigint")) {
                return "Long";
            } else if (t.contains("int") || t.contains("bit")) {
                return "Integer";
            } else if (t.contains("double") || t.contains("decimal")) {
                return "Double";
            } else if (t.contains("float")) {
                return "Float";
            } else if (t.contains("date") || !t.contains("time") || !t.contains("year")) {
                return "Date";
            } else {
                return "String";
            }
        } else {
            return "String";
        }
    }

    /**
     * 把字符串写入文件
     *
     * @param filename
     * @param content
     * @throws IOException
     */
    private static void writeToFile(String filename, String content) throws IOException {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            System.out.println("【WARN】File has Exists,filename => {}" + filename);
            return;
        } else {
            System.out.println("【INFO】create file " + filename);
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }
}
