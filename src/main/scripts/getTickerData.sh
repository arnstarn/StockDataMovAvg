#!/bin/bash

DIR="./../data"
DIRDATA=$(pwd $0)"/main/data"
DIRSCRIPTS=$(pwd $0)"/main/scripts"

#DATE=`date +%Y-%m-%d:%H:%M:%S`
DATEFROM=$(echo $1 | sed 's/[^0-9]*//g')
DATETO=$(echo $2 | sed 's/[^0-9]*//g')

shift 2

for SYMBOL in $@
do
	URL="http://ichart.finance.yahoo.com/table.csv?s=$SYMBOL&a=${DATEFROM:0:2}&b=${DATEFROM:2:2}&c=${DATEFROM:4}&d=${DATETO:0:2}&e=${DATETO:2:2}&f=${DATETO:4}&g=d&ignore=.csv"
	#echo $URL

	curl -s $URL | tail -n +2 > "$DIRDATA/$SYMBOL.csv" #tail to skip first line
	#curl -s $URL | sed '1d' > "$DIR/$SYMBOL.csv" #sed to skip first line
	#wget -q $URL > "$DIR/DATA/$SYMBOL.csv"
	#$DIR/getURL.py $URL > "$DIR/DATA/$SYMBOL.csv"
done
