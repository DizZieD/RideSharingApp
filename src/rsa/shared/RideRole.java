package rsa.shared;

public enum RideRole implements java.io.Serializable{ 
	/*** This user is driving the car*/
	DRIVER,
	/*** This user is the passenger*/
	PASSENGER;
	
	/**
	 * The other role: if driver then passenger, otherwise driver
	 * @return other role
	 */
	public RideRole other() {
		if(this == DRIVER)
			return PASSENGER;
		return DRIVER;
	}
}
