#!/bin/bash
echo "开始导出数据库SQL..."
echo "  正在导出 [sagittarius] 数据库..."
docker exec sagittarius-mysql mysqldump --socket=/tmp/mysqld.sock -uroot -proot sagittarius > ../db/05_sagittarius.sql
echo "导出数据库SQL完成..."