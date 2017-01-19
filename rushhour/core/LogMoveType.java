package rushhour.core;

public enum LogMoveType {
	NORMAL("N"),
	UNDO("U"),
	RESET("R");

	private String string;

	LogMoveType(String string) {
		this.string = string;
	}

	public static LogMoveType fromChar(char c) {
		switch(c) {
			case 'N':
				return NORMAL;
			case 'U':
				return UNDO;
			case 'R':
				return RESET;
			default:
				return null;
		}
	}

	public String toString() {
		return this.string;
	}
}
