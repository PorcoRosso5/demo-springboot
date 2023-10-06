CREATE DATABASE `test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */

create table if not exists customer_field
(
    id          bigint auto_increment comment '主键id'
    primary key,
    zhimaid     varchar(32)                        not null comment '用户id',
    template_id varchar(32)                        not null comment '模版id',
    field_id    varchar(32)                        not null comment '字段id',
    filed_name  varchar(32)                        not null comment '字段名称',
    add_time    datetime default CURRENT_TIMESTAMP null comment '添加时间',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间'
    )
    comment '用户自定义字段表';

create table if not exists record
(
    id          bigint auto_increment comment '主键id'
    primary key,
    template_id int                                not null comment '模板id',
    content     json                               not null comment '记录的json数据',
    station_id  int                                not null comment '油站id,原油站id长度',
    add_time    datetime default CURRENT_TIMESTAMP not null comment '记录添加时间'
)
    comment '数据表';

create table if not exists template
(
    id                   bigint auto_increment comment '主键id'
    primary key,
    template_name        varchar(32)  not null comment '模板名称',
    template_description varchar(64)  not null comment '模板描述',
    location             varchar(255) not null comment '模板在服务器文件系统的位置',
    station_id           int          not null comment '油站id，一个模板基本上是一个油站在用',
    constraint unq_location_template_name
    unique (location, template_name)
    )
    comment '模板表';

create table if not exists template_field
(
    id            bigint auto_increment comment '主键id'
    primary key,
    template_id   int                  not null comment '模板id，表示这个字段属于哪个模板',
    field_type    varchar(32)          not null comment '字段类型',
    field_name    varchar(32)          not null comment '字段名称',
    business_name varchar(32)          not null comment '前端中文名称',
    selected      tinyint(1) default 0 null comment '这个字段是否为可筛选项'
    )
    comment '模板字段表';

create table if not exists upload_record
(
    id            bigint auto_increment comment '主键id'
    primary key,
    station_id    int                                not null comment '油站id，表明这个上传记录属于哪个油站',
    operator_id   varchar(32)                        not null comment '操作员（员工）id,表明这个id由哪个操作员上传',
    operator_name varchar(32)                        not null comment '操作员姓名',
    template_id   int                                not null comment '模板id，表明这个上传的文件用的是哪个模板',
    location      varchar(255)                       not null comment '上传的文件在服务器文件系统的位置',
    upload_time   datetime default CURRENT_TIMESTAMP not null comment '当前文件的上传时间',
    constraint location
    unique (location)
    )
    comment '上传记录表';

-- template
1,20220818-20220819权益销售交易流水,20220818-20220819权益销售交易流水,C:\Users\Admin\Downloads,1
2,20220818-20220819权益核销交易流水,20220818-20220819权益核销交易流水,C:\Users\Admin\Downloads,1

-- template_field
1,1,java.time.LocalDateTime,date,时间,1
2,1,java.lang.String,stationName,油站,0
3,1,java.lang.String,account,账号,0
4,1,java.lang.String,payWay,支付工具,0
5,1,java.lang.String,no,商户单号,0
6,1,java.math.BigDecimal,equityAmount,权益销售(元),0
7,1,java.math.BigDecimal,actualAmount,实收额(元),0
8,1,java.math.BigDecimal,discountAmount,优惠额（元）,0
9,1,java.lang.String,payType,交易类型,0
10,2,java.time.LocalDateTime,date,时间,1
11,2,java.lang.String,stationName,油站,0
12,2,java.lang.String,account,账号,0
13,2,java.lang.String,payWay,支付工具,0
14,2,java.lang.String,no,商户单号,0
15,2,java.lang.Integer,gunNo,油枪,0
16,2,java.lang.String,oilType,油品,0
17,2,java.math.BigDecimal,price,挂牌价(元/升),0
18,2,java.math.BigDecimal,volume,售出油量(升),0
19,2,java.math.BigDecimal,transactionAmount,交易额(元),0
20,2,java.math.BigDecimal,actualAmount,实收额(元),0
21,2,java.math.BigDecimal,discountAmount,优惠额(元),0
22,2,java.math.BigDecimal,writeOffAmount,核销额(元),0
23,2,java.lang.String,payType,交易类型,0


