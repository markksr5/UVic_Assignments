#!/bin/sh

#test encoding text2mtf.py

python3 text2mtf.py tests/test00.txt
python3 text2mtf.py tests/test01.txt
python3 text2mtf.py tests/test02.txt
python3 text2mtf.py tests/test03.txt
python3 text2mtf.py tests/test04.txt
python3 text2mtf.py tests/test05.txt
python3 text2mtf.py tests/test06.txt
python3 text2mtf.py tests/test07.txt
python3 text2mtf.py tests/test08.txt
python3 text2mtf.py tests/test09.txt
python3 text2mtf.py tests/test10.txt

./check_diff_encoding.sh
