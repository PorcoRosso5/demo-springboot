package com.porco.javassist.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 数据表
 * @TableName record
 */
@Data
public class Record implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板id
     */
    private Integer templateId;

    /**
     * 记录的json数据
     */
    private Object content;

    /**
     * 油站id,原油站id长度
     */
    private Integer stationId;

    /**
     * 记录添加时间
     */
    private Date addTime;

    private static final long serialVersionUID = 1L;
}