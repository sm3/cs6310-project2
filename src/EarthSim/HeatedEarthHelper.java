

/**
* Created by Subha Melapalayam 
* Date : 10/6/2014
*/

package EarthSim;

public class HeatedEarthHelper
{
	
	private int gridSpacing; //1 to 180- input provided by the user
	private int timeInterval=1; //1 to 1440 - input provided by the user
	private static int eradius = 6371000; // in meters
	private static double esurfaceArea = 5.10072E14;
	private static double ecircumference =40030140;
	private static double eAreaVisibleToSun = 2.55036E14;
	
	public int getGridSpacing()
	{
		//return largest integer input provided that evenly divides 180 degrees without any reminder.
		
		return 0;
		
	}
	
	//add method to get grid proportion to the equator
	
	public int getCols(int gs)
	{
		return 180/gs;
	}
	
	public int getRows(int gs)
	{
		return 180/gs;
	}
	
	public int getNumberOfCells(int gs)
	{
	   return getRows(gs)*getCols(gs);
	}
	
	
	
	
}
