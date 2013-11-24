#!/bin/bash
AWK=/usr/bin/awk
HOSTS="localhost"
SENDCOUNT=3
FAILEDLIMIT=6

DIR=$(cd $(dirname $0); pwd)
JARDIR_PATH=${DIR}/sample.jar
COUNTDAT_PATH=${DIR}/.cnt

receivecount=0
receivecount=$(ping -c $SENDCOUNT $HOSTS | grep 'received' | AWK -F',' '{ print $2 }' | AWK '{ print $1 }')

failedcount=0
echo $failedcount
echo $receivecount

if [ "$receivecount" -eq "$SENDCOUNT" ];then
	echo "Ping All OK at $(date)"

#空文字ならすべて失敗
elif [ "${receivecount}" = "" ]||["$receivecount" -eq 0 ]; then

	echo "Ping All NG at $(date)"

	if test -f $COUNTDAT_PATH
	then
		#count.dat から変数 count に値を読み込み
    	read failedcount < COUNTDAT_PATH
	fi

	failedcount=`expr $failedcount + $SENDCOUNT`

	if test "$failedcount" -ge "$FAILEDLIMIT" ; then
		echo "aaaaaa"

		#連続失敗記録を超えてしまった
		echo 0 > COUNTDAT_PATH
   		# jar起動

   		#java -jar $JARDIR_PATH PingFailed
	else
		echo "$COUNTDAT_PATH"
   	 	echo $failedcount > $COUNTDAT_PATH
	fi

else
	echo "Ping  at $(date)"
fi