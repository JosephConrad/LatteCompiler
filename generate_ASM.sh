#!/bin/bash


if [ $# -eq 0 ]; then
    echo "No arguments supplied. #Exemplary usage: . generate_ASM.sh tests/good/"
fi
for i in $1*.lat; do
    java -jar target/latc_x86_64.jar $i
done
