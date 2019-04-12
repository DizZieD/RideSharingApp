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
		return stars;
	}
	
	
}
