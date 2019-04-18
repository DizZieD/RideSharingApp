package rsa.service;

import java.util.HashMap;
import java.util.Map;

import rsa.shared.Car;
import rsa.shared.PreferredMatch;
import rsa.shared.RideRole;
import rsa.shared.UserStars;

public class User extends java.lang.Object implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	String nick;
	String name;
	String password;
	PreferredMatch preferredMatch;
	
	Map<String,Car> cars;
	Map<RideRole, Integer> stars;
	private Map<RideRole, Integer> numberOfReviews;

	/**
	 * Create a user with given features
	 * @param nick - nick of user
	 * @param password - password
	 * @param name - name of user
	 */
	public User(String nick, String name, String password) {
		super();
		this.nick = nick;
		this.name = name;
		this.password = password;
		this.preferredMatch = PreferredMatch.BETTER;
		this.cars = new HashMap<String, Car>();
		this.stars = new HashMap<RideRole, Integer>();
		this.numberOfReviews = new HashMap<RideRole, Integer>();
	}
	
	/**
	 * Bind a car to this user. Can be used to change car features.
	 * @param car - to add
	 */
	public void addCar(Car car) {
		cars.put(car.getPlate(), car);
	}
	
	/**
	 * Add stars to user according to a role. The registered values are used to compute an average.
	 * @param moreStars - to add to this user
	 * @param role - in which stars are added 
	 */
	public void addStars(UserStars moreStars, RideRole role) {
		int stars = 0;
		
		switch(moreStars) {
			case ONE_STAR:
				stars = 1;
				break;
			case TWO_STARS:
				stars = 2;
				break;
			case THREE_STARS:
				stars = 3;
				break;
			case FOUR_STARS:
				stars = 4;
				break;
			case FIVE_STARS:
				stars = 5;
				break;	
		}
		
		this.numberOfReviews.put(role, this.numberOfReviews.getOrDefault(role, 0)+1);
		this.stars.put(role, this.stars.getOrDefault(role, 0)+stars);
	}
	
	/**
	 * Returns the average number of stars in given role
	 * @param role - of user
	 * @return average number of stars 
	 */
	public float getAverage(RideRole role) {
		return (float)this.stars.getOrDefault(role, 0) / this.numberOfReviews.getOrDefault(role, 1);
	}
	
	/**
	 * Check the authentication of this player
	 * @param password - for checking
	 * @return true password is the expected, false otherwise
	 */
	boolean authenticate(String password) {
		return this.password.equals(password);
	}
	
	/**
	 * Remove binding between use and car
	 * @param plate - of car to remove from this user
	 */
	void deleteCar(String plate) {
		cars.remove(plate);
	}

	/**
	 * Car with given license plate
	 * @param plate - of car
	 * @return car
	 */
	public Car getCar(String plate) {
		return cars.get(plate);
	}

	/**
	 * Name of user
	 * @return user's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Change user's name
	 * @param name - to change
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Current password of this user
	 * @return password
	 */
	String getPassword() {
		return password;
	}

	/**
	 * Change password of this user
	 * @param password - to change
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * The nick of this user: Cannot be changed as it a key.
	 * @return nick
	 */
	String getNick() {
		return nick;
	}
	
	/**
	 * Change preference for sorting matches
	 * @param preferredMatch - to set for this user
	 */
	void setPreferredMatch(PreferredMatch preferredMatch) {
		if(preferredMatch != null)
			this.preferredMatch = preferredMatch;
		else
			this.preferredMatch = PreferredMatch.BETTER;
	}
	
	/**
	 * Current preference for sorting matches. Defaults to BETTER
	 * @return preferred match by this user
	 */
	PreferredMatch getPreferredMatch() {
		return this.preferredMatch;
	}
	
	
	
}
