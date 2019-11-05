#!/usr/bin/env bash

checkResponseCode() {
  NEW_LINE=$'\n'
  URL=$1
  echo "Waiting for application to respond from $URL"
  NEXT_WAIT_TIME=1
  MAX_WAIT_TIME=600
  SLEEP_TIME=1
  until [ $NEXT_WAIT_TIME -gt $MAX_WAIT_TIME ] || [ $(curl -s -o /dev/null -w "%{http_code}" $URL) == $2 ]; do
    echo -n '.'
    sleep $SLEEP_TIME
    (( NEXT_WAIT_TIME++ ))
  done
  if [ $NEXT_WAIT_TIME -gt $MAX_WAIT_TIME ]
  then
    echo "$NEW_LINE checkResponseCode: error timeout after $NEXT_WAIT_TIME sec with $URL"
    exit 1
  else
    echo "$NEW_LINE checkResponseCode: successfully after $NEXT_WAIT_TIME sec wait time with $URL"
  fi
}


checkResponseCode $1 $2

exit 0
