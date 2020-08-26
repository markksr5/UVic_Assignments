#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define BLOCK_SIZE 512
#define NUM_BLOCKS 4096

void read_block (FILE *disk, int block_num, char *buf);

void write_block(FILE *disk, int block_num, char *data);
