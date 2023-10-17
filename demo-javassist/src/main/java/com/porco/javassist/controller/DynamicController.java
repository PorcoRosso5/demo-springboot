package com.porco.javassist.controller;

import com.porco.javassist.domain.Record;
import com.porco.javassist.domain.Result;
import com.porco.javassist.mapper.RecordMapper;
import com.porco.javassist.service.ExcelService;
import com.porco.javassist.util.LcWorker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DynamicController {

    @Resource
    private ExcelService excelService;

    @Resource
    private RecordMapper mapper;

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
        excelService.read(1, id, clz);
        return Result.of(o);
    }

    @GetMapping("/download/{id}")
    public Result download(@PathVariable("id") Long id) throws Exception {
        Class<?> clz = Class.forName("com.porco.javassist.domain.Template$" + id);

        Object o = clz.newInstance();
        LcWorker.work(o);
        excelService.simpleWrite(id, clz, "2022-08-01 17:00:00", "2023-08-31 20:00:00");
        return Result.of(o);
    }

    @GetMapping("/list/{id}")
    public Result dataList(@PathVariable("id") Long id) throws Exception {
        Class<?> clz = Class.forName("com.porco.javassist.domain.Template$" + id);
        ExcelService.CLASS_THREAD_LOCAL.set(clz);

        List<Record> records = mapper.selectAllByTemplateIdBetweenContentDate(id, "2022-08-19 00:00:00", "2022-08-20 00:00:00");

        ExcelService.CLASS_THREAD_LOCAL.remove();
        return Result.of(records);
    }

    @GetMapping("/test/{id}")
    public Result test(@PathVariable("id") Long id) throws Exception {
        Class<?> clz = Class.forName("com.porco.javassist.domain.Template$" + id);
        ExcelService.CLASS_THREAD_LOCAL.set(clz);

//        Object o = clz.newInstance();
//        Record record = new Record();
//        record.setTemplateId(1);
//        record.setContent(o);
//        record.setStationId(1);
//        record.setAddTime(LocalDateTime.now());
//        mapper.insert(record);
//        return Result.of(o);

        List<Record> records = mapper.selectAllByTemplateIdAndContentDate(1L, "2022-08-20 18:36:00");
        return Result.of(records);
    }
}
