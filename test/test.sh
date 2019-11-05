#!/usr/bin/env bash
status_code=$(curl --write-out %{http_code} --silent --output /dev/null $1)
echo "resource status is $status_code"
if [[ "$status_code" -eq 200 ]] ; then
  exit 0
else
  exit 1
fi

