#!/bin/sh

#decoding mtf2text.py

python3 mtf2text.py tests/test00.mtf
python3 mtf2text.py tests/test01.mtf
python3 mtf2text.py tests/test02.mtf
python3 mtf2text.py tests/test03.mtf
python3 mtf2text.py tests/test04.mtf
python3 mtf2text.py tests/test05.mtf
python3 mtf2text.py tests/test06.mtf
python3 mtf2text.py tests/test07.mtf
python3 mtf2text.py tests/test08.mtf
python3 mtf2text.py tests/test09.mtf
python3 mtf2text.py tests/test10.mtf

./check_diff_decoding.sh
