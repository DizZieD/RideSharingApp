package rsa.service;

import java.util.Set;

import rsa.shared.Location;
import rsa.shared.PreferredMatch;
import rsa.shared.RideMatchInfo;
import rsa.shared.RideRole;
import rsa.shared.RideSharingAppException;
import rsa.shared.UserStars;

public class Manager {
	private static Manager manager = null;
	
	Users allUsers;
	Matcher matcher;
	
	/**
	 * Create a empty Manager
	 * @throws RideSharingAppException
	 */
	private Manager() throws RideSharingAppException {
		allUsers = Users.getInstance();
		matcher = new Matcher();
	}
	
	/**
	 * Returns the single instance of this class as proposed in the singleton design pattern.
	 * @return instance of this class
	 * @throws RideSharingAppException - if I/O error occurs reading users serialization
	 */
	public static Manager getInstance() throws RideSharingAppException {
		if(manager == null)
			manager = new Manager();
		
		return manager;
	}
	
	/**
	 * Resets singleton for unit testing purposes.
	 * @throws RideSharingAppException - if I/O error occurs reading users serialization
	 */
	void reset() throws RideSharingAppException {
		allUsers.reset();
		manager = new Manager();
	}
	
	/**
	 * Register a player with given nick and password. Changes are stored in serialization file
	 * @param nick - of user
	 * @param name - of user
	 * @param password - of user
	 * @return true if registered and false otherwise
	 * @throws RideSharingAppException - if I/O error occurs when serializing data
	 */
	public boolean register(String nick, String name, String password) throws RideSharingAppException {
		return allUsers.register(nick, name, password);
	}
	
	/**
	 * Change password if old password matches the current one
	 * @param nick - of player
	 * @param oldPassword - for authentication before update
	 * @param newPassword - after update
	 * @return true if password changed and false otherwise
	 * @throws RideSharingAppException - on I/O error in serialization.
	 */
	public boolean updatePassword(String nick, String oldPassword, String newPassword) throws RideSharingAppException {
		return allUsers.updatePassword(nick, oldPassword, newPassword);
	}
	
	/**
	 * Authenticates user given id and password
	 * @param nick - of user to authenticate
	 * @param password - of user to authenticate
	 * @return true if authenticated and false otherwise
	 */
	public boolean authenticate(String nick, String password) {
		return allUsers.authenticate(nick, password);
	}
	
	/**
	 * Current preferred match for given authenticated user
	 * @param nick - of user
	 * @param password - of user
	 * @return the current preferred match for this user
	 * @throws RideSharingAppException - if authentication fails
	 */
	public PreferredMatch getPreferredMatch(String nick, String password) throws RideSharingAppException {
		if(!allUsers.authenticate(nick, password)) {
			throw new RideSharingAppException();
		}
		
		return allUsers.getUser(nick).getPreferredMatch();
	}
	
	/**
	 * Set preferred match for given authenticated user
	 * @param nick - of user
	 * @param password - of user
	 * @param preferred - kind of match
	 * @throws RideSharingAppException - if authentication fails
	 */
	public void setPreferredMatch(String nick, String password, PreferredMatch preferred) throws RideSharingAppException {
		if(!allUsers.authenticate(nick, password)) {
			throw new RideSharingAppException();
		}
		
		allUsers.getUser(nick).setPreferredMatch(preferred);
	}
	
	/**
	 * Add a ride for user with given nick, from and to the given locations. A car license plate must be given if user is the driver, or null if passenger.
	 * @param nick - of user
	 * @param password - of user
	 * @param from - origin's location
	 * @param to - destination's location
	 * @param plate - of car (null if passenger
	 * @param cost - of the ride (how must you charge, if you are the driver)
	 * @return id of created ride
	 * @throws RideSharingAppException - if authentication fails
	 */
	public long addRide(String nick, String password, Location from, Location to, String plate, float cost) throws RideSharingAppException {
		if(!allUsers.authenticate(nick, password)) {
			throw new RideSharingAppException();
		}
		
		return matcher.addRide(allUsers.getUser(nick), from, to, plate, cost);
	}
	
	/**
	 * Update current location of user and receive a set of proposed ride matches
	 * @param rideId - of ride to update
	 * @param current - location of user
	 * @return A Set of RideMatchInfo
	 */
	public Set<RideMatchInfo> updateRide(long rideId, Location current) {
		return matcher.updateRide(rideId, current);
	}
	
	/**
	 * Accept a match
	 * @param rideId - id of of ride to match
	 * @param matchId - id of match to consider
	 */
	public void acceptMatch(long rideId, long matchId) {
		matcher.acceptMatch(rideId, matchId);
	}
	
	/**
	 * Conclude a ride and provide feedback on the other partner
	 * @param rideId - of the ride to conclude
	 * @param classification - of the ride partner (in starts)
	 */
	public void concludeRide(long rideId, UserStars classification) {
		matcher.concludeRide(rideId, classification);
	}
	
	public double getAverage(String nick, RideRole role) throws RideSharingAppException	{
		User user = allUsers.getUser(nick);
		
		if(user == null)
			throw new RideSharingAppException();
		
		return user.getAverage(role);
	}
}
