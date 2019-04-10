package rsa.service;

import java.util.HashMap;
import java.util.Map;
import rsa.shared.Car;

public class User extends java.lang.Object implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	String nick;
	String password;
	String name;
	
	//Map<K,V> rides;
	//Map<K,V> stars;
	Map<String,Car> cars;

	/**
	 * Create a user with given features
	 * @param nick - nick of user
	 * @param password - password
	 * @param name - name of user
	 */
	public User(String nick, String password, String name) {
		super();
		this.nick = nick;
		this.password = password;
		this.name = name;
		this.cars = new HashMap<String, Car>();
	}
	
	/**
	 * Bind a car to this user. Can be used to change car features.
	 * @param car - to add
	 */
	public void addCar(Car car) {
		cars.put(car.getPlate(), car);
	}
	
	//public void addStars(UserStars moreStars, RideRole role)
	
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
	
	//void setPreferredMatch(PreferredMatch preferredMatch)
	
	//PreferredMatch getPreferredMatch()
	
	
	
}
