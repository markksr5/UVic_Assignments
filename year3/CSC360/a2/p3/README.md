# CSC 360 A2P3 Dining Philosophers

Mark Kaiser
V00884677
## Implemented Functionality
The Dining Philosophers problem deals with concurrency, with multiple threads trying to access the same resources.  The problem states that 5 philosophers can either think or eat, and to eat, they must have picked up a left and right chopstick.  There are 5 chopsticks total, and only one between each pair of philosophers.  If a philosopher cannot eat, they are thinking.  This problem is widely known and used to explain the concepts of deadlock and starvation.

To handle these problems, I used one mutex and N (5) semaphores.  The mutex simulated the waiter in the initial problem, allowing only one thread at a time to pick up or put down a chopstick.  The semaphores refer to the philosophers, so that they wait until they are ready to perform an action.

The mutex reduces the parallelism of the program, but is a safe way to allow philosophers to pick up a chopstick only if the chopstick has not already been picked up.  There are probably other ways to slightly increase the parallelism based on this mutex method (such as allowing two philosophers that are not adjacent to each other to pick up/put down chopsticks at the same time), but these were not implemented due to time.

Using a sixth (N+1th) thread, every second I print the philosophers who are eating.

## Resources Used
I took lots of inspiration from Foo So's YouTube video linked below.  He wrote a program to solve this problem in C but using SDL threads and semaphores instead of POSIX.  I wanted to look primarily at a solution that I wasn't able to copy exactly so I could understand the problem and solution better.  I implemented my solution with mostly the same logic but with the POSIX library.

* Dining Philosophers Implementation in C using SDL (https://www.youtube.com/watch?v=BjrMrw2GM6o&t=661s)
* Documentation for pthreads, mutexes, and semaphores