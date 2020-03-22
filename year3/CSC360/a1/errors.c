#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <libgen.h>

#include "errors.h"
#include "helpers.h"

#define BUF_SIZE 512
#define NUM_BUILT_INS 9
#define HISTORY_SIZE 8

char * handle_exec_error() {	
	printf("exec() failed. Please enter another command, or exit.");
    exit(EXIT_SUCCESS);
}

char * handle_fork_error() {	
	return "fork() failed. Please enter another command, or exit.";
}

char * cd_home_invalid() {
	return "Could not cd to HOME.";
}

char * cd_invalid() {
	return "Invalid argument. Folder does not exist.";
}

char * handle_no_var_error() {
	return "Requires an environment variable argument.\nunset [var]";
}

char * handle_no_value_error() {
	return "Requires a value argument.\nset [var] [value]";
}

char * handle_var_error(int set) {
	if (set == 0)
		return "Something went wrong in set var.";
	else
		return "Something went wrong in unset var.";
}

