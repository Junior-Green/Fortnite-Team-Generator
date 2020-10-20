package application;

public class Player implements Comparable<Player>{
	private String last, first, fullName, tier;
	private int rating;
	public final static int SORT_BY_NAME = 0, SORT_BY_RATING = 1;
	public static int sort;
	
	public Player(){} // no-arg constructor
	
	//Player constructor that sets all class fields
	public Player(String fName, String lName, int rate, String tier)
	{
		last = lName;
		first = fName;
		rating = rate;
		fullName = fName + " " +lName;
		this.tier = tier;
	}
	
	/*Method that returns the String first which represents Player's first name
	 * 
	 * @return the Player's first name
	 */
	public String getFirstName()
	{
		return first;
	}
	/*Method that returns the String flast which represents Player's last name
	 * 
	 * @return the Player's last name
	 */
	public String getLastName()
	{
		return last;
	}
	/*Method that returns the String fullName which represents Player's full name (first and last)
	 * 
	 * @return the Player's first name
	 */
	public String getName()
	{
		return fullName;
	}
	/*Method that returns the int rating which represents Player's rating
	 * 
	 * @return an int which represents the Player's rating
	 */
	public int getRating()
	{
		return rating;
	}
	/*Method that returns the String first which represents Player's tier 
	 * 
	 * @return the Player's tier rating which corresponds to the users rating
	 */
	public String getTier()
	{
		return tier;
	}
	/*Mutator which changes Player's first name
	 * 
	 * @param A string to what you want to change the first name to
	 * @return void
	 */
	public void setFirstName(String fName)
	{
		first = fName;
	}
	/*Mutator which changes Player's last name
	 * 
	 * @param A string to what you want to change the last name to
	 * @return void
	 */
	public void setLastName(String lName)
	{
		last = lName;
	}
	/*Mutator which changes Player's first and last name
	 * 
	 * @param Two string which represents the first and last names that are being changed
	 * @return void
	 */
	public void setName(String fName, String lName)
	{
		first = fName;
		last = lName;
		fullName = first + " " + last;
	}
	/*Mutator which changes Player's rating
	 * 
	 * @param takes an int to which you want to change the Player's
	 * rating to
	 * 
	 * @return void
	 */
	public void setRating(int rating)
	{
		this.rating = rating;
	}
	/*Mutator which changes Player's skill tier
	 * 
	 * @param takes a String to what you want to change 
	 * the tier to
	 * 
	 * @return void
	 */
	public void setTier(String tier)
	{
		this.tier = tier;
	}
	/*Method which takes the users full name, rating and tier
	 * and formats it for it to be displayed on to the List View
	 * 
	 * @return a formatted string to be displayed on to the List view
	 */
	public String toString()
	{
		return String.format("%-30s%-10s%-10s", fullName, rating, tier);
	}
	/*An overloaded method from the Comparable class's compareTo method
	 * which is manually set to order the Player objects numerically based on
	 * the ratings or alphabetically based on the full names
	 * 
	 * @param Takes a Player object which is used to compare to the object 
	 * calling the method
	 * 
	 * @return return and int based on whether the object being compared is larger or
	 * smaller than the object that called this method
	 */
	
	public int compareTo(Player s)
	{
	
		int comparison = 0;
		if(sort == 0)
			 comparison = fullName.compareToIgnoreCase(s.getName());
		else if(sort == 1)
		{
			 comparison = Integer.compare(rating, s.getRating());
			 
			 if(comparison == 0)
				 comparison = s.getName().compareToIgnoreCase(fullName);
		}
		return comparison;
	}
	/*When saving to the comma delimited file this method gets all the
	 * Player's info and formats it for it to be written to the file
	 * 
	 * @return The formatted string
	 */
	public String toFile()
	{
		return last + "," + first + "," + rating + "," + tier; 
	}
}
