use collect_data;
CREATE TABLE `run_log_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(255) DEFAULT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `server_name` varchar(255) DEFAULT NULL,
  `dt_time` bigint(20) DEFAULT NULL,
  `content` text,
  `log_level` varchar(255) DEFAULT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `log_type` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `log_level_index` (`log_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `finance_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `soul` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(300) NOT NULL,
  `hits` varchar(100) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1233 DEFAULT CHARSET=utf8 COMMENT='毒鸡汤';

create table collect_data.stock_hl
(
	id int auto_increment
		primary key,
	yinli varchar(50) default '' not null,
	fund_content varchar(255) default '' not null,
	suit varchar(20) default '' not null,
	summary varchar(2000) default '' not null,
	tech_data varchar(255) default '' not null,
	gmt_create datetime not null
)
comment '股市黄历';




create table collect_data.log_data
(
	id int auto_increment
		primary key,
	generate_date varchar(100) default '' not null comment '生成时间',
	content mediumtext null,
	log_type varchar(10) default '' not null comment '日志类型'
)
comment '日志数据';



create table collect_data.hot_topic
(
	id bigint auto_increment
		primary key,
	title varchar(255) default '' not null,
	url varchar(255) default '' not null,
	create_time datetime not null,
	source varchar(50) default '' not null comment '来源'
)
comment '热点话题';


CREATE TABLE logging_event
  (
    timestmp         BIGINT NOT NULL,
    formatted_message  TEXT NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL,
    event_id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
  );

CREATE TABLE logging_event_property
  (
    event_id	      BIGINT NOT NULL,
    mapped_key        VARCHAR(254) NOT NULL,
    mapped_value      TEXT,
    PRIMARY KEY(event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );

CREATE TABLE logging_event_exception
  (
    event_id         BIGINT NOT NULL,
    i                SMALLINT NOT NULL,
    trace_line       VARCHAR(254) NOT NULL,
    PRIMARY KEY(event_id, i),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );
