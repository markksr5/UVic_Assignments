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

extern char **environ;

void clear_array(char *arr, int *length) {
	for (int i = 0; i < *length; i++) {
		arr[i] = '\0';
	}
	*length = 0;
}

char * get_var(char **args) {
	//seg fault when getting a var that doesn't exist
	if (getenv(args[1]) == NULL) {
		return strcat(args[1], " is not an environment variable.\n");
	}
	return getenv(args[1]);
}

char *set_no_var() {
    // for (char **env = envp; *env != 0; env++) {
    //     char *thisEnv = *env;
    //     printf("%s\n", thisEnv);    
    // }
    // return "";
    int i = 0;
    while (environ[i]) {
        printf("\n%s", environ[i++]);
    }
    return "";
}

char *set_no_value(char **args) {
    if (setenv(args[1], "", 1) != 0) {
        return handle_var_error(0);
	}
    else {
        return strcat(args[1], " set to the empty string.");
    }
}