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
