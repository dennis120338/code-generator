package com.laohand.generator.pojo;

import java.util.List;

/**
 * @author dennis
 */
public class ClassInfo {
    private String tableName;
    private String className;
    private String classNameLowerFirst;
    private String classComment;
    private String moduleName;
    private String packageName;
    private String baseSavePath;
    private List<Column> fieldList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNameLowerFirst() {
        return classNameLowerFirst;
    }

    public void setClassNameLowerFirst(String classNameLowerFirst) {
        this.classNameLowerFirst = classNameLowerFirst;
    }

    public String getClassComment() {
        return classComment;
    }

    public void setClassComment(String classComment) {
        this.classComment = classComment;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBaseSavePath() {
        return baseSavePath;
    }

    public void setBaseSavePath(String baseSavePath) {
        this.baseSavePath = baseSavePath;
    }

    public List<Column> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Column> fieldList) {
        this.fieldList = fieldList;
    }
}