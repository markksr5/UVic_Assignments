# CSC 360 A3 LLFS

Mark Kaiser
V00884677

## Known Bugs and Defects
The most obvious defect that I was aware of but did not have time to fix is that there is currently no functionality to continue to write to and update the disk if the log filled up the disk to the 4096th block.  This was an early oversight, and by the time I got significant functionality on the file system, I did not have time to adapt the code to allow looping through the earlier, deallocated blocks.  This would probably be as simple as incrementing next_block % 4096 and checking if next_block was actually available, but I ran out of time to implement it and test it.  I am able to reallocate previously deleted inodes (up to 128 total) since I take the earliest inode index available, not maintaining a "next_inode" tracker.

One area that I am very confident is wrong is the lack of checking for bad input.  There is insufficient checking for bad parameters, especially those concerning the max number of blocks, and so my code is very vulnerable to programs that write over the whole disk.  This assignment was challenging, and I did not have the time and energy to consider much beyond the happy path.  I still think I met all of the first, second, and fourth goals, and considered future improvements to tackle the third goal.

Another very clear area of improvement for my program is the lack of utility function calls.  I had to think through each case (creating, writing, reading, deleting) carefully and only after writing a few did I realize that there were lots of duplicate areas (e.g. finding the inode of the specified file in the parent directory).  This was an oversight in my initial design, and led to complicated code.  However, I do feel like I understand the ins and outs of each function better since I had to write the code so many times.  To help with future iterations and to ease comprehension and marking, I have tried to comment the functionality of each section as well as I could.

## Resources Used
I designed my file system based on the assignment description and this video lecture from the University of Virginia: https://www.youtube.com/watch?v=KTCkW_6zz2k&t=3s.  I didn't collaborate with anyone on this assignment, but I sent Mark Sigilai the above video.