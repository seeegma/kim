#!/bin/bash

printf "puzzle_id,"
java rushhour.Main evaluate --fields
echo
for puzzle_file in generated_puzzles/*.txt; do
	puzzle_id=${puzzle_file/generated_puzzles\//}
	puzzle_id=${puzzle_id%.txt}
	printf "$puzzle_id,"
	java rushhour.Main evaluate --csv "$puzzle_file"
	echo
done
