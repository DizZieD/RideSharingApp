package rsa.shared;


public class Location extends java.lang.Object implements HasPoint{
	
	Location(double x, double y){
		this.x = x ;
		this.y = y ;

	}

	public double getX(){

		return x;
	}

	public double getY(){

		return y;
	}

	public int hashCode(){


	}

	public boolean equals(java.lang.Object obj){
		if(this.x == obj.x && this.y == obj.y){
			return true;
		}
		else 
			return false; 

	}

}