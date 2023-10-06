package com.porco.javassist.mapper;

import com.porco.javassist.domain.TemplateField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【template_field(模板字段表)】的数据库操作Mapper
 * @createDate 2023-10-04 04:14:59
 * @Entity com.porco.javassist.domain.TemplateField
 */
public interface TemplateFieldMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TemplateField record);

    int insertSelective(TemplateField record);

    TemplateField selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TemplateField record);

    int updateByPrimaryKey(TemplateField record);

    List<TemplateField> selectAll();

    List<TemplateField> selectAllBySelected(@Param("selected") Integer selected);

    List<TemplateField> selectAllByTemplateId(@Param("templateId") Long templateId);

}
