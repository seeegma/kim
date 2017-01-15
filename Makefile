all: core io evaluation generation

core:
	javac rushhour/core/*.java

io:
	javac rushhour/io/*.java

evaluation:
	javac rushhour/evaluation/*.java

generation:
	javac rushhour/generation/*.java
