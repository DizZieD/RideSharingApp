package rsa.shared;

import rsa.quad.HasPoint;

public class Location extends java.lang.Object implements HasPoint{
	double x;
	double y;
	
	/**
	 * Create a location with given X and Y
	 * @param x - coordinate of location
	 * @param y - coordinate of location
	 */
	public Location(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * The X coordinate of this location
	 * @return the X coordinate 
	 */
	public double getX() {
		return x;
	}

	/**
	 * The Y coordinate of this location
	 * @return the Y coordinate 
	 */
	public double getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
	
}