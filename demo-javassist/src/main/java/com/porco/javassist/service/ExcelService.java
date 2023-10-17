package com.porco.javassist.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.porco.javassist.domain.CustomerField;
import com.porco.javassist.domain.Record;
import com.porco.javassist.domain.Template;
import com.porco.javassist.mapper.CustomerFieldMapper;
import com.porco.javassist.mapper.RecordMapper;
import com.porco.javassist.mapper.TemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.porco.javassist.dynamic.ClassCreator.FUNCTION;

@Slf4j
@Service
public class ExcelService {
    public static final ThreadLocal<Class<?>> CLASS_THREAD_LOCAL = new ThreadLocal<>();
    private final TemplateMapper templateMapper;
    private final RecordMapper recordMapper;
    private final CustomerFieldMapper customerFieldMapper;

    public ExcelService(TemplateMapper templateMapper, RecordMapper recordMapper, CustomerFieldMapper customerFieldMapper) {
        this.templateMapper = templateMapper;
        this.recordMapper = recordMapper;
        this.customerFieldMapper = customerFieldMapper;
    }

    private static List<List<String>> getHeadList(List<CustomerField> fields) {
        List<List<String>> list = new ArrayList<>();
        for (CustomerField field : fields) {
            List<String> head = new ArrayList<>();
            head.add(field.getBusinessName());
            list.add(head);
        }
        return list;
    }

    private static List<Object> getDataList(Long templateId, List<CustomerField> fields, List<Record> records) throws NoSuchFieldException, IllegalAccessException {
        List<Object> list = new ArrayList<>();
        for (Record record : records) {
            if (fields.isEmpty()) {
                list.add(record.getContent());
            } else {
                Object content = record.getContent();
                List<Object> data = new ArrayList<>();
                // offer by Template method whose return_type is List<Object>
                for (CustomerField field : fields) {
                    Function<Object, Object> function = FUNCTION.getOrDefault(templateId + "-" + field.getFiledName(), i -> null);
                    data.add(function.apply(content));
                }
                // Class<?> aClass = content.getClass();
                // for (CustomerField field : fields) {
                //     Field declaredField = aClass.getDeclaredField(field.getFiledName());
                //     declaredField.setAccessible(true);
                //     Object o = declaredField.get(content);
                //     data.add(o);
                // }
                list.add(data);
            }
        }
        return list;
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
        // String fileName = template.getLocation() + File.separator + template.getTemplateName();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, clz, new ReadListener<Object>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 1000;
            /**
             *临时存储
             */
            private List<Object> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(Object data, AnalysisContext context) {
                // log.info("读取到一条数据{}", JSON.toJSONString(data));
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
                if (cachedDataList.isEmpty()) return;
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
    public void simpleWrite(Long templateId, Class<?> clz, String start, String end) throws NoSuchFieldException, IllegalAccessException {
        CLASS_THREAD_LOCAL.set(clz);
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        // since: 3.0.0-beta1
        Template template = templateMapper.selectByPrimaryKey(templateId);

        String fileName = template.getLocation() + File.separator + "download-" + template.getTemplateName() + ".xlsx";
        String zhimaid = "1";
        List<CustomerField> fields = customerFieldMapper.selectAllByZhimaidAndTemplateId(zhimaid, templateId);
        List<Record> records = recordMapper.selectAllByTemplateIdBetweenContentDate(templateId, start, end);
        List<Object> results;
        if (records.isEmpty()) {
            results = Collections.emptyList();
        } else {
            results = getDataList(templateId, fields, records);
        }

        if (fields.isEmpty()) {
            EasyExcel.write(fileName, clz)
                    .sheet("模板")
                    .doWrite(results);
        } else {
            EasyExcel.write(fileName)
                    .head(getHeadList(fields))
                    .sheet("模板")
                    .doWrite(results);
        }

        CLASS_THREAD_LOCAL.remove();
    }
}
