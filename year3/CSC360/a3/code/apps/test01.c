#include <math.h>
#include "../io/File.h"

int main () {
    FILE *disk = initLLFS("../disk/vdisk");
    int home_inode = find_dir_inode("/home");
    printf("home inode = %d\n", home_inode);
    
    int file_inode = create_file ("/home", "test");
    printf("new file inode %d\n", file_inode);
 
    int dir_inode = create_dir ("/home", "newdir");
    printf("new dir inode %d\n", dir_inode);

    int dir_inode_2 = create_dir ("/home", "newerdir");
    printf("newer dir inode %d\n", dir_inode_2);

    int new_file_inode = create_file ("/home/newdir", "test01");
    printf("test01 inode %d\n", new_file_inode);

    char *buf = calloc(512, 1);
    read_file("/home", "newdir", buf);
    printf("reading newdir\n");
    for (int i = 0; i < 512; i++) {
        printf("%x ", buf[i]);
    }
    printf("\n");
    free(buf);

    buf = calloc(1024, 1);
    memset(buf, 'a', 1024);
    memset(buf+512, 'b', 512);
    append_file("/home", "test", buf, 1024);
    printf("appended 512 a's and 512 b's to /home/test\n");
    free(buf);

    char *new_buf = calloc(1024, 1);
    memset(new_buf, 'c', 1024);
    memset(new_buf+512, 'd', 512);
    append_file("/home", "test", new_buf, 1024);
    printf("appended 512 c's and 512 d's to /home/test\n");
    for (int i = 0; i < 1024; i++ ) {
        printf("%c ", new_buf[i]);
    }
    printf("\n");
    free(new_buf);

    char *new_buf_1 = calloc(800, 1);
    memset(new_buf_1, 'e', 800);
    memset(new_buf_1+400, 'f', 400);
    append_file("/home/newdir", "test01", new_buf_1, 600);
    free(new_buf_1);

    delete_file("/home", "test");
    printf("deleted test\n");

    delete_file("/home", "newerdir");
    printf("deleted newerdir\n");
  
    delete_file("/home/newdir", "test01");
    printf("deletd newdir/test\n");

    fclose(disk);
    return 0;
}
