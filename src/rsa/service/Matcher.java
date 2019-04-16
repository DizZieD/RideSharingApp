package rsa.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import rsa.quad.PointQuadtree;
import rsa.quad.Trie;
import rsa.shared.Location;
import rsa.shared.RideMatchInfo;
import rsa.shared.RideRole;
import rsa.shared.UserStars;

public class Matcher implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Location bottomRight;
	private static Location topLeft;
	private static double radius;
	
	Map<Long, Ride> rides;
	Map<Long, RideMatch> matches;
	PointQuadtree<Ride> qtree;
	
	public class RideMatch {
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
			if(driver != null &&  passager != null) {
				double fromDistance = (driver.getFrom().getX() - passager.getFrom().getX()) * (driver.getFrom().getX() - passager.getFrom().getX()) + (driver.getFrom().getY() - passager.getFrom().getY()) * (driver.getFrom().getY() - passager.getFrom().getY()); 
				double toDistance = (driver.getTo().getX() - passager.getTo().getX()) * (driver.getTo().getX() - passager.getTo().getX()) + (driver.getTo().getY() - passager.getTo().getY()) * (driver.getTo().getY() - passager.getTo().getY());
				if(!driver.isMatched() && !passager.isMatched()) {
					if(fromDistance <= (Matcher.getRadius() * Matcher.getRadius())) {
						if(toDistance <= (Matcher.getRadius() * Matcher.getRadius()))
							return true;
					}	
				}
					
			}	
			return false;
		}
	}
	
	/**
	 * IMPORTANT: need to set topLeft, bottomRight and radius first<p>
	 * Create a empty matcher
	 */
	public Matcher() {
		Trie.setCapacity(10);
		this.rides = new HashMap<Long, Ride>();
		this.matches = new HashMap<Long, RideMatch>();
		this.qtree = new PointQuadtree<Ride>(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY());
	}
	
	/**
	 * Add a ride to the matcher
	 * @param user - providing or requiring a ride
	 * @param from - origin location
	 * @param to - destination location
	 * @param plate - of then car (if null then it is a passenger)
	 * @param cost - of the ride (how must you charge, if you are the driver)
	 * @return ride identifier
	 */
	public long addRide(User user, Location from, Location to, String plate, float cost) {
		Ride ride = new Ride(user, from, to, plate, cost);
		qtree.insert(ride);
		this.rides.put(ride.getId(), ride);
		return ride.getId();
	}
	
	/**
	 * Update current location of ride with given id. If ride is not yet matched, returns a set RideMatchInfo. Proposed ride matches are currently near (use PointQuadtree) have different roles (one is a driver, the other a passenger) and go almost to the same destination (differ by radius).
	 * @param rideId  - of the ride to update
	 * @param current - location
	 * @return set of RideMatchInfo
	 */
	SortedSet<RideMatchInfo> updateRide(long rideId, Location current) {
		Ride ride = this.rides.get(rideId);
		ride.setCurrent(current);
		if(ride.isMatched())
			return null;
		
		SortedSet<RideMatchInfo> sortedSet = new TreeSet<RideMatchInfo>(ride.getComparator());
		Set<Ride> unsortedSet = qtree.findNear(current.getX(), current.getY(), Matcher.radius);
		RideMatch rideMatch;
		for (Ride tmp : unsortedSet){
			
        	rideMatch = new RideMatch(ride, tmp);
        	if(rideMatch.matchable()) {
        		this.matches.put(rideMatch.getId(), rideMatch);
        		RideMatchInfo rideMatchInfo = new RideMatchInfo(rideMatch);
        		sortedSet.add(rideMatchInfo);
        	}
        		
        }
		return sortedSet;
	}
	
	/**
	 * Accept the proposed match (identified by matchId) for given ride (identified by rideId)
	 * @param rideId - id of ride
	 * @param matchId - of match to accept
	 */
	public void acceptMatch(long rideId, long matchId) {
		Ride ride = this.rides.get(rideId);
		ride.setMatch(this.matches.get(matchId));
	}
	
	/**
	 * Mark ride as concluded and classify other using stars
	 * @param rideId - of the ride to conclude
	 * @param stars - to assign to other user
	 */
	public void concludeRide(long rideId, UserStars stars) {
		Ride ride = this.rides.get(rideId);
		Ride otherRide = ride.getMatch().getRide(ride.getRideRole().other());
		
		otherRide.getUser().addStars(stars, otherRide.getRideRole());
	}
	
	/**
	 * Location of bottom right corner of matching region
	 * @return the bottomRight
	 */
	public static Location getBottomRight() {
		return bottomRight;
	}
	
	/**
	 * Change location of bottom right corner of matching region
	 * @param bottomRight - the bottomRight to set
	 */
	public static void setBottomRight(Location bottomRight) {
		Matcher.bottomRight = bottomRight;
	}
	
	/**
	 * Location of top left corner of matching region
	 * @return the topLeft
	 */
	public static Location getTopLeft() {
		return topLeft;
	}

	/**
	 * Change location of top left corner of matching region
	 * @param topLeft - the topLeft to set
	 */
	public static void setTopLeft(Location topLeft) {
		Matcher.topLeft = topLeft;
	}

	/**
	 * Maximum distance between two users eligible for a match
	 * @return the radius
	 */
	public static double getRadius() {
		return radius;
	}

	/**
	 * Set distance to consider a match
	 * @param radius - the radius to set
	 */
	public static void setRadius(double radius) {
		Matcher.radius = radius;
	}
	
	
}
