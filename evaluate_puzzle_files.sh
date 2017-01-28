#!/bin/bash

printf "puzzle_id,"
java rushhour.Main evaluate --fields
echo
for puzzle_file in puzzles/*.txt; do
	puzzle_id=${puzzle_file/puzzles\//}
	puzzle_id=${puzzle_id%.txt}
	printf "$puzzle_id,"
	java rushhour.Main evaluate --csv "$puzzle_file"
	echo
done
