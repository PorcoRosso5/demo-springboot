package com.porco.javassist.mapper;

import com.porco.javassist.domain.CustomerField;

/**
* @author Admin
* @description 针对表【customer_field(用户自定义字段表)】的数据库操作Mapper
* @createDate 2023-10-04 04:14:59
* @Entity com.porco.javassist.domain.CustomerField
*/
public interface CustomerFieldMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CustomerField record);

    int insertSelective(CustomerField record);

    CustomerField selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerField record);

    int updateByPrimaryKey(CustomerField record);

}
