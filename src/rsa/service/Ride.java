package rsa.service;

import java.util.Comparator;
import java.util.UUID;

import rsa.quad.HasPoint;
import rsa.shared.Location;
import rsa.shared.RideMatchInfo;
import rsa.shared.RideRole;

public class Ride implements HasPoint, RideMatchInfoSorter {
	User user;
	Location from;
	Location to;
	String plate;
	float cost;
	Location current;
	Matcher.RideMatch match;
	long id;
	
	/**
	 * Creates a ride from given arguments.
	 * @param user - of ride
	 * @param from - where to depart
	 * @param to - where to arrive
	 * @param plate - of the car
	 * @param cost - of the ride
	 */
	Ride(User user, Location from, Location to, String plate, float cost) {
		super();
		this.user = user;
		this.from = from;
		this.to = to;
		this.plate = plate;
		this.cost = cost;
		this.current = from;
		this.match = null;
		this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	/**
	 * User of this ride
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Change user of this ride
	 * @param user - to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Get the origin of this ride
	 * @return the from
	 */
	public Location getFrom() {
		return from;
	}

	/**
	 * Change the origin of this ride
	 * @param from - the from to set
	 */
	public void setFrom(Location from) {
		this.from = from;
	}

	/**
	 * Get destination of this ride
	 * @return the to
	 */
	public Location getTo() {
		return to;
	}

	/**
	 * Change destination of this ride
	 * @param to - the to to set
	 */
	public void setTo(Location to) {
		this.to = to;
	}

	/**
	 * Car's registration plate for this ride
	 * @return the plate null if passenger)
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * Car's registration plate for this ride
	 * @param plate - of car to set (null if passenger)
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * Cost of this ride (only meaningful for for driver)
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * Change cost of this ride (only meaningful for for driver)
	 * @param cost - the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * Current location
	 * @return the current
	 */
	public Location getCurrent() {
		return current;
	}

	/**
	 * Change current location
	 * @param current - the current to set
	 */
	public void setCurrent(Location current) {
		this.current = current;
	}

	/**
	 * Current match of this ride
	 * @return the match
	 */
	public Matcher.RideMatch getMatch() {
		return match;
	}

	/**
	 * Assign a match to this ride
	 * @param match - the match to set
	 */
	public void setMatch(Matcher.RideMatch match) {
		this.match = match;
	}

	/**
	 * Generated unique identifier of this ride.
	 * @return this ride identifier
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Role of user in ride, depending on a car's license plate being registered
	 * @return RideRole depending on plate
	 */
	public RideRole getRideRole() {
		if (this.plate == null)
			return RideRole.PASSENGER;
		return RideRole.DRIVER;
	}
	
	/**
	 * Is the user the driver in this ride?
	 * @return true if user is the driver; false otherwise
	 */
	public boolean isDriver() {
		if (this.plate == null)
			return false;
		return true;
	}
	
	/**
	 * Is the user the passenger in this ride?
	 * @return true if user is the passenger; false otherwise
	 */
	public boolean isPassenger() {
		if (this.plate == null)
			return true;
		return false;
	}
	
	/**
	 * This ride was match with another
	 * @return true is this ride is matched
	 */
	public boolean isMatched() {
		if (this.match == null)
			return false;
		return true;
	}
	
	@Override
	public Comparator<RideMatchInfo> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getX() {
		return current.getX();
	}

	@Override
	public double getY() {
		return current.getY();
	}

}
