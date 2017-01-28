#!/bin/bash

for puzzle_file in puzzles/*.txt; do
	puzzle_id=${puzzle_file/puzzles\//}
	puzzle_id=${puzzle_id%.txt}
	echo $puzzle_id
done
