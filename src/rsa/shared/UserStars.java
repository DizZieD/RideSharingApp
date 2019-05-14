package rsa.shared;

public enum UserStars{
	/*** Great ride*/
	FIVE_STARS,
	/*** Good ride*/
	FOUR_STARS,
	/*** Average ride*/
	THREE_STARS,
	/*** Bad ride*/
	TWO_STARS,
	/*** Lousy ride*/
	ONE_STAR;
	int stars;

	/**
	 * The number of stars
	 * @return the number of stars
	 */
	public int getStars() {
		switch(this) {
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
		return stars;
	}
	
	
}
