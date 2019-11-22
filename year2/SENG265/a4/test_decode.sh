#!/bin/sh

#test decoding coding2.c (decode)

echo in test_decoding

make
./mtf2text2 tests/test00.mtf
./mtf2text2 tests/test01.mtf
./mtf2text2 tests/test02.mtf
./mtf2text2 tests/test03.mtf
./mtf2text2 tests/test04.mtf
./mtf2text2 tests/test05.mtf
./mtf2text2 tests/test06.mtf
./mtf2text2 tests/test07.mtf
./mtf2text2 tests/test08.mtf
./mtf2text2 tests/test09.mtf
./mtf2text2 tests/test10.mtf
./mtf2text2 tests/test11.mtf
./mtf2text2 tests/test12.mtf
./mtf2text2 tests/test13.mtf
./mtf2text2 tests/test14.mtf
./mtf2text2 tests/test15.mtf
./mtf2text2 tests/test16.mtf
./mtf2text2 tests/test17.mtf
./mtf2text2 tests/test18.mtf
./mtf2text2 tests/test19.mtf

./check_diff_decoding.sh
