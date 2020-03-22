# CSC 360 A2P1 Worm

Mark Kaiser
V00884677
## Implemented Functionality
This is a user-level scheduler.  Tasks only are blocked when they call <code>task_wait(task1)</code>, <code>task_sleep(time_ms)</code> or <code>task_readchar()</code>.  These wait for task1 to finish, sleep for time_ms milliseconds, and wait for input characters, respectively.  The scheduler is implemented in round-robin fashion, so when a tasks finishes or is blocked, the scheduler checks the next task's (i+1th) state.  There are six states: <code>READY, RUNNING, WAITING, READING, SLEEPING, FINISHED</code>.  I used a switch statement in an infinite loop to handle these states. This seemingly worked well, but it proved to be difficult to ensure the transitions between states were correct.  To return to main, I used <code>return</code> to bring the context back into the main function, since main did not have a function attached to its context to switch back to.  I similarly used <code>return</code> to return back into the original function if it blocked with any of the aforementioned blocking functions.  I found that swapcontext(a, b) didn't work to do this because as a task blocks, the task's context itself refers to instructions inside the scheduler function, so when I swapped back into the context, it swapped back into the scheduler function, not directly back into the original function.

## Known Defects
### task_readchar()
In test 4, the fourth character does not print.  This is probably because when the last character is read, the scheduler signals that the task has finished, then the scheduler kicks in and runs the main task instead of finishing the reading task.  Therefore the main task runs and finishes before the last character is printed out.  I didn't have time to fix this issue.

### Memory Management
After checking with valgrind, there are numerous errors with memory management.  I free the context and exit context stacks if main is the task running and all other tasks are finished.  However, there are still undiagnosed issues that I didn't have time to get around to.

## Resources Used
I didn't use any resources online besides the odd stackoverflow page for syntax and functionality, and documentation.  

* Documentation for swapcontext, various C functions
* Collaborated with: Mark Sigilai