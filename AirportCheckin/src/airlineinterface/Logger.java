package airlineinterface;

public class Logger {

	// Statics
	private static Logger instance = new Logger();

	public static Logger instance() {
		return instance;
	}

	private long startMillis;

	private Logger() {
	}

	public void log(String s) {
		long time = System.currentTimeMillis() - startMillis;
		String timeString = "[" + String.format("%07d",  time) + "] ";
		System.out.println(timeString + s);
	}
	
	public void resetTimer() {
		startMillis = System.currentTimeMillis();
	}

}
