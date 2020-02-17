package com.laohand.generator.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author dennis
 */
@Data
public class ClassInfo {
    private String tableName;
    private String className;
    private String classNameLowerFirst;
    private String classComment;
    private String moduleName;
    private String packageName;
    private String baseSavePath;
    private List<Column> fieldList;
}