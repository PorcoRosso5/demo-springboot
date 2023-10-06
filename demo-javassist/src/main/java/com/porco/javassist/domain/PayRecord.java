package com.porco.javassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayRecord {
    /**
     * 1 时间
     */
    @ExcelProperty
    private LocalDateTime date;
    /**
     * 2 油站
     */
    private String stationName;
    /**
     * 3账号
     */
    private String account;
    /**
     * 4 支付工具
     */
    private String payWay;
    /**
     * 5 商户单号
     */
    private String no;
    /**
     * 6 权益销售(元)
     */
    private BigDecimal memberAmount;
    /**
     * 7 实收额(元)
     */
    private BigDecimal actualAmount;
    /**
     * 8 优惠额（元）
     */
    private BigDecimal discountAmount;
    /**
     * 9 交易类型
     */
    private String payType;
}
