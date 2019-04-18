package rsa.quad;

public class PointOutOfBoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PointOutOfBoundException() {
		System.out.println("Point out of bounds");
	}
}
