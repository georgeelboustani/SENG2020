package exception;

public class InvalidStoreIdException extends Exception {
	@Override
	public String getMessage() {
		return "No store with the given id exists";
	}
}
