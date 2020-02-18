# code-generator
每个程序员都应该要有自己的代码生成器

# 简介
* code-generator是一个适用于Spring、Spring Boot、Spring Cloud通用代码生成的框架
* code-generator库非常小，并且无第三方依赖，可以放心引入项目中而不会同其他包冲突
* 通过该工具可以通过模块生成通用代码，极大提高生产效率
* 通过自定义模板，可以生成php、python、javascript等任何语言通用代码

# 安装方式
maven安装
```xml
<dependency>
  <groupId>com.laohand</groupId>
  <artifactId>code-generator</artifactId>
  <version>1.0.0</version>
</dependency>
```
Gradle 安装
```bash
implementation 'com.laohand:code-generator:1.0.0'
```

# 使用方式
新建类CodeGen，示例代码如下便可生成一个模块代码
```java
 package com.foundao;
 import com.laohand.generator.CodeGenerator;
 import java.io.IOException;
 public class CodeGen {
     public static void main(String[] args) throws IOException {
         String sql = "CREATE TABLE 'user' (\\n\" +\n" +
                 "                \"  'user_id' int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',\\n\" +\n" +
                 "                \"  'username' varchar(255) NOT NULL COMMENT '用户名',\\n\" +\n" +
                 "                \"  'addtime' datetime NOT NULL COMMENT '创建时间',\\n\" +\n" +
                 "                \"  PRIMARY KEY ('user_id')\\n\" +\n" +
                 "                \") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息'\\n\" +\n" +
                 "                \"        ";
         CodeGenerator.gen(sql, "com.laohand", "copyright", "F:\\github\\foundao\\geesee_copyright\\src\\main\\java\\com\\foundao");
     }
 }
```
执行代码自动生成代码如下：
![Code Generator preview](http://cdn.laohand.com/img/code-generator.png "Code Generator preview")
内置模板以Spring Boot + TK mybatis框架为基础，生成controller、service、mapper、pojo等文件，并内置了常见的增删改查操作。
当然，该模板仅适用于作者，可能并不适合大多数开发者。后面通过简单分析代码演示如何自定义模板。

# 代码简析
CodeGenerator.gen()方法存在多个重构，但是最终都会调用以下方法
```java
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
```
替换步骤
* 解析sql文件内容成为classInfo对象，并将替换所需必要信息设置到classInfo中
* 调用createFile方法生成文件
* 调用createFile生成文件是会将模板内的内置变量替换为真实值，模板变量如下：
	- CLASS_NAME_UPPER：pojo文件名称，首字母大写
	- CLASS_NAME_LOWER：pojo文件名称，首字母小写
	- TABLE_NAME：mysql表名称
	- TABLE_COMMENT：mysql表注释
	- PACKAGE：基本包名
	- POJO_FIELD：pojo字段
* 可以将一些其他自定义变量及替换值放入 userReplace中，在createFile时进行替换
* 最后将替换好后的内容写入文件中

# 自定义模板
其实替换原理及代码都很简单，读者简单看看源代码就能明白，这里简单说明一下
## 方法一实现TemplateSource接口
TemplateSource 接口代码如下：
```java
public interface TemplateSource {
    /**
     * 控制器模板
     *
     * @return
     */
    String getControllerTemplate();

    /**
     * pojo模板
     *
     * @return
     */
    String getPojoTemplate();

    /**
     * service接口模板
     *
     * @return
     */
    String getServiceTemplate();

    /**
     * service实现模板
     *
     * @return
     */
    String getServiceImplTemplate();

    /**
     * mapper模板
     *
     * @return
     */
    String getMapperTemplate();


    /**
     * sql生成器模板
     *
     * @return
     */
    String getSqlBuilderTemplate();
}
```
操作步骤
- 将标准代码中需要替换的地方替换成模板变量(CLASS_NAME_UPPER、CLASS_NAME_LOWER等)并形成模板
- 实现TemplateSource接口，在接口实现中返回模板字符串
- 调用CodeGenerator.gen方法时传入自定义templateSource对象(第五个参数)
```java
public static void gen(String sql, String packageName, String moduleName, String savePath, TemplateSource templateSource, Map<String, String> userReplace){}
```

## 方法二用户自己实现gen方法
实现gen方法逻辑并调用createFile替换，该方法可用于生成任一语言代码，一般用方法一既可
