#!/bin/bash

java rushhour.Main analyze --fields 
echo
for log_file in solve_logs/**/*.txt; do
	puzzle_id=${log_file:11:-37}
	if [[ ! -f puzzles/$puzzle_id.txt ]]; then
		continue;
	fi
	printf $puzzle_id,
	solve_id=${log_file/solve_logs\/$puzzle_id\/}
	solve_id=${solve_id:0:-4}
	printf $solve_id,
	puzzle_file=puzzles/$puzzle_id.txt
	java rushhour.Main analyze --csv $puzzle_file $log_file
	echo
done
