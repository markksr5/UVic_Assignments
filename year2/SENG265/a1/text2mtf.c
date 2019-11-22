/* Mark Kaiser
 * V00884677
 * SENG 265 Assignment 1 */

#include <stdio.h>
#include <string.h>
#define max_length 1000

int main(int args, char *argv[]) {
  FILE *inputfile = fopen(argv[1], "r");
  char *inputfile_name = argv[1];
  int input_name_length = strlen(inputfile_name);
  inputfile_name[input_name_length - 3] = 'm';
  inputfile_name[input_name_length - 2] = 't';
  inputfile_name[input_name_length - 1] = 'f';

  FILE *outputfile = fopen(inputfile_name, "w");
  fprintf(outputfile, "\xba\x5e\xba\x11"); /*ba 5e ba 11*/

  if (inputfile == NULL) {
    printf("No input file\n");
    return 0;
  }

  if (outputfile == NULL) {
  	printf("No output file\n");
  	return 0;
  }

  char *token;
  char str[max_length];
  char dict[max_length][max_length];
  strncpy(dict[0], "\0", 1);
  int ascii = 128;
  int num_words = 0;
  int return_line = -1;

  while (fgets(str, sizeof(char) * max_length, inputfile) != NULL) {
  	/*token points to first word*/
  	token = strtok(str, " ");

  	/*printf("str: %s", str);*/
  	/*printf("first word: %s\n", token);*/
  	while (token != NULL) {
  		int i;
  		if (num_words == 0) {
  			if (token[strlen(token) - 1] == '\n') {
  				if (strcmp(token, "\n") == 0) {
  					/*string is only new line char*/
  					fprintf(outputfile, "\n");
  					break;
  				}
	    		/*if last char is \n, replace with \0 for comparison*/
	    		token[strlen(token) - 1] = '\0';
	    		/*notes \n at end of line to print to file*/
	    		return_line = 1;
	    	}
	    	/*first word: insert into dict[1], num_words = 1;*/
  			strncpy(dict[1], token, strlen(token));
  			num_words = 1;
  			fprintf(outputfile, "%c%s", ascii+1, dict[1]);
  			if (return_line == 1) {
  				fprintf(outputfile, "\n");
  				return_line = -1;
  			}
  			/*token -> next word*/
  			token = strtok(NULL, " ");
  			/*skip next for loop: start with next word*/
  			continue;
  		}
  		if (strcmp(token, "\n") == 0) {
				/*string is just new line*/
				fprintf(outputfile, "\n");
				break;
			}
  		for (i = 1; i <= num_words && token != NULL; i++) { 
  			/*loops to check every dict word against token*/
	    	if (token[strlen(token) - 1] == '\n') {
	    		/*if last char is \n, replace with \0 for comparison*/
	    		token[strlen(token) - 1] = '\0';
	    		/*notes \n at end of line to print to file*/
	    		return_line = 1;
	    	}
	    	if (strcmp(token, dict[i]) == 0) { 
	    		/*word is repeated
	    			print ascii char*/
	    		fprintf(outputfile, "%c", ascii+i);
	    		/*shift dict array down*/
	    		int j;
	    		for (j = i; j > 1; j--) {
			        strncpy(dict[j], dict[j-1], strlen(dict[j-1]));
			        dict[j][strlen(dict[j-1])] = '\0';
			    }
			    /*bring token to front of dict (dict[1])*/
			    strncpy(dict[1], token, strlen(token));
			    dict[1][strlen(token)] = '\0';
			    /*get next word*/
			    break;
	    	}
	    	else if (i >= num_words) {
	    		/*passed through whole dict, word is new*/
	    		/*i == numwords: reaches last word and is not equal*/
	    		int m;
	    		num_words++;
	    		for (m = num_words; m >= 1; m--) {
	    			/*shift all words down*/
	    			strncpy(dict[m+1], dict[m], strlen(dict[m]));
	    			dict[m+1][strlen(dict[m])] = '\0';
	    		}
	    		/*copy token into dict[1]*/
	    		strncpy(dict[1], token, strlen(token));
	    		dict[1][strlen(token)] = '\0';
	    		/*print ascii code + word*/
	    		fprintf(outputfile, "%c%s", ascii+num_words, token);
	    		/*
	    		int k;
	    		for (k = 1; k <= num_words; k++) {
	    			printf("k: %d, %s\n", k, dict[k]);
	    		}*/

	    		/*get new word*/
	    		break;
	    	}
	    	
  		} /*checks next dict word*/

  		if (return_line == 1) { 
  			/*print out \n (end of line)*/
	    	fprintf(outputfile, "\n");
	    	return_line = -1;
	    }
  		token = strtok(NULL, " "); /*gets next word*/
    	/*printf("%s", token);*/
    	
    } /*gets next word*/

  }/*gets next line (fgets)*/

  /* int i = 1;
  printf("end\n");
  for (i = 1; i <= num_words; i++) {
  	printf("dict: %s\n", dict[i]);
  }*/

	fclose(inputfile);
	fclose(outputfile);
	
	return 0;
}