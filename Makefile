all: core solving io util generation main

core:
	javac -d . src/rushhour/core/*.java

solving:
	javac -d . src/rushhour/solving/*.java

io:
	javac -d . src/rushhour/io/*.java

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
