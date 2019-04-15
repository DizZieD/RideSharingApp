package rsa.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rsa.shared.Location;
import rsa.shared.RideRole;

public class Matcher implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Location bottomRight;
	private static Location topLeft;
	private static double radius;
	
	class RideMatch {
		long id;
		Map <RideRole, Ride> rides;
		
		/**
		 * Create a possible ride match for a pair of rides (rides have no particular order)
		 * @param left - ride
		 * @param right - ride
		 */
		public RideMatch(Ride left, Ride right) {
			this.rides = new HashMap<RideRole, Ride>();
			this.rides.put(left.getRideRole(), left);
			this.rides.put(right.getRideRole(), right);
			this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
		}
		
		/**
		 * Generated unique identifier of this ride match.
		 * @return this ride match identifier
		 */
		public long getId() {
			return id;
		}
		
		/**
		 * Ride of user with given role
		 * @param role - of user
		 * @return the driver Ride
		 */
		public Ride getRide(RideRole role) {
			return this.rides.get(role);
		}
		
		boolean matchable() {
			Ride driver = this.rides.get(RideRole.DRIVER);
			Ride passager = this.rides.get(RideRole.PASSENGER);
			double fromDistance = (driver.getFrom().getX() - passager.getFrom().getX()) * (driver.getFrom().getX() - passager.getFrom().getX()) + (driver.getFrom().getY() - passager.getFrom().getY()) * (driver.getFrom().getY() - passager.getFrom().getY()); 
			double toDistance = (driver.getTo().getX() - passager.getTo().getX()) * (driver.getTo().getX() - passager.getTo().getX()) + (driver.getTo().getY() - passager.getTo().getY()) * (driver.getTo().getY() - passager.getTo().getY()); 
			if(driver != null &&  passager != null)
				if(!driver.isMatched() && !passager.isMatched())
					if(fromDistance <= (Matcher.getRadius() * Matcher.getRadius()))
						if(toDistance <= (Matcher.getRadius() * Matcher.getRadius()))
							return true;
			return false;
		}
	}
	
	public static double getRadius() {
		return radius;
	}
}
