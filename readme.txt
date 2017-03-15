RUSH HOUR!!!
============

authors: dyl & ben

- To set up the code for running:
    1. download [EJML](https://sourceforge.net/projects/ejml/files/latest/download?source=files) and unzip the .jar files in the project's root directory (rushhour-astar)
    2. run `./setup.sh`, which will extract the .jars and make the project

- To use the code: mainly, see the code's built-in usage. Here are some examples:

    - Learn the optimal weights for the three features, print them, then compute error on the dev set:

`java rushhour.Main learn puzzles/datasets/train --devSet puzzles/datasets/dev --features solved,blocking,forward`

    - Same, but with regularization, and write the results into a file "weights".

`java rushhour.Main learn puzzles/datasets/train --devSet puzzles/datasets/dev --features solved,blocking,forward --regularize --learningRate 1 --complexityPenalty 5 --regularizationQ 1 --lossQ 2 --outfile weights`

    - Test how well 
