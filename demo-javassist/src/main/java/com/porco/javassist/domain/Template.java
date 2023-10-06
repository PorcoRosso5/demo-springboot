package com.porco.javassist.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 模板表
 * @TableName template
 */
@Data
public class Template implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板描述
     */
    private String templateDescription;

    /**
     * 模板在服务器文件系统的位置
     */
    private String location;

    /**
     * 油站id，一个模板基本上是一个油站在用
     */
    private Integer stationId;

    private static final long serialVersionUID = 1L;
}