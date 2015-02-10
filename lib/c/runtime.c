#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void printInt(int number) {
    printf("%d\n", number);
}

void printString(char* string) {
    printf("%s\n", string);
}

int readInt() {
    int number;
    scanf("%d\n", &number);
    return number; 
}

char* readString() {
    int index = 0;
    char c;
    char *string = (char *) malloc(1025);
    while((c = getchar()) != '\n') {
        string[index++] = c;
    }
    return string;
}

char* concatenateString(char* str1, char* str2) {
    char* string = (char *) malloc(strlen(str1) + strlen(str2) + 1);
    strcpy(string, str1);
    strcat(string, str2);
    return string;
}
