#include "File.h"

#define DIR_SIZE 512
#define NUM_INODES 128
#define INODE_SIZE 32

//block 0 superblock
//block 1 free block vector
//block 2 free inode vector
//block 3 initially used for imap
//imap will always be last block
//each inode location tracked by an int in the imap (0 for unallocated)
//when modifying an inode, it copies the inode to the end of the log, edits the imap for that inode index to point to the second last block (new inode location)
//that way we don't need to modify the inodes in dirs every time a file is changed

//dir format
//entry 0: its own inode number and name
//entry 1: parent dir inode number and name
//entry 2-15: child dirs or files inodes

struct inode {
    int file_size;
    int file_type; //0 for dir, 1 for data file
    //need to make direct_blocks entries 2 bytes not 4
    int16_t direct_blocks[12]; //holds 'addresses' of blocks, represented by ints (the block number). First 10 direct, 10 single, 11 double
} inode;

struct inode inodes[NUM_INODES]; //indices here refer to one less than the indices in dirs

int imap[NUM_INODES];

FILE *disk;

int next_block; //tracks the last block used + 1 (the first free block in the log)
int next_inode;

void deallocate_inode(int index) {
    //deallocate inode
    imap[index] = 0;
    //this does NOT make it persistent
    //need to add imap to end of log elsewhere after this function
}

void deallocate_block(int index) {
    //deallocate block
    int byte = index / 8;
    int bit = index % 8;
    //set the bit from 0 to 1
    char *buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, 1, buf);
    buf[byte] = buf[byte] + pow(2, 7-bit);
    write_block(disk, 1, buf);
    free(buf);
    printf("deallocated %d\n", index);
}

int find_dir_inode(char *dir) {
    //find the inode of the directory the file/dir is in
    //read entry 0
    printf("finding dir inode for %s\n", dir);
    char *path_dirs;
    char *buf; //dirs are always just one block
    int path_inode = 0; //root inode (actually inode 1, but bit 0 in block 2)
    char *name;
    char dir_stat[31];
    strcpy(dir_stat, dir);


    //find root dir block with its inode
    int block_num = inodes[0].direct_blocks[0];
    path_dirs = strtok(dir_stat, "/"); //first will always be "home\0"
    printf("path_dirs: %s\n", path_dirs);
    if (strcmp(path_dirs, "home") != 0) {
        printf("Must use absolute path\n");
        exit(EXIT_FAILURE);
    }
    //already have info for root dir, so get next block
    path_dirs = strtok(NULL, "/");
    printf("path_dirs: %s\n", path_dirs);
    for (int i = 0; path_dirs != NULL; i++) {
        buf = calloc(512,1);
        //iterate up files to get to working dir
        read_block(disk, block_num, buf);
        //buf separated into 16 entries
        printf("file size %d\n", inodes[path_inode].file_size);
        for (int j = 0; j < inodes[path_inode].file_size / 32; j++) {
            name = calloc(31, 1);
            memcpy(name, buf+32*j + sizeof(int), sizeof(char) * 31);
            printf("path_inode %d, name %s\n", path_inode, name);
            if (strcmp(name, path_dirs) == 0) {
                memcpy(&path_inode, buf + 32*j, sizeof(int));
                printf("find_dir_inode: matched %s with %s\n", name, path_dirs);
                //go to next inode
                path_inode = path_inode - 1;
                break;
            }
            free(name);
        }
        free(name);
        free(buf);
        path_dirs = strtok(NULL, "/");
        block_num = inodes[path_inode].direct_blocks[0];
    }
    return path_inode;
}

