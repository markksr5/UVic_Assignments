#!/usr/bin/python3 

import os
import sys
import re

MAGIC_NUMBER_1 = chr(0xBA) + chr(0x5E) + chr(0xBA) + chr(0x11)
MAGIC_NUMBER_2 = chr(0xBA) + chr(0x5E) + chr(0xBA) + chr(0x12)

class Dictionary(list):
	"""class for dictionary"""

	def __init__(self, outputfile):
		self.dictionary = list()
		self.outputfile = outputfile

	def decode_add(self, word_str, word_str_stripped):
		#add new word to dictionary and output
		self.outputfile.write(word_str)
		if word_str[-1] != '\n':
			self.outputfile.write(" ")
			#print ("here")
		self.insert(0, word_str_stripped)

	def move_front(self, code, word):
		#move existing word to front of dictionary
		self.outputfile.write(self[code])
		if re.match("^\n+$", word):
			self.outputfile.write(word)
		else:
			#print ("xxxxxxxx")
			self.outputfile.write(" ")
		self.insert(0, self.pop(code))

	def encode_add(self, word_str):
		#next available index is len(self)
		self.insert(0, word_str)
		if len(self) < 121:
			self.outputfile.write(chr(len(self) + 128))
		elif len(self) < 376:
			self.outputfile.write(chr(249) + chr(len(self) - 121))
		else: 
			self.outputfile.write(chr(250) + chr((len(self) - 376) // 256) \
				+ chr((len(self) - 376) % 256))
		self.outputfile.write(word_str)

	def encode_m2f(self, index):
		if index < 120:
			self.outputfile.write(chr(index + 129))
		elif index < 375:
			self.outputfile.write(chr(249) + chr(index - 120))
		else:
			self.outputfile.write(chr(250) + chr((index - 375) // 256) + \
				chr((index - 375) % 256))
		#move to front
		self.insert(0, self.pop(index))

	def write_file(self, word):
		self.outputfile.write(word)


################## encode ####################

def encode(input_name): 
	encode_exp = re.compile('([^ \n]*)( |\n+)', re.MULTILINE)

	(base_name, _, _) = input_name.rpartition(".")
	output_name = base_name + "." + "mtf"

	#print("output file name: " + output_name)

	#open new files file_name and from command line
	inputfile = open(input_name, "r", encoding="latin-1", newline="")
	outputfile = open(output_name, "w+", encoding="latin-1", newline="")

	#print magic number
	outputfile.write(MAGIC_NUMBER_2)
	#print (MAGIC_NUMBER_2)

	#new dictionary
	dictionary = Dictionary(outputfile)

	#read all lines from file into list lines
	lines = inputfile.readlines()
	#make lines_str a string of whole file
	lines_str = "".join(lines)

	#get all matches of encode_exp
	all_words = re.findall(encode_exp, lines_str)
	#print (all_words)

	encode_all_words(all_words, dictionary)

	#print (dictionary)
	

def encode_all_words(all_words, dictionary):
	for word in all_words:
		index = check_dict(word[0], dictionary)
		#print (index)
		if index == -1:
			#new word, not in dictionary
			dictionary.encode_add(word[0])
		else:
			#repeated word
			dictionary.encode_m2f(index)
		if re.search("\n+", word[1]):
			dictionary.write_file(word[1])

def check_dict(word, dictionary):
	for entry in dictionary:
		if entry == word:
			return dictionary.index(entry)
	return -1


################## decode #################

def decode(input_name):
	expression = re.compile\
		('(([\x81-\xf8])|(\xf9[\x00-\xff])|(\xfa[\x00-\xff][\x00-\xff]))?([\x00-\x7f]*)', re.MULTILINE)
	
	(base_name, _, _) = input_name.rpartition(".")
	output_name = base_name + "." + "txt"

	#print("output file name: " + output_name)

	#open new files file_name and from command line
	inputfile = open(input_name, "r", encoding="latin-1", newline="")
	outputfile = open(output_name, "w+", encoding="latin-1", newline="")

	#new dictionary
	dictionary = Dictionary(outputfile)


	lines = inputfile.readlines()
	#print (lines)
	#check if magic number is present and make sure we don't decode \
	#magic number
	extract_magic_number(lines)

	#get string of whole file
	lines_str = "".join(lines)
	#print (lines_str)
	
	all_words = re.findall(expression, lines_str)
	#for word in all_words
	#word[0] is the code
	#word[1] is the code for case 1, otherwise empty
	#word[2] is the code for case 2, otherwise empty
	#word[3] is the code for case 3, otherwise empty
	#word[4] is the word, empty if repeated word
	#print ("all_words")
	#print (all_words)
	#for word in all_words:
		#print (word[0] + ", " + word[1] + ", " + word[2] + ", " + word[3])

	check_all_words(all_words, dictionary)


def check_all_words(all_words, dictionary):
	for word in all_words:
		if word[1] != "":
			#first case
			code = ord(word[1]) - 128
			word_str = word[4]
			word_str_stripped = word_str.strip()
			if word_str_stripped != "":
				#new word
				dictionary.decode_add(word_str, word_str_stripped)
			else:
				#repeated word
				dictionary.move_front(code-1, word_str)
		elif word[2] != "":
			#second case
			code = ord(word[2][1]) + 121
			#second char of word[2]
			word_str = word[4]
			word_str_stripped = word_str.strip()
			if word_str_stripped != "":
				#new word
				dictionary.decode_add(word_str, word_str_stripped)
			else:
				#repeated word
				dictionary.move_front(code-1, word_str)
		elif word[3] != "":
			code = ord(word[3][1]) * 256 + 376 + ord(word[3][2])
			#print(code)
			#print (dictionary[code-2])
			word_str = word[4]
			word_str_stripped = word_str.strip()
			if word_str_stripped != "":
				#new word
				dictionary.decode_add(word_str, word_str_stripped)
			else:
				#repeated word
				dictionary.move_front(code-1, word_str)
		else:
			dictionary.write_file(word[4])
			#print ("word with no code: " + str(word))
	#print (dictionary)


def extract_magic_number (lines):
	first_line_str = lines[0]
	#print (first_line_str)
	magic_number = str(first_line_str[:4])
	#print (magic_number)
	check_magic_number(magic_number)
	removed_magic = first_line_str[4:]
	lines[0] = removed_magic
	#print (lines)


def check_magic_number (file_number):
	#magic_number = [chr(186),chr(94),chr(186),chr(18)]
	#print (magic_number)
	#print (file_number)
	if file_number != MAGIC_NUMBER_2 and file_number != MAGIC_NUMBER_1:
		#raise error
		print ("no magic number, exiting")
		raise ValueError('no magic number, exiting')
		sys.exit(0)