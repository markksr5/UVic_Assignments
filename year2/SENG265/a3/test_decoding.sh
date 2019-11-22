#!/bin/sh

#test decoding decode.py

echo in test_decoding

python3 decode.py decode_tests/test00.mtf
python3 decode.py decode_tests/test01.mtf
python3 decode.py decode_tests/test02.mtf
python3 decode.py decode_tests/test03.mtf
python3 decode.py decode_tests/test04.mtf
python3 decode.py decode_tests/test05.mtf
python3 decode.py decode_tests/test06.mtf
python3 decode.py decode_tests/test07.mtf
python3 decode.py decode_tests/test08.mtf
python3 decode.py decode_tests/test09.mtf
python3 decode.py decode_tests/test10.mtf
python3 decode.py decode_tests/test11.mtf
python3 decode.py decode_tests/test12.mtf
python3 decode.py decode_tests/test13.mtf
python3 decode.py decode_tests/test14.mtf
python3 decode.py decode_tests/test15.mtf
python3 decode.py decode_tests/test16.mtf
python3 decode.py decode_tests/test17.mtf
python3 decode.py decode_tests/test18.mtf
python3 decode.py decode_tests/test19.mtf

./check_diff_decoding.sh
