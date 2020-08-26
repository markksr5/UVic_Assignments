CC := gcc
CFLAGS := -g -Wall -Wno-deprecated-declarations

all: test01

clean:
	rm -rf test01 test01.dSYM

test01: ../io/File.c ../io/File.h ../disk/drivers.c ../disk/drivers.h test01.c
	$(CC) $(CFLAGS) -o test01 ../io/File.c ../disk/drivers.c test01.c -lm
