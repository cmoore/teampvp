#!/bin/bash

while inotifywait -q -r -e modify --exclude '\*#$' ./src; do
    sleep 1
    mvn compile
done

