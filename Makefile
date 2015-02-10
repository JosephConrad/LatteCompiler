all:
	mvn install
	gcc -c lib/c/runtime.c -o lib/c/runtime.o
