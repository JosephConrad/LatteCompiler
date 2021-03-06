#!/bin/bash


if [ $# -eq 0 ]; then
    echo "No arguments supplied. #Exemplary usage: . generate_ASM.sh tests/good/"
fi
for i in $1*.lat; do
    ./latc_x86_64 $i
done

cd $1
for i in *.s; do
    filename=$(basename "$i")
    filename="${filename%.*}"
    extension='.output'
    file2=$filename$extension
    file1="core.output"
    
    nasm -g -f elf64 -o lat.o $i
    gcc -o $filename -Wall -g lat.o ../../lib/runtime.o
    ./$filename < core018.input > core.output
    
    
    if diff $file1 $file2 >/dev/null ; then
        echo $file2" OK" 
    else
        echo Different
        cat $file2
        cat $file1
    fi

done

rm core.output
