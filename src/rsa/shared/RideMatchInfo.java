package rsa.shared;

import rsa.service.Matcher.RideMatch;
import rsa.service.Ride;
import rsa.service.User;

public class RideMatchInfo extends java.lang.Object {
	RideMatch match;

	/**
	 * Creates an instance by copying relevant data from an instance of the Matcher's inner class Matcher.RideMatch.
	 * @param match - to use as data source
	 */
	public RideMatchInfo(RideMatch match) {
		super();
		this.match = match;
	}
	
	/**
	 * Id of match
	 * @return id
	 */
	public long getMatchId() {
		return this.match.getId();
	}
	
	/**
	 * Car used in ride
	 * @return car
	 */
	public Car getCar() {
		Ride driver =  this.match.getRide(RideRole.DRIVER);
		User driverUser = driver.getUser();
		return driverUser.getCar(driver.getPlate());
	}
	
	/**
	 * Cost of this ride, payed by the passenger to the driver
	 * @return cost
	 */
	public float getCost() {
		return this.match.getRide(RideRole.DRIVER).getCost();
	}
	
	/**
	 * Get name of user with given role
	 * @param role - of user in match
	 * @return name of user with given role
	 */
	public String getName(RideRole role) {
		return this.match.getRide(role).getUser().getName();
	}
	
	/**
	 * Get average number of stars of user with given role
	 * @param role - of user in match
	 * @return stars average of user with given role
	 */
	public float getStars(RideRole role) {
		return this.match.getRide(role).getUser().getAverage(role);
	}
	
	/**
	 * The location of a user with given role
	 * @param role - of user in match
	 * @return location of user with given role
	 */
	public Location getWhere(RideRole role) {
		return this.match.getRide(role).getCurrent();
	}
}
