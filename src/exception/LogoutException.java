package exception;

public class LogoutException extends Exception {
	@Override
	public String getMessage() {
		return "Logout option selected";
	}
}
