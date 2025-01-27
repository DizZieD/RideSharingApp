package rsa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rsa.shared.Car;
import rsa.shared.Location;
import rsa.shared.PreferredMatch;
import rsa.shared.RideMatchInfo;
import rsa.shared.RideRole;
import rsa.shared.RideSharingAppException;
import rsa.shared.UserStars;

/**
* Template for a test class on Manager - YOU NEED TO IMPLEMENTS THESE TESTS!
* 
*/
public class ManagerTest extends rsa.TestData {
	static Manager manager;
	
	Location from = new Location(X1,Y1);
	Location to   = new Location(X2,Y2);
	Location other = new Location(X3,Y3);
	
	@BeforeClass
	public static void setUpClass() throws rsa.shared.RideSharingAppException {
		manager = Manager.getInstance();
	}
	
	@Before
	public void setUp() throws Exception {
		manager.reset();
	}
	
	/**
	 * Check user registration with invalid nicks, duplicate nicks, multiple users
	 * 
	 * @throws RideSharingAppException on reading serialization file (not tested)
	 */
	@Test
	public void testRegister() throws RideSharingAppException {
		assertFalse("Invalid nick", manager.register(INVALID_NICK, NAMES[0], PASSWORDS[0]));
		assertTrue("Valid nick", manager.register(NICKS[0], NAMES[0], PASSWORDS[0]));
		assertFalse("Duplicate nick", manager.register(NICKS[0], NAMES[0],PASSWORDS[0]));
		assertTrue("Valid nick", manager.register(NICKS[1], NAMES[1], PASSWORDS[1]));
		assertFalse("Duplicate nick", manager.register(NICKS[1], NAMES[1], PASSWORDS[0]));
	}
	
