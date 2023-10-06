package com.porco.javassist.service;

import com.porco.javassist.DemoJavassistApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoJavassistApplication.class)
class ExcelServiceTest {
    @Autowired
    private ExcelService service;

    @Test
    void simpleRead() {
        service.simpleRead();
    }
}