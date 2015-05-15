#!/bin/bash

echo "Getting Index Data"

DIRDATA=$(pwd $0)"/main/data"
DIRSCRIPTS=$(pwd $0)"/main/scripts"
TCDT="getTickerData.sh"
CNF="$DIRSCRIPTS/stock_db.cnf"

[ ! -f "$DIRSCRIPTS/$TCDT" ] && echo "$DIRSCRIPTS/$TCDT doesn't exist!" && exit 1

if [[ $# -gt 2 ]]; then
	#DATE=`date +%Y-%m-%d:%H:%M:%S`
	DATEFROM=$(echo $1 | sed 's/[^0-9]*//g')
	DATETO=$(echo $2 | sed 's/[^0-9]*//g')
	shift 2
else
	#statements
	DATETO=$(date +%m%d%Y)
	DATEFROM=$(date -v -10y +%m%d%Y)
	if [[ $# -eq 1 ]]; then
		FILE="$1"
	elif [[ $# -eq 3 ]]; then
		FILE="$3"
	else
		FILE="$DIRSCRIPTS/sp500.csv"
		[ ! -f "$FILE" ] && echo "sp500.csv doesn't exist!" && exit 1		
	fi
fi

# echo $FILE
# echo $DATEFROM
# echo $DATETO


FAIL=0

while IFS=',' read -r f1 f2 f3 f4 f5
do
	CMD="$DIRSCRIPTS/$TCDT $DATEFROM $DATETO $f1"
	$CMD &
	sleep .05 #gets really slow when market is open so use it

  	#echo "$f1"
done < "$FILE"

for job in `jobs -p`
do
	#echo $job
    wait $job || let "FAIL+=1"
done

echo "THREAD FAILS: $FAIL"

while IFS=',' read -r f1 f2 f3 f4 f5
do
	CMD="$DIRSCRIPTS/putTickerData.sh $f1"
	$CMD
done < "$FILE"
