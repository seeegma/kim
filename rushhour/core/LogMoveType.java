package rushhour.core;

public enum LogMoveType {
	NORMAL("N"),
	UNDO("U"),
	RESET("R");

	private String string;

	LogMoveType(String string) {
		this.string = string;
	}

	public String toString() {
		return this.string;
	}
}
