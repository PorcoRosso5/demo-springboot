package com.porco.javassist.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.porco.javassist.domain.Record;
import com.porco.javassist.domain.Template;
import com.porco.javassist.mapper.RecordMapper;
import com.porco.javassist.mapper.TemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelService {
    public static final ThreadLocal<Class<?>> CLASS_THREAD_LOCAL = new ThreadLocal<>();
    private final TemplateMapper templateMapper;
    private final RecordMapper recordMapper;

    public ExcelService(TemplateMapper templateMapper, RecordMapper recordMapper) {
        this.templateMapper = templateMapper;
        this.recordMapper = recordMapper;
    }

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link com.porco.javassist.domain.PayRecord}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    public void read(Integer stationId, Long templateId, Class<?> clz) {
        CLASS_THREAD_LOCAL.set(clz);
        Template template = templateMapper.selectByPrimaryKey(templateId);

        // 匿名内部类 不用额外写一个DemoDataListener
        String fileName = template.getLocation() + File.separator + template.getTemplateName() + ".xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, clz, new ReadListener<Object>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 10;
            /**
             *临时存储
             */
            private List<Object> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(Object data, AnalysisContext context) {
                log.info("读取到一条数据{}", JSON.toJSONString(data));
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                List<Record> list = cachedDataList.stream()
                        .map(e -> Record.builder()
                                .templateId(templateId)
                                .content(e)
                                .stationId(stationId)
                                .addTime(LocalDateTime.now())
                                .build())
                        .collect(Collectors.toList());
                recordMapper.insertBatch(list);
                log.info("存储数据库成功！");
            }
        }).sheet().doRead();
        CLASS_THREAD_LOCAL.remove();
    }

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 直接写即可
     */
    public void simpleWrite(Long templateId, Class<?> clz, String start, String end) {
        CLASS_THREAD_LOCAL.set(clz);
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        // since: 3.0.0-beta1
        Template template = templateMapper.selectByPrimaryKey(templateId);

        String fileName = template.getLocation() + File.separator + "download-" + template.getTemplateName() + ".xlsx";

        List<Record> records = recordMapper.selectAllByTemplateIdBetweenContentDate(templateId, start, end);
        List<Object> results;
        if (!records.isEmpty()) {
            results = records.stream().map(Record::getContent).collect(Collectors.toList());
        } else {
            results = Collections.emptyList();
        }

        // 写法2
        EasyExcel.write(fileName, clz).sheet("模板").doWrite(results);

        // 写法3
        // fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        // try (ExcelWriter excelWriter = EasyExcel.write(fileName, clz).build()) {
        //     WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        //     excelWriter.write(ArrayList::new, writeSheet);
        // }
        CLASS_THREAD_LOCAL.remove();
    }
}
