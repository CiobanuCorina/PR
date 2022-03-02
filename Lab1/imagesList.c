/*
#include <stdio.h>
#include <stdlib.h>
#define PCRE2_CODE_UNIT_WIDTH 0
#include <pcre2.h>

char * imagesList(int bytesCount) {
    FILE *file;
    char * buffer = malloc(sizeof(char)*bytesCount);
    pcre2_match_data_32 pcre;
    file = fopen("D:\\Cora\\univer\\SemVII\\PR\\content.txt", "rb");
    if (file == NULL) {
        perror("\n\n\n\n\nFailed to open file\n\n\n\n\n");
        exit(0);
    }
    fread(buffer, 1, bytesCount, file);
    pcre2_match_32("", buffer, bytesCount * sizeof(char), 0, PCRE2_NOTEMPTY, pcre);
}

*/
