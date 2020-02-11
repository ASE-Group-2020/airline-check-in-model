package exceptions;
public class AlreadyCheckedInException extends Exception {
	private static final long serialVersionUID = -6375803080743295984L;
	public AlreadyCheckedInException(String customerName)
	{
		super("Customer has already checked in: " + customerName);
	}
}
