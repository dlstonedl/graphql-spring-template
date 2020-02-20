#!/usr/bin/env bash

docker rmi graphql:1.0
docker build -t graphql:1.0 .

