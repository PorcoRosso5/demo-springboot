package com.porco.javassist.mapper;

import com.porco.javassist.DemoJavassistApplication;
import com.porco.javassist.domain.Record;
import com.porco.javassist.domain.Template;
import com.porco.javassist.domain.TemplateField;
import com.porco.javassist.service.ExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = DemoJavassistApplication.class)
class RecordMapperTest {
    @Autowired
    private RecordMapper mapper;

    @Test
    void deleteByPrimaryKey() {
    }

    @Test
    void insert() {
    }

    @Test
    void insertSelective() {
        ExcelService.CLASS_THREAD_LOCAL.set(Template.class);
        Record record = new Record();
        record.setTemplateId(1);
        record.setContent(new Template());
        record.setStationId(1);
        record.setAddTime(LocalDateTime.now());
         mapper.insert(record);
        ExcelService.CLASS_THREAD_LOCAL.remove();

        ExcelService.CLASS_THREAD_LOCAL.set(TemplateField.class);
        Record record1 = new Record();
        record1.setTemplateId(1);
        record1.setContent(new TemplateField());
        record1.setStationId(1);
        record1.setAddTime(LocalDateTime.now());
         mapper.insert(record1);
        ExcelService.CLASS_THREAD_LOCAL.remove();
    }

    @Test
    void selectByPrimaryKey() {
        ExcelService.CLASS_THREAD_LOCAL.set(Template.class);

        Record record = mapper.selectByPrimaryKey(1L);
        System.out.println(record);
        ExcelService.CLASS_THREAD_LOCAL.remove();

        ExcelService.CLASS_THREAD_LOCAL.set(TemplateField.class);

        Record record1 = mapper.selectByPrimaryKey(4L);
        System.out.println(record1);
        ExcelService.CLASS_THREAD_LOCAL.remove();
    }

    @Test
    void updateByPrimaryKeySelective() {
    }

    @Test
    void updateByPrimaryKey() {
    }
}