	/**
	 * Check password update, with valid password, old password and wrong password
	 *    
	 * @throws RideSharingAppException on reading serialization file (not tested)
	 */
	@Test
	public void testUpdatePassword() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[1], PASSWORDS[0]);

		assertTrue(manager.updatePassword(NICKS[0], PASSWORDS[0], PASSWORDS[1]));
		assertFalse(manager.updatePassword(NICKS[0], PASSWORDS[0], PASSWORDS[1]));
		assertTrue(manager.updatePassword(NICKS[0], PASSWORDS[1], PASSWORDS[0]));
	}

	/**
	 * Check authentication valid and invalid tokens and multiple users
	 * 
	 * @throws RideSharingAppException on reading serialization file (not tested)
	 */
	@Test
	public void testAuthenticate() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);

		assertTrue(manager.authenticate(NICKS[0], PASSWORDS[0]));
		assertFalse(manager.authenticate(NICKS[0], PASSWORDS[1]));
		assertFalse(manager.authenticate(NICKS[1], PASSWORDS[1]));
		assertFalse(manager.authenticate(NICKS[1], PASSWORDS[0]));
	}

	
	@Test
	public void testPreferredMatch() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		
		assertEquals(PreferredMatch.BETTER,manager.getPreferredMatch(NICKS[0], PASSWORDS[0]));
		
		for(PreferredMatch preferred: PreferredMatch.values()) {
			manager.setPreferredMatch(NICKS[0], PASSWORDS[0], preferred);
			assertEquals(preferred,manager.getPreferredMatch(NICKS[0], PASSWORDS[0]));
		}
		
		manager.setPreferredMatch(NICKS[0], PASSWORDS[0], null); // check default
		assertEquals(PreferredMatch.BETTER,manager.getPreferredMatch(NICKS[0], PASSWORDS[0]));	
	}

	
	
	/**
	 * Check if rides don't match when both are drivers
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testRidesDontMatchBothDrivers() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, PLATES[1],COSTS[0]);
	
		Set<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		Set<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(0,driverMatches.size());
		assertEquals(0,passgrMatches.size());
	}
	
	/**
	 * Check if rides don't match when both are passengers
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testRidesDontMatchBothPassengers() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, null,COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
	
		Set<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		Set<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(0,driverMatches.size());
		assertEquals(0,passgrMatches.size());
	}

	
	/**
	 * Check if rides don't match when destination is different  
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testRidesDontMatchDifferentDestination() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, other, null,COSTS[0]);
	
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(0,driverMatches.size());
		assertEquals(0,passgrMatches.size());
	}
	

	/**
	 * Check if rides don't match when current position is different  
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testRidesDontMatchWhenInDifferentPositions() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], other, to, null,COSTS[0]);
	
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(0,driverMatches.size());
		assertEquals(0,passgrMatches.size());
	}


	/**
	 * Simple match: both rides with same path (origin and destination)
	 * One is driver and other passenger.
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testSimpleMatch() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(1,passgrMatches.size());
		
		RideMatchInfo driverMatch = driverMatches.first();
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[0],driverMatch.getName(RideRole.DRIVER));
		assertEquals(NAMES[0],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(NAMES[1],driverMatch.getName(RideRole.PASSENGER));
		assertEquals(NAMES[1],passgrMatch.getName(RideRole.PASSENGER));
		
		manager.acceptMatch(driverRideId, passgrMatch.getMatchId());
		manager.acceptMatch(passgrRideId, driverMatch.getMatchId());
		
		assertEquals(0,manager.getAverage(NICKS[0],RideRole.DRIVER),DELTA);
		assertEquals(0,manager.getAverage(NICKS[1],RideRole.PASSENGER),DELTA);
		
		manager.concludeRide(driverRideId, UserStars.FOUR_STARS);
		manager.concludeRide(passgrRideId, UserStars.FIVE_STARS);
		
		assertEquals(5,manager.getAverage(NICKS[0],RideRole.DRIVER),DELTA);
		assertEquals(4,manager.getAverage(NICKS[1],RideRole.PASSENGER),DELTA);
	}

	/**
	 * Double match: two drivers with same path (origin and destination)
	 * First has more starts and is used the default preference (BETTER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchDefault1() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[0]);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo driverMatch = driverMatches.first();
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[0],driverMatch.getName(RideRole.DRIVER));
		assertEquals(NAMES[0],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[0],driverMatch.getCar().getPlate());
		assertEquals(PLATES[0],passgrMatch.getCar().getPlate());
	}
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * Second has more stars and is used the default preference (BETTER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchDefault2() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[1],MAKES[1], MODELS[1], COLORS[1]));
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[0]);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[2],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[2],passgrMatch.getCar().getPlate());
	}
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * First has more starts and is used the better driver preference (BETTER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchBetter1() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[0]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.BETTER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo driverMatch = driverMatches.first();
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[0],driverMatch.getName(RideRole.DRIVER));
		assertEquals(NAMES[0],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[0],driverMatch.getCar().getPlate());
		assertEquals(PLATES[0],passgrMatch.getCar().getPlate());
	}
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * Second has more stars and is used the better driver preference (BETTER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchBetter2() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[1],MAKES[1], MODELS[1], COLORS[1]));
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[0]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[0]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.BETTER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[2],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[2],passgrMatch.getCar().getPlate());
	}
	
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * First has more starts and is used the cheapest ride preference (CHEAPER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchCheaper1() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[1]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[2]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.CHEAPER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo driverMatch = driverMatches.first();
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[0],driverMatch.getName(RideRole.DRIVER));
		assertEquals(NAMES[0],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[0],driverMatch.getCar().getPlate());
		assertEquals(PLATES[0],passgrMatch.getCar().getPlate());
	}
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * Second has more stars and is used the cheapest ride preference (CHEAPER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchCheaper2() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[1],MAKES[1], MODELS[1], COLORS[1]));
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[2]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[1]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.CHEAPER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[2],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[2],passgrMatch.getCar().getPlate());
	}
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * First has more starts and is used the closer ride preference (CLOSER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchCloser1() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		Location near = new Location(X1+RADIUS,Y1);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[1]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[2]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.CLOSER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, from);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, near);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo driverMatch = driverMatches.first();
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[0],driverMatch.getName(RideRole.DRIVER));
		assertEquals(NAMES[0],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[0],driverMatch.getCar().getPlate());
		assertEquals(PLATES[0],passgrMatch.getCar().getPlate());
	}
	
	
	/**
	 * Double match: two drivers with same path (origin and destination)
	 * Second has more stars and is used the closer ride preference (CLOSER)
	 * @throws RideSharingAppException 
	 */
	@Test
	public void testDoubleDriverMatchCloser2() throws RideSharingAppException {
		manager.register(NICKS[0], NAMES[0], PASSWORDS[0]);
		Manager.allUsers.getUser(NICKS[0]).addCar(new Car(PLATES[0],MAKES[0], MODELS[0], COLORS[0]));
		manager.register(NICKS[1], NAMES[1], PASSWORDS[1]);
		manager.register(NICKS[2], NAMES[2], PASSWORDS[2]);
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[1],MAKES[1], MODELS[1], COLORS[1]));
		Manager.allUsers.getUser(NICKS[2]).addCar(new Car(PLATES[2],MAKES[2], MODELS[2], COLORS[2]));
		
		Location near = new Location(X1+RADIUS,Y1);
		
		long driverRideId   = manager.addRide(NICKS[0], PASSWORDS[0],  from, to, PLATES[0],COSTS[1]);
		long passgrRideId = manager.addRide(NICKS[1], PASSWORDS[1], from, to, null,COSTS[0]);
		long otherRideId  = manager.addRide(NICKS[2], PASSWORDS[2], from, to, PLATES[2], COSTS[2]);
		
		manager.setPreferredMatch(NICKS[1], PASSWORDS[1], PreferredMatch.CLOSER);
		
		Manager.allUsers.getUser(NICKS[0]).addStars(UserStars.FOUR_STARS, RideRole.DRIVER);
		Manager.allUsers.getUser(NICKS[2]).addStars(UserStars.FIVE_STARS, RideRole.DRIVER);
		
		SortedSet<RideMatchInfo> driverMatches = manager.updateRide(driverRideId, near);
		SortedSet<RideMatchInfo> otherMatches  = manager.updateRide(otherRideId, from);
		SortedSet<RideMatchInfo> passgrMatches = manager.updateRide(passgrRideId, from);

		assertEquals(1,driverMatches.size());
		assertEquals(2,passgrMatches.size());
		assertEquals(1,otherMatches.size());
		
		RideMatchInfo passgrMatch = passgrMatches.first();
		
		assertEquals(NAMES[2],passgrMatch.getName(RideRole.DRIVER));
		
		assertEquals(PLATES[2],passgrMatch.getCar().getPlate());
	}

}
