# CSC 360 A2P2 L1 Approximation

Mark Kaiser
V00884677
## Implemented Functionality
My approach for this problem was to use multiple threads that read different indices for their "start" point.  Each thread would then compute an L1 line for each pair of points (i,k) where i is the start point and k is all the values from i+1 to the end of the set.  I defined the number of threads at the top of the file, and then each thread was designated a certain range of indices for which it would calculate the L1 lines.  After calculating the L1 line for each pair of data points, I then compared the distance calculated to a shared global variable.  By separating the data into distinct ranges, I needed to use two mutexes to lock the critical sections of comparing the distance calculated to <code>min_dist_02</code> (in the threads calculating 2002 L1 lines) and to <code>min_dist_complete</code> (in the threads calculating L1 lines for the complete data set).  This was because each pair of data points was distinct, and the threads were only reading and modifying the two above shared values.

## Known Defects
### Parallel Thread Solution Running Slower than Sequential
Interestingly, the sequential part of the program to calculate the best L1 line runs faster than the parallel method by about 20 seconds.  I suspect that this is because I take into account the pthread creating and joining in the elapsed time.  Also, the thread ranges are not optimized, as the first thread (range of lowest valued indices) has many more pairs of points to calculate than the last thread (range of highest valued indices).  If I had more time, I would implement better ranges for threads so that they would all have a relatively even amount of pairs to consider.  Also, I would calculate the times with and without the thread creation and joining to compare and see how expensive these operations are.

## Resources Used
* Stackoverflow for various challenges I came across, including reading from .csv
* Documentation for pthreads, mutexes, and semaphores
* Collaborated with: no one