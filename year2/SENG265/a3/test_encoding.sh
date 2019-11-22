#!/bin/sh

#test encoding encode.py

echo in test_encoding

python3 encode.py encode_tests/test11.txt
python3 encode.py encode_tests/test12.txt
python3 encode.py encode_tests/test13.txt
python3 encode.py encode_tests/test14.txt
python3 encode.py encode_tests/test15.txt
python3 encode.py encode_tests/test16.txt
python3 encode.py encode_tests/test17.txt
python3 encode.py encode_tests/test18.txt
python3 encode.py encode_tests/test19.txt

./check_diff_encoding.sh
