#!/bin/bash


if [ $# -eq 0 ]; then
    echo "No arguments supplied. #Exemplary usage: . generate_ASM.sh tests/good/"
fi
for i in $1*.lat; do
    echo "==================================================================="
    ./latc_x86_64 $i
done

