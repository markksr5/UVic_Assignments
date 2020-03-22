# CSC 360 SEEsh

Mark Kaiser
V00884677
## Implemented Functionality

The SEEsh shell runs all the required built-in commands (cd, pwd, help, exit, set, unset) as well as the history function (up to the previous 100 lines in the current session - `history_arr` is defined statically).  Currently the optional `![prefix]` command is not implemented.  The shell also is able to run user programs located in the PATH variable.

## Known Defects
### chdir Memory Leak
One defects that is known is the memory leak in the cd function.  This occurs when the directory is changed to a valid directory (does not occur when the user inputs a variable that is not a reachable directory).  The output from valgrind is shown below.
```
==10403== Invalid read of size 1
==10403==    at 0x4C32D32: __strlen_sse2 (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==10403==    by 0x4EBC9D1: puts (ioputs.c:35)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000480 is on thread 1's stack
==10403==  584 bytes below stack pointer
==10403==
==10403== Invalid read of size 1
==10403==    at 0x4C32D44: __strlen_sse2 (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==10403==    by 0x4EBC9D1: puts (ioputs.c:35)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000481 is on thread 1's stack
==10403==  575 bytes below stack pointer
==10403==
==10403== Invalid read of size 1
==10403==    at 0x4EC7A5D: _IO_file_xsputn@@GLIBC_2.2.5 (fileops.c:1241)
==10403==    by 0x4EBCA8E: puts (ioputs.c:40)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000498 is on thread 1's stack
==10403==  488 bytes below stack pointer
==10403==
==10403== Invalid read of size 1
==10403==    at 0x4EC7A74: _IO_file_xsputn@@GLIBC_2.2.5 (fileops.c:1241)
==10403==    by 0x4EBCA8E: puts (ioputs.c:40)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000497 is on thread 1's stack
==10403==  489 bytes below stack pointer
==10403==
==10403== Invalid read of size 1
==10403==    at 0x4C371B8: mempcpy (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==10403==    by 0x4EC7993: _IO_file_xsputn@@GLIBC_2.2.5 (fileops.c:1258)
==10403==    by 0x4EBCA8E: puts (ioputs.c:40)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000480 is on thread 1's stack
==10403==  496 bytes below stack pointer
==10403==
==10403== Invalid read of size 1
==10403==    at 0x4C371C6: mempcpy (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==10403==    by 0x4EC7993: _IO_file_xsputn@@GLIBC_2.2.5 (fileops.c:1258)
==10403==    by 0x4EBCA8E: puts (ioputs.c:40)
==10403==    by 0x109602: call_seesh (SEEsh.c:223)
==10403==    by 0x109837: main (SEEsh.c:260)
==10403==  Address 0x1fff000482 is on thread 1's stack
==10403==  494 bytes below stack pointer
==10403==
```
### Extra Arguments
The shell also does not check extra arguments.  In its current state it only checks if the first argument passed matches either a built in function or a user program.  For example, `pwd abc` will still print the current working directory, as `abc` is ignored entirely.  Another example is `set Mark Kaiser abc`, where `abc` is again ignored, as the `set` function does not check for extra arguments beyond the defined three.  This could be quite easily implemented if I had more time, but I didn't notice any clear requirements around this defect.

## Resources Used

Here is a short list of all the help resources that I used during this project.

* Stephen Brennen's shell tutorial
* Stack Overflow for C and shell help
* GNU library for C functions
* Various GDB and Vim cheat sheets
* Collaborated with: Mark Sigilai and Matthew Olaka