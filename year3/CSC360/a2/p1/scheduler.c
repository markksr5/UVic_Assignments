#define _XOPEN_SOURCE
#define _XOPEN_SOURCE_EXTENDED

#include "scheduler.h"

#include <assert.h>
#include <curses.h>
#include <ucontext.h>
#include <conio.h>
#include <bool.h>

#include "util.h"

// This is an upper limit on the number of tasks we can create.
#define MAX_TASKS 128

// This is the size of each task's stack memory
#define STACK_SIZE 65536

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
  
  enum state{READY, RUNNING, WAITING, READING, SLEEPING, FINISHED};
  time_t wake_up;
  int waiting_for;
  //bool user_in;
  int task_num; //not needed
} task_info_t;

int current_task = 0; //< The handle of the currently-executing task
int num_tasks = 1;    //< The number of tasks created so far
task_info_t tasks[MAX_TASKS]; //< Information for every task

/**
 * Initialize the scheduler. Programs should call this before calling any other
 * functiosn in this file.
 */
void scheduler_init() {
  // TODO: Initialize the state of the scheduler
  //create a main context (which is current_task = tasks[0]) to go back to at the end
  task_t main_task;
  task_create(&main_task, main); //???
  //run it
  //swapcontext or getcontext?
}

void scheduler_exec() {
  for (int i = current_task+1; i < num_tasks; i++) {
    switch (tasks[i]->state) {
      case READY:
	//run it
	tasks[i].state = RUNNING;
	current_task = i;
	swapcontext(&tasks[current_task].context, &tasks[i].context);
	//won't return here
	break;
      case RUNNING:
	//should never be running already right?
	//just wait
        break;
      case READING:
	//waiting for input to be read
	//i++, run next one
	break;
      case WAITING:
	//i++, run next one
	break;
      case SLEEPING:
	//check if wakeup time has been reached. if yes, check waiting_for field
	if (wake_up != -1 && clock() < wake_up) {
          break;
	}
	//else wakeup time has been reached
	tasks[i].wake_up = -1;
	if (waiting_for == NULL) {
          tasks[i].status = READY;
	  i--;
	}
	else {
	  tasks[i].status = WAITING;
	}
	//possible to go from reading straight to sleeping? I don't think so
	break;
      case FINISHED:
	//i++, run next one
	if (current_task == 0) {
          return; //main is finishing, exit program
	}
      default:
	//nothing
	break;
    }

    if (i == num_tasks - 1) {
      i = -1;
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
      tasks[i].waiting_for == NULL;
      tasks[i].status = READY;
    }
  }
  if (current_task != 0) {
    scheduler_exec();
  }
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
  tasks[current_task].state = WAITING;
  tasks[current_task].waiting_for = handle;
  int temp = current_task;
  current_task = handle;
  tasks[handle].state = RUNNING;
  swapcontext(&tasks[temp].context, &tasks[handle].context);
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
  
  //in milliseconds
  if (ms < 0) return;
  time_t initial_time = clock();
  tasks[current_task].wake_up = ms * CLOCKS_PER_SEC + (double) initial_time;
  tasks[current_task].status = SLEEPING;
  scheduler_exec();
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
  tasks[current_task].status = READING;
  char c;
  while (!(c = getch())) {
    //blocked. run a different task
  }

  return c;

  //return ERR;
}
