#!/usr/bin/env bash

if [[ $# -ne 1 ]]; then
  echo "Incorrect number of arguments!" && exit 1;
fi

FILE=$1".csv"
DIR=$(pwd $0)"/main/data"
#DIR="../data"

MYDIR=$(pwd $0)"/main/scripts"
PATH="$DIR/$FILE"
SYMBOL=$1

CNF="$MYDIR/stock_db.cnf"

/usr/local/bin/mysql --defaults-extra-file=$CNF stock_db -e "set FOREIGN_KEY_CHECKS=0; load data infile '$PATH' ignore into table stock FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (date, open, high, low, close, volume, adj_price) SET symbol='$SYMBOL'"

