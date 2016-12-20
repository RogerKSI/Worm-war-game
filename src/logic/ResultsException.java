package logic;

public class ResultsException extends Exception {

	private static final long serialVersionUID = 1L;

	private int errorType;

	public ResultsException(int errorType) {
		this.errorType = errorType;
	}

	public String getMessage() {

		if (errorType == 0)
			return "Name cannot be empty";
		else if (errorType == 1)
			return "Name can't contain any of the following characters : < >";
		else if (errorType == 2)
			return "The name cannot be the same";
		else
			return "Wrong results format";
	}
}
