#include "drivers.h"



void read_block(FILE *disk, int block_num, char *buf) {
    fseek(disk, block_num * BLOCK_SIZE, SEEK_SET);
    fread(buf, BLOCK_SIZE, 1, disk);
}

void write_block(FILE *disk, int block_num, char *data) {
    fseek(disk, block_num * BLOCK_SIZE, SEEK_SET);
    fwrite(data, BLOCK_SIZE, 1, disk); //will overwrite existing data in the block
}

