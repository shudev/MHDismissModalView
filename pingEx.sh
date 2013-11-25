#!/bin/bash
TARGET="localhost"
SENDCOUNT=3
FAILEDLIMIT=6
LOGFILELIMIT=7

DIR=$(cd $(dirname $0); pwd)
JARDIR_PATH=${DIR}/sample.jar
COUNTDAT_PATH=${DIR}/.cnt
LOGFILE_PATH=${DIR}/log
mkdir $LOGFILE_PATH　>/dev/null 2>&1
LOGFILE_NAME=${LOGFILE_PATH}/
LOGFILE_NAME=${LOGFILE_NAME}ping
LOGFILE_NAME=${LOGFILE_NAME}`date +%Y%m%d.log`

failedcount=0

i=0
while [ $i -lt $SENDCOUNT ]
do
	ping -c 1 $TARGET >> $LOGFILE_NAME
	if ! [ $? -eq 0 ]
	then
		echo "Ping failed (failed count=$failedcount) $(date)" >> $LOGFILE_NAME 

		if test -f $COUNTDAT_PATH
		then
			#count.dat から変数 count に値を読み込み
    		read failedcount < $COUNTDAT_PATH
		fi

		failedcount=`expr $failedcount + 1`

		if test "$failedcount" -ge "$FAILEDLIMIT" ; then
			echo "Limit came (failed count=$FAILEDLIMIT)" >> $LOGFILE_NAME 

			#連続失敗記録を超えてしまった
			echo 0 > $COUNTDAT_PATH

   			# jar起動
   			#java -jar $JARDIR_PATH PingFailed
   			i=$SENDCOUNT
		else

   	 		echo $failedcount > $COUNTDAT_PATH
		fi

	else
		echo "Ping success (failed count=$failedcount) $(date)" >> $LOGFILE_NAME
		#連続記録をリセット
		echo 0 > $COUNTDAT_PATH
	fi

	i=`expr $i + 1`
done

# ファイル更新日時が指定日数を越えたログファイルを削除
find $LOGFILE_PATH -name "ping*.log" -type f -mtime +${LOGFILELIMIT} -exec rm -f {} \;
