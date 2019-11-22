#!/usr/bin/env python3 # For use on ELW B238 machines
# -*- coding: utf-8 -*-
# See https://www.python.org/dev/peps/pep-0263/ about encoding
import os
import sys

def encode_main():
	#print("In encode_main: {}".format(str(sys.argv[1])))
	file_name = str(get_file_name(False))
	#open new files file_name and from command line
	inputfile = open(str(sys.argv[1]), "r", encoding="latin-1")
	outputfile = open(file_name, "w+", encoding="latin-1")

	#print magic number
	outputfile.write("{}{}{}{}".format(chr(186),chr(94),chr(186),chr(17)))

	dictionary = list()
	repeated = False

	#read all lines from file into list lines
	lines = inputfile.readlines()

	for line in lines:
		line = line.strip()
		words = line.split(" ")
		#print line
		#print words
		
		for word in words:
			if word == '':
				#line is just new line (stripped)
				#print "just new line"
				break
			repeated = False
			for dict_entry in dictionary:
				if word == dict_entry:
					#repeated word, output index of dictionary + 128
					repeated = True
					#print dictionary.index(word)
					outputfile.write(str(chr(dictionary.index(word) + 129)))
					#pop repeated word and insert at front
					dictionary.insert(0, dictionary.pop(dictionary.index(word)))
					break

			if repeated == False:
				#new word, add to dictionary
				dictionary.insert(0, word)
				#output to file: index of first unused position and word
				#print "{}{}".format(len(dictionary), word)
				outputfile.write("{}{}".format(chr(len(dictionary)+128), word))
			#print dictionary
		outputfile.write("\n")

	#close files
	inputfile.close()
	outputfile.close()

def get_file_name(txt):
	#get filename and append .txt or .mtf
	#print "argv: {}".format(str(sys.argv))
	#argv[0] is text2mtf.py, argv[1] is test file name
	#print file_name
	file_name = str(sys.argv[1]).split(".")
	file_name = file_name[0].split("/") #remove this line if we want to keep the dir
	if txt == False:
		file_name = file_name[-1] + ".mtf"	#test01 etc
	else:
		file_name = file_name[-1] + ".txt"
	#print (file_name)
	return file_name


#################### decode ##########################


def decode_main():
	#print("In decode_main: {}".format(str(sys.argv[1])))
	file_name = str(get_file_name(True))
	#open new files file_name and from command line
	inputfile = open(str(sys.argv[1]), "r", encoding="latin-1")
	outputfile = open(file_name, "w+", encoding="latin-1")

	dictionary = list()
	first_line = 1

	#read all lines into list lines
	lines = inputfile.readlines()
	#print (lines)

	for line in lines:
		if first_line == 1:
			#if first line, do not decode magic number
			magic_number = list(line[:4])
			check_magic_number(magic_number)
			line = list(line[4:])
			first_line = 0
		space = ""
		word_string = ""
		#line = list of chars in line
		line = list(line)
		#print (line)
		for char in line:
			if char == '\n' and word_string != "":
				#new line char at end of line, add word_string to dict
				dictionary.insert(0, word_string)
				#print ("inserted: {}".format(word_string))
			if ord(char) > 128:
				#ascii code is a dictionary index
				#if index exists, print out word at index
				#if not read next chars til next ascii

				if word_string != '\n' and word_string != '':
					#add word_string to dictionary
					dictionary.insert(0, word_string)
					#print ("inserted: {}".format(word_string))
					word_string = ""
				else:
					outputfile.write(word_string)
				if ord(char) <= len(dictionary) + 128:
					#repeated word, char is dict index
					#print (dictionary[ord(char) - 129])
					outputfile.write(space)
					space = " "
					#output repeated word from dict to file
					outputfile.write(dictionary[ord(char) - 129])
					#move indexed word to dictionary[0]
					dictionary.insert(0, dictionary.pop(ord(char) - 129))
				else:
					#index does not exist yet, new word coming
					outputfile.write(space)
					space = " "
			else:
				#char is character to be printed out
				#print (char)
				outputfile.write(char)
				#add char to word to eventually add to dict
				word_string += char
		#print (dictionary)

def check_magic_number (file_number):
	magic_number = [chr(186),chr(94),chr(186),chr(17)]
	if file_number != magic_number:
		#raise error
		print ("no magic number, exiting")
		raise ValueError('no magic number, exiting')
		sys.exit(0)






command = os.path.basename(__file__)
if __name__ == "__main__" and command == "text2mtf.py":
	encode_main()
elif __name__ == "__main__" and command == "mtf2text.py":
	decode_main()