package com.porco.javassist.mapper;

import com.porco.javassist.domain.Record;

/**
* @author Admin
* @description 针对表【record(数据表)】的数据库操作Mapper
* @createDate 2023-10-04 04:14:59
* @Entity com.porco.javassist.domain.Record
*/
public interface RecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

}
