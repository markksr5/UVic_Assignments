#define _XOPEN_SOURCE
#define _XOPEN_SOURCE_EXTENDED

#include "scheduler.h"
#include "util.h"

#include <assert.h>
#include <ucontext.h>
#include <time.h>
#include <ncurses.h>


// This is an upper limit on the number of tasks we can create.
#define MAX_TASKS 128

//Max amount of characters read in readchar
#define MAX_CHARS_READ 1024

// This is the size of each task's stack memory
#define STACK_SIZE 65536

enum State {READY, RUNNING, WAITING, READING, SLEEPING, FINISHED};

// This struct will hold the all the necessary information for each task
typedef struct task_info {
  // This field stores all the state required to switch back to this task
  ucontext_t context;
  
  // This field stores another context. This one is only used when the task
  // is exiting.
  ucontext_t exit_context;
  
  // TODO: Add fields here so you can:
  //   a. Keep track of this task's state.
  //   b. If the task is sleeping, when should it wake up?
  //   c. If the task is waiting for another task, which task is it waiting for?
  //   d. Was the task blocked waiting for user input? Once you successfully
  //      read input, you will need to save it here so it can be returned.
  
  enum State state;
  long wake_up;
  int waiting_for;
  int input;
} task_info_t;

int current_task = 0; //< The handle of the currently-executing task
int num_tasks = 1;    //< The number of tasks created so far
task_info_t tasks[MAX_TASKS]; //< Information for every task
int c[MAX_CHARS_READ]; //<input chars read
int ind = 0;

/**
 * Initialize the scheduler. Programs should call this before calling any other
 * functiosn in this file.
 */
void scheduler_init() {
  // TODO: Initialize the state of the scheduler
  //create a main context (which is current_task = tasks[0]) to go back to at the end
  tasks[0].state = RUNNING;
  
  // Now we start with the task's actual running context
  getcontext(&tasks[0].context);
  
  // Allocate a stack for the new task and add it to the context
  tasks[0].context.uc_stack.ss_sp = malloc(STACK_SIZE);
  tasks[0].context.uc_stack.ss_size = STACK_SIZE;
}

int scheduler_exec() {
  int temp = -1;
  int input;
  for (int i = (current_task+1) % num_tasks; ; i = (i+1) % num_tasks) {
    int not_finished = 0;
    switch (tasks[i].state) {
      case READY:
          if (i == 0) {
              //main
              for (int j = 0; j < num_tasks; j++) {
                  if (tasks[i].state != FINISHED) {
                      not_finished = 1;
                      break;
                  }
              }
              if (not_finished == 0) {
                  //all tasks except main are finished.  free everything
                  for (int j = 0; j < num_tasks; j++) {
                      free(tasks[j].context.uc_stack.ss_sp);
                      free(tasks[j].exit_context.uc_stack.ss_sp);
                  }
              }
          }
    	temp = current_task;
    	tasks[i].state = RUNNING;
    	current_task = i;
        swapcontext(&tasks[temp].context, &tasks[i].context); 
        break;
      case RUNNING:
        return -1;
      case READING:
        input = getch();
        if (input != ERR) {
            tasks[i].state = READY;
            tasks[i].input = input;
            if (i == current_task)
                return input;
            else {
                temp = current_task;
                tasks[i].state = RUNNING;
                current_task = i;
                swapcontext(&tasks[temp].context, &tasks[i].context);
            }
        }
    	break;
      case WAITING:
    	break;
      case SLEEPING:
    	if (tasks[i].wake_up != -1 && time_ms() < tasks[i].wake_up) {
           break;
    	}
    	tasks[i].wake_up = -1;
	    if (tasks[i].waiting_for == -1) {
          if (i != current_task) {
            temp = current_task;
            current_task = i;
            swapcontext(&tasks[temp].context, &tasks[i].context);
            if (tasks[current_task].state != SLEEPING) {
//              printf("task %d case %d: ", current_task, tasks[current_task].state);
              break;
            }
          }
//          printf("task %d is returning from sleep\n", current_task);
          return -1;
    	}
    	else {
    	  tasks[i].state = WAITING;
    	}
    	//possible to go to reading straight from sleeping? I don't think so
    	break;
      case FINISHED:
      default:
    	//nothing
    	break;
    }
  }
}


