package com.porco.javassist.mapper;

import com.porco.javassist.domain.Record;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【record(数据表)】的数据库操作Mapper
 * @createDate 2023-10-04 04:14:59
 * @Entity com.porco.javassist.domain.Record
 */
public interface RecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Record record);

    int insertBatch(@Param("list") List<Record> record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    List<Record> selectAllByTemplateIdAndContentDate(@Param("templateId") Long templateId, @Param("date") String date);

    List<Record> selectAllByTemplateIdBetweenContentDate(@Param("templateId") Long templateId, @Param("start") String start, @Param("end") String end);

    List<Record> selectAllByTemplateIdAndContentId(@Param("templateId") Long templateId, @Param("id") Integer id);

}
