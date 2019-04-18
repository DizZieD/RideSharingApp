package rsa.shared;

public class RideSharingAppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RideSharingAppException() {
		
	}
	
	public RideSharingAppException(String message) {
		System.out.println(message);
	}
	
	public RideSharingAppException(String message, Throwable cause) {
		System.out.println(message);
	}
	
	public RideSharingAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		System.out.println(message);
	}
	
	public RideSharingAppException(Throwable cause) {
		
	}
}