/**
 * This function will execute when a task's function returns. This allows you
 * to update scheduler states and start another task. This function is run
 * because of how the contexts are set up in the task_create function.
 */
void task_exit() {
  // TODO: Handle the end of a task's execution here
  tasks[current_task].state = FINISHED;
  for (int i = 0; i < num_tasks; i++) {
    if (tasks[i].waiting_for == current_task) {
      tasks[i].waiting_for = -1;
      tasks[i].state = READY;
    }
  }
  scheduler_exec();
}

/**
 * Create a new task and add it to the scheduler.
 *
 * \param handle  The handle for this task will be written to this location.
 * \param fn      The new task will run this function.
 */
void task_create(task_t* handle, task_fn_t fn) {
  // Claim an index for the new task
  int index = num_tasks;
  num_tasks++;

  // Set the task handle to this index, since task_t is just an int
  *handle = index;
 
  // We're going to make two contexts: one to run the task, and one that runs at the end of the task so we can clean up. Start with the second
  
  // First, duplicate the current context as a starting point
  getcontext(&tasks[index].exit_context);
  
  // Set up a stack for the exit context
  tasks[index].exit_context.uc_stack.ss_sp = malloc(STACK_SIZE);
  tasks[index].exit_context.uc_stack.ss_size = STACK_SIZE;
  
  // Set up a context to run when the task function returns. This should call task_exit.
  makecontext(&tasks[index].exit_context, task_exit, 0);
  
  // Now we start with the task's actual running context
  getcontext(&tasks[index].context);
  
  // Allocate a stack for the new task and add it to the context
  tasks[index].context.uc_stack.ss_sp = malloc(STACK_SIZE);
  tasks[index].context.uc_stack.ss_size = STACK_SIZE;
  
  // Now set the uc_link field, which sets things up so our task will go to the exit context when the task function finishes
  tasks[index].context.uc_link = &tasks[index].exit_context;
  
  // And finally, set up the context to execute the task function
  makecontext(&tasks[index].context, fn, 0);

  tasks[index].wake_up = -1;
  tasks[index].waiting_for = -1;
  tasks[index].state = READY;
  tasks[index].input = -1;
}

/**
 * Wait for a task to finish. If the task has not yet finished, the scheduler should
 * suspend this task and wake it up later when the task specified by handle has exited.
 *
 * \param handle  This is the handle produced by task_create
 */
void task_wait(task_t handle) {
  // TODO: Block this task until the specified task has exited.
  //so when main calls this, main suspends until task_1 is finished
  if (tasks[handle].state == FINISHED) {
    current_task = handle;
    if (scheduler_exec() == -1) {
      return;
    }
  }
  tasks[current_task].state = WAITING;
  tasks[current_task].waiting_for = handle;

  scheduler_exec();
}

/**
 * The currently-executing task should sleep for a specified time. If that time is larger
 * than zero, the scheduler should suspend this task and run a different task until at least
 * ms milliseconds have elapsed.
 * 
 * \param ms  The number of milliseconds the task should sleep.
 */
void task_sleep(size_t ms) {
  // TODO: Block this task until the requested time has elapsed.
  // Hint: Record the time the task should wake up instead of the time left for it to sleep. The bookkeeping is easier this way.
  if (ms < 0) return;
  long initial_time = time_ms();
  tasks[current_task].wake_up = initial_time + ms;
  tasks[current_task].state = SLEEPING;
  if (scheduler_exec() == -1) {
    return;
  }
}

/**
 * Read a character from user input. If no input is available, the task should
 * block until input becomes available. The scheduler should run a different
 * task while this task is blocked.
 *
 * \returns The read character code
 */
int task_readchar() {
  // TODO: Block this task until there is input available.
  // To check for input, call getch(). If it returns ERR, no input was available.
  // Otherwise, getch() will returns the character code that was read.
  tasks[current_task].state = READING;
  scheduler_exec();
  return tasks[current_task].input;
}
