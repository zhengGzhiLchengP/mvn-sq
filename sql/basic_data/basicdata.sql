use basic_data;

CREATE TABLE `bank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) NOT NULL COMMENT '银行名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 工商银行、建设银行、农业银行、中国银行、交通银行、邮政储蓄、平安银行、兴业银行、中信银行、浦发银行、光大银行、民生银行、招商银行、江苏银行、华夏银行、北京银行、宁波银行、广发银行、农商银行、南京银行、渤海银行、广州银行、上海银行、华润银行

CREATE TABLE `cpu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `basic_pinlv` double DEFAULT NULL COMMENT '基础频率',
  `core_num` int(11) DEFAULT NULL COMMENT '核心数',
  `thread_num` int(11) DEFAULT NULL COMMENT '线程数',
  `ludashi_score` double DEFAULT NULL COMMENT '鲁大师跑的分数',
  `sdmark_score` double DEFAULT NULL COMMENT '3dmark的分数',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `power` double(255,0) DEFAULT NULL COMMENT '功率 瓦数',
  `nami` int(255) DEFAULT NULL COMMENT '多少纳米',
  `rui_pinlv` double(255,0) DEFAULT NULL COMMENT '睿频',
  `super_pinlv` double(255,0) DEFAULT NULL COMMENT '超频',
  `main_board` varchar(255) DEFAULT NULL COMMENT '推荐主板',
  `memory_type` varchar(255) DEFAULT NULL COMMENT '内存类型',
  `interface_type` varchar(255) DEFAULT NULL COMMENT '接口类型 NTEL LGA1200...',
  `extra_info` varchar(255) DEFAULT NULL COMMENT '额外信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table basic_data.holiday
(
	id int auto_increment
		primary key,
	name varchar(50) default '' null comment '节日名称',
	gmt_start datetime null comment '开始时间',
	gmt_end datetime not null,
	gmt_create datetime not null comment '创建时间',
	remark varchar(100) default '' not null comment '备注'
)
comment '节假日';



create table basic_data.shell_script
(
	id int auto_increment
		primary key,
	bash text null comment '脚本详细内容',
	remark varchar(255) default '' not null comment '备注',
	type varchar(50) default '' not null comment '类型',
	dictionary varchar(50) default '' not null comment '目录',
	gmt_create datetime not null,
	operator varchar(45) default '' not null comment '操作人',
	shell_file_name varchar(50) default '' not null comment 'shell文件名称',
	shell_name varchar(50) default '' not null comment 'shell脚本简称'
)
comment 'shell脚本管理';


create table basic_data.taobao_product
(
	id int auto_increment
		primary key,
	link_url varchar(255) default '' not null,
	product_desc varchar(255) default '' not null comment '商品描述',
	create_time datetime not null,
	tao_kou_lin varchar(50) default '' not null comment '淘口令',
	qrcode varchar(255) default '' not null comment '二维码图片',
	product_img varchar(255) default '' not null comment '产品图片',
	type varchar(40) default '' not null comment '类型',
	provider varchar(50) default '' not null comment '提供商',
	label varchar(50) default '' not null comment '标签'
)
comment '淘宝联盟商品优惠券';





create table basic_data.finance_knowledge
(
	id bigint auto_increment
		primary key,
	question varchar(500) default '' not null,
	answer varchar(500) default '' not null,
	gmt_create datetime not null,
	tags varchar(100) default '' not null comment '标签'
)
comment '金融知识';



create table basic_data.corporation
(
	id int auto_increment
		primary key,
	corporation_name varchar(50) default '' not null,
	valuation varchar(50) default '' not null,
	valuation_date varchar(50) default '' not null,
	country varchar(50) default '' not null,
	industry varchar(50) default '' not null,
	link varchar(100) default '' not null
)
comment '公司';



create table basic_data.xiaomi_city
(
	id int auto_increment
		primary key,
	city_name varchar(50) default '' not null,
	city_num varchar(50) default '' not null
);

create table basic_data.stock_basic
(
	id int auto_increment
		primary key,
	ts_code varchar(50) default '' not null,
	symbol varchar(50) default '' not null,
	name varchar(50) default '' not null,
	area varchar(50) default '' not null,
	industry varchar(50) default '' not null,
	market varchar(50) default '' not null,
	list_date varchar(50) default '' not null
);
create table basic_data.ws_log
(
	id bigint auto_increment
		primary key,
	bytesPerTcpReceive double default 0 not null,
	decodeErrorCount int default 0 not null,
	duration bigint default 0 not null,
	durationType int default 0 not null,
	formated_duration varchar(50) default '' not null,
	handled_bytes bigint default 0 not null,
	handled_costs_per_packet float default 0 not null,
	handled_packet_costs int default 0 not null,
	handled_packets int default 0 not null,
	ip varchar(100) default '' not null,
	packets_per_tcp_receive double default 0 not null,
	received_bytes bigint default 0 not null,
	received_packets int default 0 not null,
	received_tcps int default 0 not null,
	request_count int default 0 not null,
	sent_bytes bigint default 0 not null,
	sent_packets int default 0 not null,
	start varchar(100) default '' not null
)
comment 'webscoket日志';




