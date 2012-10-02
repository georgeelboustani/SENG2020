package exception;

public class CancelException extends Exception {
	@Override
	public String getMessage() {
		return "Cancel option selected";
	}
}
