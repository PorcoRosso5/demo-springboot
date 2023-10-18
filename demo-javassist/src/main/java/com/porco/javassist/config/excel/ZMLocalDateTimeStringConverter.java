package com.porco.javassist.config.excel;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;

/**
 * Custom LocalDateTime Parser from String
 *
 * @author porco
 * @date 2023/10/18 11:46
 **/
public class ZMLocalDateTimeStringConverter implements Converter<LocalDateTime> {
    public static final String DATE_FORMAT_20 = "yyyy-M-d HH:mm";
    public static final String DATE_FORMAT_21 = "yyyy-M-d HH:mm:ss";

    public static String switchDateFormat(String dateString) {
        if (!dateString.contains("-")) throw new RuntimeException("年月日必须使用符号/或-分隔" + dateString);
        String[] strings = dateString.split(" ");
        if (strings.length != 2) throw new RuntimeException("日期与时间应有一个空格分开" + dateString);
        String time = strings[1];
        final int[] colonCount = {0};
        time.chars().forEach(c -> {
            if (c == ':') colonCount[0]++;
        });
        switch (colonCount[0]) {
            case 1:
                return DATE_FORMAT_20;
            case 2:
                return DATE_FORMAT_21;
            default:
                throw new IllegalArgumentException("can not find date format for：" + dateString);
        }
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) throws ParseException {
        String stringValue = cellData.getStringValue().replace("/", "-");

        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return DateUtils.parseLocalDateTime(stringValue, switchDateFormat(stringValue), globalConfiguration.getLocale());
        } else {
            return DateUtils.parseLocalDateTime(cellData.getStringValue(),
                    contentProperty.getDateTimeFormatProperty().getFormat(), globalConfiguration.getLocale());
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new WriteCellData<>(DateUtils.format(value, null, globalConfiguration.getLocale()));
        } else {
            return new WriteCellData<>(
                    DateUtils.format(value, contentProperty.getDateTimeFormatProperty().getFormat(),
                            globalConfiguration.getLocale()));
        }
    }
}

