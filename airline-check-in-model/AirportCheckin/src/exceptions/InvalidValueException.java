package exceptions;
public class InvalidValueException extends Exception {
	private static final long serialVersionUID = -1936571217940205016L;
	public InvalidValueException(String attributeMessage)
	{
		super("Invalid attribute value: " + attributeMessage);
	}
}