package com.porco.javassist.mapper;
import java.util.List;

import com.porco.javassist.domain.Template;

/**
* @author Admin
* @description 针对表【template(模板表)】的数据库操作Mapper
* @createDate 2023-10-04 04:14:59
* @Entity com.porco.javassist.domain.Template
*/
public interface TemplateMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Template record);

    int insertSelective(Template record);

    Template selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Template record);

    int updateByPrimaryKey(Template record);

    List<Template> selectAll();

}
