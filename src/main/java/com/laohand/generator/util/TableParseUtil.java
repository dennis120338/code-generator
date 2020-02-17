package com.laohand.generator.util;


import com.laohand.generator.util.StringUtil;
import com.laohand.generator.pojo.ClassInfo;
import com.laohand.generator.pojo.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将创建表的sql字符串解析成为对象
 *
 * @author dennis
 */
public class TableParseUtil {
    private TableParseUtil() {
    }

    /**
     * 解析sql
     *
     * @param sql String
     * @return ClassInfo
     */
    public static ClassInfo parseSql(String sql) {
        List<Column> fieldList = new ArrayList<>();
        ClassInfo classInfo = new ClassInfo();
        //匹配整个ddl，将ddl分为表名，列sql部分，表注释
        String ddlPatternString = "\\s*create\\s+table\\s+(?<tableName>\\S+)[^\\(]*\\((?<columnsSQL>[\\s\\S]+)\\)[^\\)]+?(comment\\s*(=|on\\s+table)\\s*'(?<tableComment>.*?)'\\s*;?)?$";
        Pattern ddlPattern = Pattern.compile(ddlPatternString, Pattern.CASE_INSENSITIVE);
        //匹配列sql部分，分别解析每一列的列名 类型 和列注释
        String colPatternString = "\\s*(?<fieldName>\\S+)\\s+(?<fieldType>\\w+)\\s*(?:\\([\\s\\d,]+\\))?((?!comment).)*(comment\\s*'(?<fieldComment>.*?)')?\\s*(,|$)";
        Pattern colPattern = Pattern.compile(colPatternString, Pattern.CASE_INSENSITIVE);

        Matcher matcher = ddlPattern.matcher(sql);
        if (matcher.find()) {
            String tableName = matcher.group("tableName");
            String tableComment = matcher.group("tableComment");
            classInfo.setTableName(tableName.replaceAll("'", "").replaceAll("`", ""));
            classInfo.setClassName(StringUtil.upperCaseFirst(StringUtil.underlineToCamelCase(classInfo.getTableName())));
            classInfo.setClassNameLowerFirst(StringUtil.lowerCaseFirst(StringUtil.underlineToCamelCase(classInfo.getTableName())));
            if (tableComment != null) {
                classInfo.setClassComment(tableComment.replaceAll("'", ""));
            } else {
                classInfo.setClassComment("");
            }
            String columnSql = matcher.group("columnsSQL");
            if (columnSql != null && columnSql.length() > 0) {
                Matcher colMatcher = colPattern.matcher(columnSql);
                while (colMatcher.find()) {
                    String fieldName = colMatcher.group("fieldName").replaceAll("`", "");
                    String fieldType = colMatcher.group("fieldType");
                    String fieldComment = colMatcher.group("fieldComment");
                    if (!"key".equalsIgnoreCase(fieldType)) {
                        Column column = new Column();
                        column.setFieldName(fieldName.replaceAll("'", ""));
                        column.setFieldType(fieldType.replaceAll("'", ""));
                        if (fieldComment != null) {
                            column.setFieldComment(fieldComment.replaceAll("'", ""));
                        } else {
                            column.setFieldComment("");
                        }
                        fieldList.add(column);
                    }
                }
            }
            classInfo.setFieldList(fieldList);
        }
        return classInfo;
    }

}
