package com.porco.javassist.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 模板字段表
 * @TableName template_field
 */
@Data
public class TemplateField implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板id，表示这个字段属于哪个模板
     */
    private Long templateId;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 前端中文名称
     */
    private String businessName;

    /**
     * 这个字段是否为可筛选项
     */
    private Integer selected;

    private static final long serialVersionUID = 1L;
}