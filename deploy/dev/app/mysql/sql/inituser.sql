-- # 1、初始化数据库：创建nacos外接数据库frameworkjava_nacos_dev和脚手架业务数据库frameworkjava_dev
-- # 2、创建用户，用户名：zyhdev 密码：zyh@123
-- # 3、授予zyhdev用户特定权限

CREATE database if NOT EXISTS `frameworkjava_nacos_dev` default character set utf8mb4 collate utf8mb4_general_ci;
CREATE database if NOT EXISTS `frameworkjava_dev` default character set utf8mb4 collate utf8mb4_general_ci;

CREATE USER 'zyhdev'@'%' IDENTIFIED BY 'zyh@123';
grant replication slave, replication client on *.* to 'zyhdev'@'%';

GRANT ALL PRIVILEGES ON frameworkjava_nacos_dev.* TO  'zyhdev'@'%';
GRANT ALL PRIVILEGES ON frameworkjava_dev.* TO  'zyhdev'@'%';

FLUSH PRIVILEGES;
