all: core analysis io evaluation generation main

main:
	javac -d . src/rushhour/Main.java

core:
	javac -d . src/rushhour/core/*.java

io:
	javac -d . src/rushhour/io/*.java

evaluation:
	javac -d . src/rushhour/evaluation/*.java

analysis:
	javac -d . src/rushhour/analysis/*.java

generation:
	javac -d . src/rushhour/generation/*.java

clean:
	rm -f rushhour/Main.class
	rm -f rushhour/core/*.class
	rm -f rushhour/io/*.class
	rm -f rushhour/evaluation/*.class
	rm -f rushhour/analysis/*.class
	rm -f rushhour/generation/*.class
