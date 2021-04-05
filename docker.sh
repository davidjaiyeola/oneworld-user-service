#!/bin/bash
source ~/.bashrc

echo "Building"

docker build -t user-service:1.0-SNAPSHOT . -f Dockerfile
