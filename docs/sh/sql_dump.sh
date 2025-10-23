#!/bin/bash
databases=(sagittarius_system sagittarius_article)
i=6
for db in ${databases[@]} ; do
    echo "  正在导出 [$db] 数据库..."
    echo "USE \`$db\`;" >> ../db/0${i}_${db}.sql
    echo "" >> ../db/0${i}_${db}.sql

    # 导出表结构和数据
    docker exec sagittarius-mysql mysqldump --socket=/tmp/mysqld.sock -uroot -proot --skip-comments --add-drop-table --skip-extended-insert $db >> ../db/0${i}_${db}.sql

    # 添加尾部信息
    echo "" >> ../db/0${i}_${db}.sql
    echo "SET FOREIGN_KEY_CHECKS = 1;" >> ../db/0${i}_${db}.sql

    i=$(($i+1))
done
echo "导出数据库SQL完成..."