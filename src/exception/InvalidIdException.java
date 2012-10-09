package exception;

public class InvalidIdException extends Exception {
	@Override
	public String getMessage() {
		return "The id provided was invalid and no entry with such an id exists";
	}
}
