all:
	mvn install
	gcc -c lib/runtime.c -o lib/runtime.o