int create_file (char *loc, char *name) {
    int imap_old = next_block - 1;
    printf("creating file %s in %s\n", name, loc);
    //set up inode
    int inode_index = find_available_inode(disk);
    inodes[inode_index].file_size = 0;
    inodes[inode_index].file_type = 1;
    int inode_index_plus = inode_index + 1;
    printf("new inode index %d\n", inode_index);

    char *buf = calloc(BLOCK_SIZE, 1);
    memcpy(buf, (char *) &inodes[inode_index].file_size, sizeof(int));
    memcpy(buf+4, (char *) &inodes[inode_index].file_type, sizeof(int));
    //no blocks yet for just creating a file

    //write new inode to disk
    int inode_block = find_available_block();
    imap[inode_index] = inode_block;
    printf("new inode block %d\n", inode_block);
    write_block(disk, inode_block, buf);
    free(buf);

    //put file in parent dir (loc)
    int parent_dir_inode = find_dir_inode(loc);
    printf("parent dir inode = %d\n", parent_dir_inode);
    int parent_dir_block = inodes[parent_dir_inode].direct_blocks[0];
    printf("parent dir block %d\n", parent_dir_block);
    char *dir_buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, parent_dir_block, dir_buf);
    int dir_buf_next_entry = inodes[parent_dir_inode].file_size;
    char *name_term = calloc(31, 1);
    strcpy(name_term, name);
    inodes[parent_dir_inode].file_size += 32;
    memcpy(dir_buf+dir_buf_next_entry, (char *) &inode_index_plus, sizeof(int));
    memcpy(dir_buf+dir_buf_next_entry+4, name_term, 31);
    free(name_term);

    //write updated dir contents to disk
    int new_dir_block = find_available_block();
    printf("new home data block %d\n", new_dir_block);
    inodes[parent_dir_inode].direct_blocks[0] = new_dir_block;
    write_block(disk, new_dir_block, dir_buf);
    free(dir_buf);

    //deallocate dir inode and data blocks
    printf("AAA\n");
    deallocate_block(imap[parent_dir_inode]);
    deallocate_block(parent_dir_block);

    //write updated dir inode to end of log
    int new_dir_inode_block = find_available_block();
    printf("new home inode block %d\n", new_dir_inode_block);
    imap[parent_dir_inode] = new_dir_inode_block;
    char *dir_inode_buf = calloc(BLOCK_SIZE, 1);
    memcpy(dir_inode_buf, (char *) &inodes[parent_dir_inode].file_size, sizeof(int));
    memcpy(dir_inode_buf+4, (char *) &inodes[parent_dir_inode].file_type, sizeof(int));
    for (int i = 0; i < inodes[parent_dir_inode].file_size / 32; i++) {
        memcpy(dir_inode_buf+8+2*i, (char *) &inodes[parent_dir_inode].direct_blocks[i], sizeof(int16_t));
    }
    write_block(disk, new_dir_inode_block, dir_inode_buf);
    free(dir_inode_buf);

    //update imap and write to end of log
    int imap_block = find_available_block();
    printf("new imap block %d\n", imap_block);
    write_block(disk, imap_block, (char *) imap);

    //deallocate imap block
    deallocate_block(imap_old);

    return inode_index;
}

int create_dir (char *loc, char *name) {
    if (strcmp(loc, "") == 0) {
        if (strcmp(name, "root") != 0) {
            printf("cannot create a directory at root level\n");
            exit(EXIT_FAILURE);
        }
        //creating root dir
        return find_available_inode();
    }

    int imap_old = next_block - 1;

    //set up inode
    int inode_index = find_available_inode();
    inodes[inode_index].file_size = 32;
    inodes[inode_index].file_type = 0;
    //only need one direct block since file_size = 512
    inodes[inode_index].direct_blocks[0] = find_available_block();
    
    //write dir content to its own directory
    int inode_plus = inode_index + 1;
    char *dir_buf = calloc(BLOCK_SIZE, 1);
    char *name_term = calloc(BLOCK_SIZE, 1);
    strcpy(name_term, name);
    memcpy(dir_buf, (char *) &inode_plus, sizeof(int));
    memcpy(dir_buf+4, name_term, sizeof(char) * 31);
    write_block(disk, inodes[inode_index].direct_blocks[0], dir_buf);
    free(dir_buf);

    //write new inode to available block
    int inode_block = find_available_block();
    char *dir_inode_buf = calloc(BLOCK_SIZE, 1);
    memcpy(dir_inode_buf, (char *) &inodes[inode_index].file_size, sizeof(int));
    memcpy(dir_inode_buf+4, (char *) &inodes[inode_index].file_type, sizeof(int));
    memcpy(dir_inode_buf+8, (char *) &inodes[inode_index].direct_blocks[0], sizeof(int16_t));
    write_block(disk, inode_block, dir_inode_buf);
    free(dir_inode_buf);

    //find the dir's parent dir inode #
    int parent_dir_inode = find_dir_inode(loc);
    int old_parent_inode_block = imap[parent_dir_inode];
    //locate block pointed to by parent dir inode
    int parent_block = inodes[parent_dir_inode].direct_blocks[0];
    int new_parent_block = find_available_block();
    int new_parent_inode_block = find_available_block();

    //add new dir to parent dir block
    char *parent_buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, parent_block, parent_buf);
    int parent_dir_init_size = inodes[parent_dir_inode].file_size;
    inodes[parent_dir_inode].file_size += 32;
    inodes[parent_dir_inode].direct_blocks[0] = new_parent_block;

    //write inode_index and file name to next entry in dir
    memcpy(parent_buf + parent_dir_init_size, (char *) &inode_plus, sizeof(int));
    memcpy(parent_buf + parent_dir_init_size + 4, name_term, sizeof(char) * 32); 
    free(name_term);

    //write updated parent block to new block
    write_block(disk, new_parent_block, parent_buf);
    deallocate_block(parent_block);
    free(parent_buf);

    //write updated parent inode to new block
    char *parent_inode_buf = calloc(BLOCK_SIZE, 1);
    memcpy(parent_inode_buf, (char *) &inodes[parent_dir_inode].file_size, sizeof(int));
    memcpy(parent_inode_buf+4, (char *) &inodes[parent_dir_inode].file_type, sizeof(int));
    memcpy(parent_inode_buf+8, (char *) &inodes[parent_dir_inode].direct_blocks[0], sizeof(int16_t));
    write_block(disk, new_parent_inode_block, parent_inode_buf);
    deallocate_block(old_parent_inode_block);
    free(parent_inode_buf);

    //update imap and write to new block
    imap[parent_dir_inode] = new_parent_inode_block;
    imap[inode_index] = inode_block;
    int new_imap_block = find_available_block();
    write_block(disk, new_imap_block, (char *) imap);

    deallocate_block(imap_old);

    return inode_index;
}

