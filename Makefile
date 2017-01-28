all: core io evaluation analysis generation main

main:
	javac -d . src/rushhour/Main.java

core:
	javac -d . src/rushhour/core/*.java

io: core analysis
	javac -d . src/rushhour/io/*.java

evaluation: core
	javac -d . src/rushhour/evaluation/*.java

analysis: core
	javac -d . src/rushhour/analysis/*.java

generation: core
	javac -d . src/rushhour/generation/*.java

clean:
	rm -f rushhour/Main.class
	rm -f rushhour/core/*.class
	rm -f rushhour/io/*.class
	rm -f rushhour/evaluation/*.class
	rm -f rushhour/analysis/*.class
	rm -f rushhour/generation/*.class
