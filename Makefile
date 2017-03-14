all: core io solving util learning generation main

core:
	javac -d . src/rushhour/core/*.java

io:
	javac -d . src/rushhour/io/*.java

solving:
	javac -d . src/rushhour/solving/*.java

learning:
	javac -d . src/rushhour/learning/*.java

util:
	javac -d . src/rushhour/Util.java

generation:
	javac -d . src/rushhour/generation/*.java

main:
	javac -d . src/rushhour/Main.java

clean:
	rm -f rushhour/Main.class
	rm -f rushhour/Util.class
	rm -f rushhour/core/*.class
	rm -f rushhour/solving/*.class
	rm -f rushhour/generation/*.class
	rm -f rushhour/io/*.class
