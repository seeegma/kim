all: core io evaluation analysis generation main

main:
	javac rushhour/Main.java

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

clean:
	rm rushhour/Main.class
	rm rushhour/core/*.class
	rm rushhour/io/*.class
	rm rushhour/evaluation/*.class
	rm rushhour/analysis/*.class
	rm rushhour/generation/*.class
