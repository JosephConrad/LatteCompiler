#!/bin/bash

for i in $1*.lat; do
    java -jar target/latc_x86_64.jar $i
done
