package com.porco.javassist.controller;

import com.porco.javassist.domain.Result;
import com.porco.javassist.service.ExcelService;
import com.porco.javassist.util.LcWorker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("demo")
public class DynamicController {

    @Resource
    private ExcelService excelService;

    @GetMapping("/upload/{id}")
    public Result upload(@PathVariable("id") Long id) throws Exception {
        Class<?> clz = Class.forName("com.porco.javassist.domain.Template$" + id);
        // CtClass ctClass = ClassPool.getDefault().get("com.porco.javassist.domain.Template$" + id);
        // Class<?> clz;
        // try {
        //     clz = ctClass.toClass();
        // } catch (ClassFormatError error) {
        //     clz = Class.forName("com.porco.javassist.domain.Template$" + id);
        // }
        Object o = clz.newInstance();
        LcWorker.work(o);
        excelService.read(id, clz);
        return Result.of(o);
    }

    @GetMapping("/download/{id}")
    public Result download(@PathVariable("id") Long id) throws Exception {
        Class<?> clz = Class.forName("com.porco.javassist.domain.Template$" + id);

        Object o = clz.newInstance();
        LcWorker.work(o);
        excelService.simpleWrite(id, clz);
        return Result.of(o);
    }
}
