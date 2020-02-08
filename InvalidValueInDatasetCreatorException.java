
public class InvalidValueInDatasetCreatorException extends Exception {
	private static final long serialVersionUID = -1756336707707772545L;
	public InvalidValueInDatasetCreatorException(String errorMessage) {
		super("Invalid input variables detected: " + errorMessage);
	}
}
