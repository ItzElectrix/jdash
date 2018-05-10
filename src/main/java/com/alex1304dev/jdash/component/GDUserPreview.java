package com.alex1304dev.jdash.component;

/**
 * Represents a user preview when searching for them in Geometry Dash.
 * 
 * @author Alex1304
 */
public class GDUserPreview implements GDComponent {
	
	private long accountID;
	private long playerID;
	private String name;
	private int stars;
	private int secretCoins;
	private int userCoins;
	private int demons;
	private int creatorPoints;

	/**
	 * @param name
	 *            - user's nickname
	 * @param playerID
	 *            - user's player ID
	 * @param secretCoins
	 *            - user's secret coins
	 * @param userCoins
	 *            - user's user coins
	 * @param stars
	 *            - user's stars
	 * @param diamonds
	 *            - user's diamonds
	 * @param demons
	 *            - user's demons
	 * @param creatorPoints
	 *            - user's creator points
	 * @param accountID
	 *            - user's account ID
	 */
	public GDUserPreview(String name, long playerID, int secretCoins, int userCoins, int stars, int demons,
			int creatorPoints, long accountID) {
		this.name = name;
		this.playerID = playerID;
		this.secretCoins = secretCoins;
		this.userCoins = userCoins;
		this.stars = stars;
		this.demons = demons;
		this.creatorPoints = creatorPoints;
		this.accountID = accountID;
	}
	

	/**
	 * Gets the user's nickname
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the user's player ID
	 * 
	 * @return long
	 */
	public long getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the user's secret coins
	 * 
	 * @return int
	 */
	public int getSecretCoins() {
		return secretCoins;
	}

	/**
	 * Gets the user's user coins
	 * 
	 * @return int
	 */
	public int getUserCoins() {
		return userCoins;
	}

	/**
	 * Gets the user's stars
	 * 
	 * @return int
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * Gets the user's demons
	 * 
	 * @return int
	 */
	public int getDemons() {
		return demons;
	}

	/**
	 * Gets the user's creator points
	 * 
	 * @return int
	 */
	public int getCreatorPoints() {
		return creatorPoints;
	}

	/**
	 * Gets the user's account ID
	 * 
	 * @return long
	 */
	public long getAccountID() {
		return accountID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountID ^ (accountID >>> 32));
		return result;
	}
	
	/**
	 * Two users are equal if and only if they have the same accountID.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GDUserPreview))
			return false;
		GDUserPreview other = (GDUserPreview) obj;
		if (accountID != other.accountID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GDUserPreview [accountID=" + accountID + ", playerID=" + playerID + ", name=" + name + ", stars=" + stars
				+ ", secretCoins=" + secretCoins + ", userCoins=" + userCoins + ", demons="
				+ demons + ", creatorPoints=" + creatorPoints + "]";
	}
}