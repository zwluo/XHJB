-- 故事表
drop table tale;
create table tale
(
    id         Int primary key auto_increment comment '编号',
    type      varchar(50) comment '栏目分类',
    title       varchar(50) comment '标题',
    content    text comment '内容',
    narrator   varchar(20) comment '口述人',
    recorder    varchar(20) comment '搜集人',
    createdate datetime comment '创建时间',
    createby   varchar(20) comment '创建人'
);

-- 访问日志表
drop table visit_log
create table visit_log
(
    id         Int primary key auto_increment comment '编号',
    method      varchar(50) comment '处理方法',
    request      varchar(50) comment '请求参数',
    response       varchar(50) comment '接口返回',
    ip      varchar(50) comment '访问者IP',
    createdate datetime comment '创建时间' default now(),
    createby   varchar(20) comment '创建人' default 'system'
);

-- 反馈表
create table feedback
(
    id         Int primary key auto_increment comment '编号',
    type      varchar(50) comment '栏目分类',
    title       varchar(50) comment '标题',
    content    text comment '反馈内容',
    ip      varchar(50) comment '访问者IP',
    createdate TIMESTAMP comment '创建时间' default now(),
    createby   varchar(20) comment '创建人' default 'system'
);