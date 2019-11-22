#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "coding2.h"

char  magic_number_1[] = {0xBA, 0x5E, 0xBA, 0x11, 0x00};
char  magic_number_2[] = {0xBA, 0x5E, 0xBA, 0x12, 0x00};

char *space;

struct Entry {
	/*linked list structure for dictionary*/
	char *word;
	struct Entry *next;
};


void push (int *stack, unsigned char ch, int index) {
	/*printf("pushing %d\n", ch);*/
	stack[index] = ch;
	return;
}


void pop(int *stack) {
	/*printf("popping stack\n");*/
	int i;
	for (i = 0; i < 4; i++) {
		stack[i] = -1;	
	}
	return;
}


struct Entry* create_node(char *new_word) {
	struct Entry *new_node = (struct Entry*) malloc(sizeof(struct Entry));
	new_node->word = (char *) malloc(sizeof(char) * strlen(new_word));

	/*can't use reference(word), must copy into new_node->word*/
	strcpy(new_node->word, new_word);
	new_node->next = NULL;
	return new_node;
}


struct Entry* insert(struct Entry *new_node, struct Entry *head, int *dict_length) {
	new_node->next = head;
	head = new_node;
	strcpy(head->word, new_node->word);
	/*printf("insert head: %s\n", head->word);*/
	(*dict_length)++;
	return new_node;
}


struct Entry* move2front(FILE* output, int index, struct Entry *head) {
	/*also prints to file*/
	/*printf("in move2front\n");*/
	if (index == 0) {
		//printf("outputting %s\n", head->word);
		fprintf(output, space);
		fprintf(output, head->word);
		space[0] = ' ';
		return head;
	}
	struct Entry *curr;
	struct Entry *prev;
	struct Entry *move;

	curr = head;
	prev = NULL;
	int i;

	for (i = 0; i < index; i++, prev = curr, curr = curr->next);

	move = curr;
	curr = curr->next;
	if (prev != NULL && curr != NULL) {
		prev->next = curr;
	} else if (prev != NULL) {
		/*curr is null*/
		prev->next = NULL;
	}

	move->next = head;
	head = move;
	/*printf("outputting %s\n", head->word);*/
	fprintf(output, space);
	fprintf(output, head->word);
	space[0] = ' ';
	return head;
}


int check_dict(int index, int *dict_length) {
	if (index >= *dict_length) {
		/*new word; code > dict_length*/
		return -1;
	}
	return 0;
}


int check_stack(FILE* output, int *stack, unsigned char ch) {
	unsigned char x80 = '\x80';
	unsigned char x81 = '\x81';
	unsigned char xf8 = '\xf8';
	unsigned char xf9 = '\xf9';
	unsigned char xfa = '\xfa';

	if (stack[0] == -1) {
		/*printf("cs0, stack[0]: %x %d \n\n", stack[0], stack[0]);*/
		/*push any ascii code onto stack, except newline*/
		return 0;
	}
	if (x81 <= stack[0] && stack[0] <= xf8) {
		/*ascii code from case 1 on stack*/
		/*printf("cs1, stack[0]: %x %d \n\n", stack[0], stack[0]);*/
		return 1;
	}
	if (stack[0] == xf9) {
		if (stack[1] == -1) {
			/*looking for second ascii code in case 2*/
			return -2;
		}
		/*stack has both ascii codes for case 2*/
		return 2;
	}
	if (stack[0] == xfa) {
		if (stack[1] == -1) {
			/*looking for second and third ascii code in case 3*/
			return -3;
		}
		else if (stack[2] == -1) {
			/*looking for third ascii code in case 3*/
			return -4;
		}
		/*stack has all 3 ascii codes for case 3*/
		return 3;
	} else {
		/*something unexpected happened*/
		/*printf("***\ncheck stack failed: %d %d \n\n", stack[0], '\x7f');*/
		return -1;
	}
}