void delete_file (char *dir, char *name) {
    //first check if dir and then check if empty

    //track old imap block
    int imap_old = next_block - 1;
    //find dir block
    char *name_term = calloc(31, 1);
    strcpy(name_term, name);
    printf("name %s, name_term %s\n", name, name_term);
    char *file_name;
    int file_inode = 0;
    int dir_inode = find_dir_inode(dir);
    char *dir_buf = calloc(BLOCK_SIZE, 1);
    int old_dir_block = inodes[dir_inode].direct_blocks[0];
    read_block(disk, old_dir_block, dir_buf);
    
    //find file inode
    int i;
    printf("dir block %d, dir inode %d\n", old_dir_block, dir_inode);
    printf("dir file size %d\n", inodes[dir_inode].file_size);
    for (i = 1; i < inodes[dir_inode].file_size / 32; i++) {
        file_name = calloc(31, 1);
        memcpy(&file_inode, dir_buf + sizeof(char)*32*i, sizeof(int));
        memcpy(file_name, dir_buf + sizeof(char)*32*i + sizeof(int), sizeof(char)*31);
        printf("file_name %s name_term %s file_inode %d\n", file_name, name_term, file_inode);
        if (strcmp(file_name, name_term) == 0) {
            printf("delete file: matched %s with %s\n", file_name, name_term);
            //memcpy(&file_inode, line, sizeof(int));
            free(file_name);
            break;
        }
        free(file_name);
    }
    file_inode = file_inode - 1;
    if (file_inode == -1) {
        printf("File is not in this directory.\n");
        return;
    }
    free(name_term);
    
    //deallocate all deleted file's direct blocks
    for (int k = 0; k < inodes[file_inode].file_size / 512; k++) {
        deallocate_block(inodes[file_inode].direct_blocks[k]);
        inodes[file_inode].direct_blocks[k] = 0;
    }

    //above for loop will skip if file is a dir (file size = 32, so 32 / 512 == 0)
    if (inodes[file_inode].file_type == 0) {
        deallocate_block(inodes[file_inode].direct_blocks[0]);
        inodes[file_inode].direct_blocks[0] = 0;
    }

    //deallocate deleted file's old inode block
    deallocate_block(imap[file_inode]);

    //shift following entries down
    int j;
    for (j = i; j < inodes[dir_inode].file_size / 32 - 1; j++) {
        memcpy(dir_buf + 32*j, dir_buf + 32*(j+1), sizeof(char) * 32);
    }
    memset(dir_buf + 32*j, 0, sizeof(char) * 32);

    //decrement dir file size
    inodes[dir_inode].file_size -= 32;

    //write new dir block to end of log
    int new_dir_block = find_available_block();
    inodes[dir_inode].direct_blocks[0] = new_dir_block;
    write_block(disk, new_dir_block, dir_buf);
    deallocate_block(old_dir_block);
    free(dir_buf);

    //write new dir inode to end of log
    char *new_dir_inode_buf = calloc(BLOCK_SIZE, 1);
    memcpy(new_dir_inode_buf, (char *) &inodes[dir_inode].file_size, sizeof(int));
    memcpy(new_dir_inode_buf+sizeof(int), (char *) &inodes[dir_inode].file_type, sizeof(int));
    memcpy(new_dir_inode_buf+sizeof(int)*2, (char *) &new_dir_block, sizeof(int16_t));
    
    int new_dir_inode_block = find_available_block();
    write_block(disk, new_dir_inode_block, new_dir_inode_buf);
    free(new_dir_inode_buf);

    //update imap
    //deallocate inode
    deallocate_inode(file_inode);
    //update parent dir imap entry
    int old_dir_inode_block = imap[dir_inode];
    imap[dir_inode] = new_dir_inode_block;
    //write new imap to end of file
    int new_imap_block = find_available_block();
    write_block(disk, new_imap_block, (char *) imap);
    deallocate_block(old_dir_inode_block);
    deallocate_block(imap_old);

    

}

