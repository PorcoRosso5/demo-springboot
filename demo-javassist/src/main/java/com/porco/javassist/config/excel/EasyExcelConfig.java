package com.porco.javassist.config.excel;

import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.converters.DefaultConverterLoader;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * EasyExcel config
 *
 * @author porco
 * @date 2023/10/18 13:21
 **/
@Configuration
public class EasyExcelConfig {

    @PostConstruct
    public void customConverter() {
        ZMLocalDateTimeStringConverter converter = new ZMLocalDateTimeStringConverter();
        DefaultConverterLoader.loadDefaultReadConverter()
                .put(ConverterKeyBuild.buildKey(converter.supportJavaTypeKey(), converter.supportExcelTypeKey()), converter);

    }
}