int check_if_code (FILE* output, int *stack, unsigned char ch) {
	unsigned char x80 = '\x80';
	unsigned char x81 = '\x81';
	unsigned char xf8 = '\xf8';
	unsigned char xf9 = '\xf9';
	unsigned char xfa = '\xfa';

	if (x81 <= ch && ch <= xf8)
		return 1;
	else if (ch == xf9)
		return 1;
	else if (ch == xfa)
		return 1;
	else
		return 0;
}


int check_magic_number (FILE *input) {
	char magic[5];
	int i;
	int c;
	for (i = 0; i < 4; i++) {
		/*get first 4 chars, check magic number*/
		if ((c = fgetc(input)) != EOF) {
			/*printf("c: %d %c\n");*/
			magic[i] = (char)c;	
		} else {
			/*printf("feof in check_magic_number\n\n");*/
			return -1;
		}
	}
	magic[5] = '\x00';
	if (!strcmp(magic, magic_number_1) && !strcmp(magic, magic_number_2)) {
		/*printf("magic number not found\n");*/
		exit(0);
	}
	/*printf("magic number found: %x %x %x %x\n\n", magic[0],
		magic[1], magic[2], magic[3]);*/
}


int append_word (char *word, char *ch, int *word_length) {
	if (strlen(word) >= *word_length) {
		word = realloc(word, *word_length * 2);
		if (word == NULL) {
			printf("word points to null\n");
			exit(0);
		}
	}
	strncat(word, ch, 1);
	*word_length *= 2;
	return 0;
}



/*
 * The encode() function may remain unchanged for A#4.
 */

int encode(FILE *input, FILE *output) {
    return 0;
}


