package com.porco.javassist.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户自定义字段表
 * @TableName customer_field
 */
@Data
public class CustomerField implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private String zhimaid;

    /**
     * 模版id
     */
    private String templateId;

    /**
     * 字段id
     */
    private String fieldId;

    /**
     * 字段名称
     */
    private String filedName;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}