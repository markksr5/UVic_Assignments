#include <math.h>

#include "../disk/drivers.h"

void deallocate_inode (int index);

void deallocate_block (int index);

int find_dir_inode (char *loc);

int create_file (char *loc, char *name);

int create_dir (char *loc, char *name);

void delete_file_from_dir (int dir_block, int dir_inode, int file_inode);

void delete_file (char *loc, char *name);

void append_file (char *loc, char *name, char *buf, int num_chars);

void read_file (char *loc, char *name, char *buf);

int locate_first_1 (int byte);

int find_available_inode ();

int find_available_block ();

int check_disk ();

FILE *initLLFS (char *disk_name);
