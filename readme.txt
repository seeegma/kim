RUSH HOUR!!!
============

authors: dyl & ben

- To set up the code for running: run `./setup.sh`, which will extract the .jar files (included) and make the project

- To use the code: mainly, see the code's built-in usage. Here are some examples:

    - Print information about a board:

`java rushhour.Main info puzzles/for_table/49.txt`

    - Learn the optimal weights for the three features, print them, then compute error on the dev set:

`java rushhour.Main learn puzzles/datasets/train --devSet puzzles/datasets/dev --features solved,blocking,forward`

    - Same, but with regularization, and write the results into a file "weights".

`java rushhour.Main learn puzzles/datasets/train --devSet puzzles/datasets/dev --features solved,blocking,forward --regularize --learningRate 1 --complexityPenalty 5 --regularizationQ 1 --lossQ 2 --outfile weights`

    - Test how well our final heuristic does on our test set:

`java rushhour.Main test puzzles/datasets/test solved,blocking,forward actualOptimalWeights`
