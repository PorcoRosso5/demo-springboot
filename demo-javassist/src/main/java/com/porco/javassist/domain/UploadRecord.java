package com.porco.javassist.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 上传记录表
 * @TableName upload_record
 */
@Data
public class UploadRecord implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 油站id，表明这个上传记录属于哪个油站
     */
    private Integer stationId;

    /**
     * 操作员（员工）id,表明这个id由哪个操作员上传
     */
    private String operatorId;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 模板id，表明这个上传的文件用的是哪个模板
     */
    private Integer templateId;

    /**
     * 上传的文件在服务器文件系统的位置
     */
    private String location;

    /**
     * 当前文件的上传时间
     */
    private Date uploadTime;

    private static final long serialVersionUID = 1L;
}