package rsa.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import rsa.shared.RideSharingAppException;

public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	private static File managerFile = new File("./users.ser");
	
	private static Users allUsers = null;
	
	private static Map<String, User> users;
	
	/**
	 * Create a empty of Users
	 */
	private Users() {
		Users.users = new HashMap<String, User>();
	}
	
	/**
	 * Restores a saved in serialized file
	 * @throws RideSharingAppException - if I/O error occurs reading serialization
	 */
	private static void restore() throws RideSharingAppException {
		try {
			InputStream inStream;
			inStream = new FileInputStream(managerFile);
	        ObjectInputStream fileObjectIn = new ObjectInputStream(inStream);
	        allUsers = (Users) fileObjectIn.readObject();
	        fileObjectIn.close();
	        inStream.close();
		} catch (IOException | ClassNotFoundException e) {
			throw new RideSharingAppException(e);
		}
	}
	
	/**
	 * Saves Users to a serializable file
	 * @throws RideSharingAppException - if I/O error occurs reading serialization
	 */
	private static void backup() throws RideSharingAppException {
		try {
			OutputStream outStream = new FileOutputStream(managerFile);
	        ObjectOutputStream fileObjectOut = new ObjectOutputStream(outStream);
	        fileObjectOut.writeObject(allUsers);
	        fileObjectOut.close();
	        outStream.close();
		}
		catch(IOException e) { 
			throw new RideSharingAppException(e);
		}
	}
	
	/**
	 * Returns the single instance of this class as proposed in the singleton design pattern. If a backup of this class is available then the users instance is recreated from that data
	 * @return instance of this class
	 * @throws RideSharingAppException - if I/O error occurs reading serialization
	 */
	public static Users getInstance() throws RideSharingAppException {
		if(allUsers == null) {
			if(managerFile == null || managerFile.length() == 0)
				allUsers = new Users();
			else
				restore();
		}
		return allUsers;
	}
	
	/**
	 * Resets singleton for unit testing purposes.
	 */
	void reset() {
		allUsers = new Users();
		managerFile.delete();
	}
	
	/**
	 * Name of file containing managers's data
	 * @return file containing serialization
	 */
	public static File getPlayersFile() {
		return managerFile;
	}
	
	/**
	 * Change pathname of file containing mnanager's data
	 * @param managerFile - contain serialization
	 */
	public static void setPlayersFile(File managerFile) {
		Users.managerFile = managerFile;
	}
	
	/**
	 * Register a player with given nick and password. Changes are immediately serialized.
	 * @param nick  - of user
	 * @param name - of user
	 * @param password - id user
	 * @return true if registered and false otherwise
	 * @throws RideSharingAppException - on I/O error in serialization
	 */
	boolean register(String nick, String name, String password) throws RideSharingAppException {
		if(!nick.matches("\\S+") || Users.users.get(nick) != null)
			return false;
		
		User user = new User(nick, name, password);
		Users.users.put(nick, user);
		
		backup();
		
        return true;
	}
	
	/**
	 * Change password if old password matches current one. Changes are immediately serialized.
	 * @param nick - of player
	 * @param oldPassword - for authentication before update
	 * @param newPassword - after update
	 * @return true if password changed and false otherwise
	 * @throws RideSharingAppException - on I/O error in serialization.
	 */
	boolean updatePassword(String nick, String oldPassword, String newPassword) throws RideSharingAppException {
		if(authenticate(nick, oldPassword)) {
			getUser(nick).setPassword(newPassword);
			
			backup();
			
			return true;
		}
		return false;
	}
	
	/**
	 * Authenticate user given id and password
	 * @param nick - of user to authenticate
	 * @param password - of user to authenticate
	 * @return true if authenticated and false otherwise
	 */
	boolean authenticate(String nick, String password) {
		User user = getUser(nick);
		if (user != null) {
			return user.authenticate(password);
		}
		return false;
	}
	
	/**
	 * Get the user with given nick
	 * @param nick - of player
	 * @return player instance
	 */
	public User getUser(String nick) {
		return Users.users.get(nick);
	}
}
