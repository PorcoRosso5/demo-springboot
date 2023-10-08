package com.porco.javassist.mapper;

import com.porco.javassist.domain.UploadRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【upload_record(上传记录表)】的数据库操作Mapper
* @createDate 2023-10-04 04:14:59
* @Entity com.porco.javassist.domain.UploadRecord
*/
@Mapper
public interface UploadRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UploadRecord record);

    int insertSelective(UploadRecord record);

    UploadRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UploadRecord record);

    int updateByPrimaryKey(UploadRecord record);

}