void append_file (char *dir, char *name, char *buf, int num_bytes) {
    //assumes buf is initialized and holds contents to write to file name
    int imap_old = next_block - 1;
    char *name_term = calloc(31, 1);
    strcpy(name_term, name);
    //find dir inode
    int file_inode = 0;
    int dir_inode = find_dir_inode(dir);
    char *dir_buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, inodes[dir_inode].direct_blocks[0], dir_buf);
    //need to change the inode of the i'th entry once appended
    int i;
    printf("BBB\n");
    printf("file size %d\n", inodes[dir_inode].file_size);
    for (i = 1; i < inodes[dir_inode].file_size / 32; i++) {
        char *file_name = calloc(32, 1);
        //get the file/dir name
        memcpy(file_name, dir_buf + 32*i + 4, sizeof(char) * 31);
        if (strcmp(name_term, file_name) == 0) {
            //found file, now find its contents with inode
            memcpy(&file_inode, dir_buf + 32*i, sizeof(int));
            free(file_name);
            break;
        }
        free(file_name);
    }
    free(name_term);
    file_inode = file_inode - 1;
    if (file_inode == -1) {
        printf("could not find file in this directory, or no inode allocated\n");
        exit(EXIT_FAILURE);
    }
    printf("appending to file inode %d\n", file_inode);

    //reject if file is a dir
    if (inodes[file_inode].file_type == 0) {
        printf("Cannot append to a directory.\n");
        return;
    }

    //find first block to write to
    int block_write = inodes[file_inode].file_size / 512; //size should always be in 512 increments
    int num_blocks_write = num_bytes / 512;
    if (num_bytes % 512 != 0) {
        num_blocks_write += 1;
    }
    char *buf_write = calloc(512 * num_blocks_write, 1);
    memcpy(buf_write, buf, sizeof(char)*num_bytes);
    printf("starting at block %d for %d blocks\n", block_write, num_blocks_write);
    for (int j = block_write; j < block_write + num_blocks_write; j++) {
        //allocate blocks
        inodes[file_inode].direct_blocks[j] = find_available_block();
        //write to block
        printf("writing to block %d\n", inodes[file_inode].direct_blocks[j]);
        write_block(disk, inodes[file_inode].direct_blocks[j], buf_write+512*(j-block_write));
    }
    free(buf_write);
    inodes[file_inode].file_size += BLOCK_SIZE*num_blocks_write;
    printf("new file size %d\n", inodes[file_inode].file_size);
    //write inode to disk
    char *inode_buf = calloc(BLOCK_SIZE, 1);
    memcpy(inode_buf, (char *) &inodes[file_inode].file_size, sizeof(int));
    memcpy(inode_buf+sizeof(int), (char *) &inodes[file_inode].file_type, sizeof(int));
    for (int j = 0; j < inodes[file_inode].file_size / 512; j++) {
        memcpy(inode_buf+sizeof(int)*2+sizeof(int16_t)*j, (char *) &inodes[file_inode].direct_blocks[j], sizeof(int16_t));
    }
    int new_inode_block = find_available_block();
    write_block(disk, new_inode_block, inode_buf);
    //int new_inode_block_plus = new_inode_block + 1;
    printf("deallocating old inode block\n");
    deallocate_block(imap[file_inode]);
    imap[file_inode] = new_inode_block;
    free(inode_buf);
    
    free(dir_buf);
