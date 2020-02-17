package com.laohand.generator.pojo;

import lombok.Data;

/**
 * @author dennis
 */
@Data
public class Column {
    private String fieldName;
    private String fieldType;
    private String fieldComment;
}
