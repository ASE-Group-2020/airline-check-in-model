/**
 * Is thrown when invalid input variables are used in DatasetCreator
 * 
 * @authors
 * Edi Edimov ee21@hw.ac.uk<br>
 * David Gladwell drg1@hw.ac.uk<br>
 * Alex Swift as184@hw.ac.uk<br>
 * Niko Tether nst2@hw.ac.uk
 * @since 9/02/2020
 */
public class InvalidValueInDatasetCreatorException extends Exception {
	private static final long serialVersionUID = -1756336707707772545L;
	public InvalidValueInDatasetCreatorException(String errorMessage) {
		super("Invalid input variables detected: " + errorMessage);
	}
}