/*
    //update parent dir block
    int new_parent_dir_block = find_available_block();
    memcpy(dir_buf+32*i, (char *) &new_inode_block_plus, sizeof(int));
    write_block(disk, new_parent_dir_block, dir_buf);
    free(dir_buf);
    int old_parent_dir_block = inodes[dir_inode].direct_blocks[0];
    inodes[dir_inode].direct_blocks[0] = new_parent_dir_block;
    deallocate_block(old_parent_dir_block);

    //update parent dir inode
    int new_parent_dir_inode_block = find_available_block();
    char *new_parent_dir_inode_buf = calloc(32, 1);
    memcpy(new_parent_dir_inode_buf, (char *) &inodes[dir_inode].file_size, sizeof(int));
    memcpy(new_parent_dir_inode_buf+sizeof(int), (char *) &inodes[dir_inode].file_type, sizeof(int));
    memcpy(new_parent_dir_inode_buf+sizeof(int)*2, (char *) &inodes[dir_inode].direct_blocks[0], sizeof(int16_t));
    write_block(disk, new_parent_dir_inode_block, new_parent_dir_inode_buf);
    free(new_parent_dir_inode_buf);
    int old_parent_dir_inode_block = imap[dir_inode];
    imap[dir_inode] = new_parent_dir_inode_block;
    deallocate_block(old_parent_dir_inode_block);
*/    
    //update imap
    deallocate_block(imap_old);
    int imap_new = find_available_block();
    write_block(disk, imap_new, (char *) imap);
}

void read_file (char *dir, char *file, char *block_buf) {
    //assumes block_buf is initialized and the right size
    //should also work for directories

    //find dir inode
    int file_inode = 0;
    int dir_inode = find_dir_inode(dir);
    //read dir contents to find file's inode
    char *buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, inodes[dir_inode].direct_blocks[0], buf);
    for (int i = 1; i < inodes[dir_inode].file_size / 32; i++) {
        char *name = calloc(32, 1);
        //get the file/dir name
        memcpy(name, buf + 32*i + 4, sizeof(char) * 31);
        if (strcmp(file, name) == 0) {
            //found file, now find its contents with inode
            memcpy(&file_inode, buf + 32*i, sizeof(int));
            free(name);
            break;
        }
        free(name);
    }
    free(buf);
    file_inode = file_inode - 1;
    if (file_inode == -1) {
        printf("could not find file in this directory, or no inode allocated\n");
        exit(EXIT_FAILURE);
    }
    printf("reading file inode %d ", file_inode);
    
    //find direct blocks of the file's inode
    int num_blocks = inodes[file_inode].file_size / 512;
    if (inodes[file_inode].file_type == 0) {
        //file is a dir, so read its only block
        num_blocks = 1;
    }
    //open direct blocks and read
    for (int i = 0; i < num_blocks; i++) {
        int block = inodes[file_inode].direct_blocks[i];
        read_block(disk, block, block_buf + 512*i);
        printf("blocks %d ", block);
    }
    printf("\n");
}

/*
int locate_first_1 (int byte) {
    //to find first available block/inode
    int loc_1 = 0;
    if (byte <= 0x01) {
        loc_1 = 7;
    }
    else if (byte <= 0x03) {
        loc_1 = 6;
    }
    else if (byte <= 0x07) {
        loc_1 = 5;
    }
    else if (byte <= 0x0F) {
        loc_1 = 4;
    }
    else if (byte <= 0x1F) {
        loc_1 = 3;
    }
    else if (byte <= 0x3F) {
        loc_1 = 2;
    }
    else if (byte <= 0x7F) {
        loc_1 = 1;
    }
    return loc_1;
}
*/

int find_available_inode () {
    //look at imap block for first zero entry
    //return + 1
    int i;
    int found = 0;
    for (i = 0; i < NUM_INODES; i++) {
        if (imap[i] == 0) {
            found = 1;
            break;
        }
    }
    if (found == 0) {
        //no inodes left
        printf("No more inodes to allocate.  Exiting...\n");
        exit(EXIT_FAILURE);
    }

    //need to update the disk and allocate elsewhere because we don't know its location yet

    return i;
}

