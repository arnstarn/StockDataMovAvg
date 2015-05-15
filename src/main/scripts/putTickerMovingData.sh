#!/usr/bin/env bash

DIR=$(pwd $0)"/main/resultdata/output"

MYDIR=$(pwd $0)"/main/scripts"
PATH="$DIR/part-00000"
CNF="$MYDIR/stock_db.cnf"

echo "Uploading Moving Avg Data"

/usr/local/bin/mysql --defaults-extra-file=$CNF stock_db -e "set FOREIGN_KEY_CHECKS=0; load data infile '$PATH' ignore into table mvg_avg FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' (symbol, date, mvg_avg, upper_band, lower_band, std_dv)"

echo "Finished Importing Avg Data Table"
