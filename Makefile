all: core io evaluation analysis generation

core:
	javac rushhour/core/*.java

io:
	javac rushhour/io/*.java

evaluation:
	javac rushhour/evaluation/*.java

analysis:
	javac rushhour/analysis/*.java

generation:
	javac rushhour/generation/*.java
