#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <libgen.h>
#include <signal.h>

#include "errors.h"
#include "helpers.h"

#define BUF_SIZE 512
#define NUM_BUILT_INS 9
#define HISTORY_SIZE 8

char * cd(char **args);
char * help(char **args);
char * exit_shell(char **args);
char * set_var(char **args);
char * unset_var(char **args);
char * pwd(char **args);
char * history(char **args);
char * history_call(char **args);
char * get_var(char **args);

//char ** allocated;
char history_arr[100][BUF_SIZE];
int history_arr_length = 0;

char *built_in[] = {"cd", "help", "exit", "set", "unset", "pwd", "history", "!", "get_var"};

char *built_in_desc = \
" cd [dir]: Change directory\n \
help: Display built in function information\n \
exit: Exit SEEsh\n \
set [var] [new_value]: Set or modify an environment variable\n \
unset [var]: Delete an environment variable\n \
pwd: Display full filename of working directory\n \
history: Show previous 100 commands\n \
![previous_command]: Recall a previously executed command";

char * (*functions[]) (char **) = {&cd, &help, &exit_shell, &set_var, &unset_var, &pwd, &history, &history_call, &get_var};

void handle_sigign() {
	return;
}

void add_to_history(char *line) {
	int i;
	for (i = 0; i < history_arr_length; i++) {
		if (strcmp(history_arr[i], line) == 0) 
			return;
	}
	strncpy(history_arr[i++], line, BUF_SIZE);
	history_arr_length = i;
}

char * exit_shell(char **args) {
	exit(EXIT_SUCCESS);
}

bool read_line(char *line) {
	int i = 0;
	int c = getchar();
	while (c != '\n' && c != EOF) {
		line[i++] = c;
		if (i >= BUF_SIZE) {
			printf("Exceeded buffer of 512 characters. Please enter another command.");
			clear_array(line, &i);
			break;
		}
		c = getchar();
	}
	if (c == EOF) {
		exit_shell(&line);
		return false;
	}
	line[i] = '\0';
	add_to_history(line);
	return true;
}

char **tokenize_line(char *line, char **argv) {
	char *token;
	char **tokens = (char**)malloc(BUF_SIZE * sizeof(char*));
	
	char *whitespace = " \t\r\v\f\n";
	int i = 0;
	if (!tokens) {
		printf("malloc failed. Exiting.");
		exit(EXIT_FAILURE);
	}
	token = strtok(line, whitespace);
	while (token != NULL) {
		tokens[i++] = token;
		token = strtok(NULL, whitespace);
	}
	tokens[i] = NULL;
	return tokens;
}

char * fork_and_exec(char **args) {
	pid_t pid;
	int status;
	pid = fork();
	if (pid == 0) {
		if (execvp(args[0], args) == -1) {
			perror("error");
			return handle_exec_error();
		}
	}
	else if (pid < 0) {
		return handle_fork_error();
	}
	else {
		do {
			waitpid(pid, &status, WUNTRACED);
		} while (!WIFEXITED(status) && !WIFSIGNALED(status));
	}
	return "";
}

char * execute_seesh(char **tokens) {
	bool built_in_executed = false;
	if (tokens[0] == NULL) {
		return "";
	}
	for (int i = 0; i < NUM_BUILT_INS; i++) {
		if (strcmp(tokens[0], built_in[i]) == 0) {
			built_in_executed = true;
			return (*functions[i])(tokens);
		}
	}
	if (!built_in_executed) {
		return fork_and_exec(tokens);
	}
	printf("Something went wrong.");
	return exit_shell(tokens);
}

char * cd(char **args) {
	bool status = false;
	if (args[1] == NULL) {
		status = chdir(getenv("HOME"));
	}
	else {
		status = (chdir(args[1]) == 0);
		if (!status) return cd_invalid();
	}
	return pwd(args);
}

char * pwd(char **args) {
	char buf[BUF_SIZE];
	return getcwd(buf, BUF_SIZE);
}

char * help(char **args) {
	return built_in_desc;
}

char * set_var(char **args) {
	if (args[1] == NULL) {
		return set_no_var();
	}
	if (args[2] == NULL) {
		return set_no_value(args);
	}

	if (setenv(args[1], args[2], 1) != 0) {
		return handle_var_error(0);
	}
	
	return strcat(args[1], " set.");
}

char * unset_var(char **args) {
	if (args[1] == NULL) {
		return handle_no_var_error();
	}
	if (unsetenv(args[1]) != 0) {
		return handle_var_error(1);
	}
	return strcat(args[1], " removed.");
}

char * history(char ** args) {
	for (int i = 0; i < history_arr_length; i++) {
		printf("%s\n", history_arr[i]);
	}	
	return "";
}

char * history_call(char **args) {
	return "";
}

void call_seesh(char **argv) {
	char line[BUF_SIZE];
	char **tokens;
	char *result;
	bool status = true;

	while (1) {
		printf("? ");
		status = read_line(line);
		if (!status) return;
		tokens = tokenize_line(line, argv);
		result = execute_seesh(tokens);
		printf("%s\n", result);
	}
	free(tokens);
}

void read_rc(char *name, char **argv) {
	FILE * rc = fopen(name, "r");
	char all_chars[BUF_SIZE * 100]; //able to handle 100 full lines in the rc file - a very lazy way to do it
	char *whitespace = " \t\r\v\f\n";
	char *token;
	char **tokens = (char**)malloc(BUF_SIZE * sizeof(char*));
	char *result;
	int k;
	while(fgets(all_chars, BUF_SIZE * 100, rc)) {
		printf("? %s", all_chars);
		token = strtok(all_chars, whitespace);
		k = 0;
		while (token) {
			tokens[k++] = token;
			token = strtok(NULL, whitespace);
		}
		result = execute_seesh(tokens);
		printf("%s\n", result);
		for (int a = 1; a < k; a++) {
			tokens[a] = NULL;
		}
	}

	free(tokens);
	fclose(rc);
}

int main(int argc, char **argv) {
	signal(SIGINT, handle_sigign);
	printf("You are using the %s shell\n", argv[0]);
	read_rc(".SEEshrc", argv);
	call_seesh(argv);
	return 0;
}
