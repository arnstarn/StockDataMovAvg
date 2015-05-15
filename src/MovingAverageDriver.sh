#!/usr/bin/env bash

#CNF="./main/scripts//stock_db.cnf"

#Truncate DB Tables
#/usr/local/bin/mysql --defaults-extra-file=$CNF stock_db -e "set FOREIGN_KEY_CHECKS=0; truncate stock; truncate mvg_avg"
#Download HISTORICAL DATA FOR S&P500 index
#./main/scripts/getIndexData.sh ./main/scripts/sp500.csv

#HADOOP JOB
#######################################################
hdfs dfs -rm -r output
hdfs dfs -rm -r data
hdfs dfs -copyFromLocal ./main/data data

#JAVA_HOME=`/usr/libexec/java_home -v 1.6`

hadoop jar ../target/StockDataMovAvg-1.0.jar \
  com.StockData.MovingAverage.MovingAverageJob \
  /user/arnstarn/data \
  /user/arnstarn/output

rm -rf ./main/resultdata/output
hdfs dfs -copyToLocal output ./main/resultdata
#######################################################

#Put HADOOP Results in MySQL DB
#./main/scripts/putTickerMovingData.sh

