package com.porco.javassist.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户自定义字段表
 *
 * @TableName customer_field
 */
@Data
public class CustomerField implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private Long templateId;
    /**
     * 字段id
     */
    private Long fieldId;
    /**
     * 字段名称
     */
    private String filedName;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}