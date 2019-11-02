#!/bin/sh
docker kill micro
docker rm micro
docker run -d -p 50080:8080 -p 50048:4848 --name micro dkrasimir/micro