int find_available_block () {
    int byte = next_block / 8;
    int bit = next_block % 8;
    char *buf = calloc(BLOCK_SIZE, 1);
    read_block(disk, 1, buf);
    //set next block to 0
    buf[byte] = buf[byte] - pow(2, 7-bit);
    write_block(disk, 1, buf);
    free(buf);
    return next_block++;
}

int check_disk () {

}

FILE *initLLFS (char *disk_name) {
    disk = fopen(disk_name, "w+");
    char *init_zero = calloc(BLOCK_SIZE * NUM_BLOCKS, 1); //inits to zeros
    fwrite(init_zero, BLOCK_SIZE * NUM_BLOCKS, 1, disk);
    free(init_zero);
    fclose(disk);
    
    disk = fopen(disk_name, "r+");

    next_block = 10;
    next_inode = 0;

    //set up block 0 and 1
    char *buf = calloc(BLOCK_SIZE, 1);
    //block 0 is the superblock
    //first 4 bytes: magic number
    int magic_num = 96;
    int num_blocks = NUM_BLOCKS;
    int num_inodes = NUM_INODES;
    memcpy(buf, (char *) &magic_num, sizeof(int));
    //next 4 bytes: # of blocks on disk
    memcpy(buf+4, (char *) &num_blocks, sizeof(int));
    //next 4 bytes: number of inodes for disk
    memcpy(buf+8, (char *) &num_inodes, sizeof(int));

    write_block(disk, 0, buf);
    free(buf);

    //block 1 is the free block vector (512 bytes)
    //first 10 blocks 0-9 not available for data
    //indicates available blocks with 1
    char *bitmap = calloc(NUM_BLOCKS / 8, 1);
    bitmap[0] = 0x00;
    bitmap[1] = 0x3F;
    for (int i  = 2; i < NUM_BLOCKS / 8; i++) {
        bitmap[i] = 0XFF;
    }

    write_block(disk, 1, bitmap);
    free(bitmap);
    //set up inode partition
    //128 inodes of 4 bytes each to indicate which block they're in
    //0 if unassigned
    //don't overwrite this block, instead it is always the last on the log
    //don't need to set anything up because all zeros by default from init_zero
    


    //create root directory inode
    inodes[0].file_size = 32;//only consists of its own inode to start
    inodes[0].file_type = 0;
    printf("finding block for root dir\n");
    inodes[0].direct_blocks[0] = find_available_block(); //first available data block, should be 10
    printf("finding block for root inode\n");
    int root_inode_block = find_available_block();
    int root_inode = find_available_inode();
    int root_inode_plus = root_inode + 1;
    
    printf("data block %d %x\ninode block %d %x\n", inodes[0].direct_blocks[0], inodes[0].direct_blocks[0] * 512, root_inode_block, root_inode_block * 512);

    //write root directory contents to disk
    char *root_dbuf = calloc(BLOCK_SIZE, 1);
    char *root_dir_name = calloc(31, 1);
    char *home = "home\0";
    strncpy(root_dir_name, home, 5);
    memcpy(root_dbuf, (char *) &root_inode_plus, sizeof(int));
    memcpy(root_dbuf+4, root_dir_name, sizeof(char) * 31);
    
    write_block(disk, inodes[0].direct_blocks[0], root_dbuf);
    free(root_dbuf);
    free(root_dir_name);

    //write inode contents to a buffer
    char *root_ibuf = calloc(BLOCK_SIZE, 1);
    memcpy(root_ibuf, (char *) &inodes[0].file_size, sizeof(int));
    memcpy(root_ibuf+4, (char *) &inodes[0].file_type, sizeof(int));
    memcpy(root_ibuf+8, (char *) &inodes[0].direct_blocks[0], sizeof(int16_t));
    write_block(disk, root_inode_block, root_ibuf);
    free(root_ibuf);

    //create imap and write new imap to latest block
    imap[0] = root_inode_block;
    for (int i = 1; i < NUM_INODES; i++) {
        imap[i] = 0;
    }
    //write imap to last block
    //need to memcpy from imap to char arr?
    printf("finding block for imap\n");
    int imap_index = find_available_block();
    write_block(disk, imap_index, (char *) imap);
    next_block = imap_index + 1;
    printf("imap %d %x\nnext_block %d %x\n", imap_index, imap_index * 512, next_block, next_block * 512);
    return disk;      
}