int decode(FILE *input, FILE *output) {
	if (input == NULL) {
		printf("file is null\n");
		exit(0);
	}

	printf("in decode\n\n");
	/*get and check magic number*/
	int i;
	check_magic_number(input);
	

	int *stack = (int *) malloc(sizeof(int) * 4);
	if (stack == NULL) {
		printf("stack references null\n");
		exit(0);
	}
	int dict_length = 0;

	for (i = 0; i < 4; i++) {
		stack[i] = '\x80';
	}
	/*printf("original stack: %x %x %x %x\n", stack[0], stack[1], stack[2], stack[3]);
	printf("original stack status: %d\n\n", check_stack(output, stack, '\x80'));*/
	
	struct Entry *head = (struct Entry*) malloc(sizeof(struct Entry));
	if (head == NULL) {
		printf("head references null\n");
		exit(0);
	}

	char ch;
	int c;
	unsigned char uch;
	int code;
	int word_length = 2;
	char *word = (char *) malloc(sizeof(char) * word_length);
	if (word == NULL) {
		printf("word references null\n");
		exit(0);
	}
	word[0] = '\0';

	space = (char *) malloc(sizeof(char) * 2);
	if (space == NULL) {
		printf("space references null\n");
		exit(0);
	}
	space[0] = '\0';
	space[1] = '\0';

	int push1 = 0;
	int push2 = 0;

	i = 5;

	while (1) {
		/*printf("\n**\ni: %d\n", i);*/

		if ((c = fgetc(input)) == EOF) {
			/*printf("feof %d\n", i);
			printf("word: %s\n", word);*/
			if (word[0] != '\0') {
				fprintf(output, word);
				fprintf(output, "\n");
			}
			break;
		}
		/*printf("c: %d %c %x\n\n", c, (char)c, c);*/
		ch = (char) c;
		uch = (unsigned char) c;

		if (i == 5) {
			/*first char read*/
			push(stack, uch, 0);
			i++;
			continue;
		}

		/*checks if ch = fgetc is a valid (first) code for any case*/
		int is_code = check_if_code(output, stack, ch);
		/*printf("is_code: %d\n", is_code);*/

		int stack_status = check_stack(output, stack, uch);
		/*printf("stack status: %d\n", stack_status);*/

		if (push1 == 1) {
			/*pushes char into stack[1]*/
			push(stack, uch, 1);
			push1 = 0;
			/*printf("push1\n");*/
			/*printf("stack: %x %x %x %x\n", stack[0], stack[1], stack[2], stack[3]);*/
			if (check_stack(output, stack, uch) == -4){
				/*third case, need to push next ch as code2*/
				/*printf("accessing push2\n");*/
				push2 = 1;
			}
			i++;
			continue;
		}

		if (push2 == 1) {
			/*pushes char into stack[2]*/
			push(stack, uch, 2);
			push2 = 0;
			/*printf("push2\n");*/
			/*printf("stack: %x %x %x %x\n", stack[0], stack[1], stack[2], stack[3]);*/
			i++;
			continue;
		}

		if (is_code == 1) {
			pop(stack);
			push(stack, uch, 0);
		}

		stack_status = check_stack(output, stack, uch);
		/*printf("stack status: %d\n", stack_status);*/

		if (is_code == 1) {
			/*char is a code, so old code is not needed anymore*/
			/*output and reset word to empty if not empty*/
			if (word[0] != '\0') {
				/*word is not empty: add word to dict, output word*/
				/*printf("word from last round: %s\n", word);*/
				struct Entry *new_node = 
					create_node(word);
				/*printf("1 ");*/
				head = insert(new_node, head, &dict_length);
				/*printf("word: %s\n", word);
				printf("word: %s\n", head->word);*/

				fprintf(output, space);
				fprintf(output, word);
				space[0] = ' ';
			}
			word[0] = '\0';
			word_length = 2;
		}

		/*cannot be code*/
		if (ch == '\n' && stack_status > -2) {
			if (word[0] != '\0') {
				/*word is not empty: add word to dict, output word*/
				struct Entry *new_node = 
					create_node(word);
				/*printf("2 ");*/
				head = insert(new_node, head, &dict_length);
				/*printf("word: %s\n", word);
				printf("word: %s\n", head->word);*/

				fprintf(output, space);
				fprintf(output, word);
				space[0] = ' ';
			}
			word[0] = '\0';
			word_length = 2;
			fprintf(output, "\n");
			space[0] = '\0';
			i++;
			/*printf("\n");*/
			continue;
		}

		switch (stack_status) {
			case 0:
				/*stack is empty, should never happen*/
				printf("STACK IS EMPTY, SHOULD NOT HAPPEN\n");
				exit(0);
			case 1:
				/*stack has code: case 1*/
				code = stack[0] - 129;
				if (ch == '\n') {
					space[0] = '\0';
					break;
				}
				if (check_dict(code, &dict_length) == 0) {
					/*repeated word*/
					head = move2front(output, code, head);
					/*next byte must be another code if repeated word*/
				} else if (is_code != 1) {
					/*char is not a code*/
					/*new word, may not be finished yet, append to word*/
					append_word(word, &ch, &word_length);
				}
				break;
			case -2:
				/*stack has code: case 2, need to push second byte*/
				push1 = 1;
				break;
			case 2:
				/*stack has both ascii codes for case 2*/
				if (ch == '\n') {
					space[0] = '\0';
					break;
				}
				code = (int)stack[1] + 122;
				if (check_dict(code, &dict_length) == 0) {
					/*repeated word*/
					move2front(output, code, head);
				} else if (is_code != 1) {
					/*new word, may not be finished yet, append to word*/
					append_word(word, &ch, &word_length);
				}
				break;
			case -3:
				/*case 3: looking for second and third ascii code*/
				push1 = 1;
				break;
			case -4:
				/*case 3: looking for third ascii code*/
				push2 = 1;
				break;
			case 3:
				/*case 3: all 3 codes in stack*/
				if (ch == '\n') {
					space[0] = '\0';
					break;
				}
				code = stack[1] * 256 + 376 + stack[2] + 1;
				/*printf("code = %d\n", code);*/
				if (check_dict(code, &dict_length) == 0) {
					/*repeated word*/
					move2front(output, code, head);
				} else if (is_code != 1) {
					/*new word, may not be finished yet, append to word*/
					/*printf("word: %s, ch: %c\n", word, ch);*/
					append_word(word, &ch, &word_length);
				}
				break;
			default:
				/*should never get here*/
				break;
		}
		i++;
	}

    return 0;
}