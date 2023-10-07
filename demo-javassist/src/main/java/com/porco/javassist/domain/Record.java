package com.porco.javassist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据表
 *
 * @TableName record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 模板id
     */
    private Long templateId;
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
    private LocalDateTime addTime;
}