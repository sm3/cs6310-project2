package EarthSim;
/**
* Created by Subha Melapalayam 
* Date : 10/6/2014
*/


public class HeatedEarthHelper
{
	
	
	private static int eradius = 6371000; // in meters
	private static double esurfaceArea = 5.10072E14;
	private static double ecircumference =40030140;
	private static double eAreaVisibleToSun = 2.55036E14;
	private int gs;
	private int p; //proportion of equator used by one unit of gs
	
	
	//add method to get grid proportion to the equator
	
	public HeatedEarthHelper(int gridSpacing)
	{
		this.gs= gridSpacing;
		p= 360/gs;
	}
	public int getCols()
	{
		return 360/gs;
	}
	
	public int getRows()
	{
		return 180/gs;
	}
	
	public int getNumberOfCells()
	{
	   return getRows()*getCols();
	   
	}
	
	//Origin of a cell is the lower left hand corner. 
	public double getOriginLatitude(int row)
	{
		double oLat = (row-((180/gs)/2))*gs;
		return oLat;
		
	}
	
	
	public double getOriginLongtitude(int col)
	{
		double oLong = 360 - ((col+1)*gs);
		return oLong;
		
	}
	
	//calculate cell's vertical side lv
	public double calcCVerticalSide()
	{
		return ecircumference*p;
	}
	
	//calculate cell's base lb
	public double calcCBase(int row)
	{
		return Math.cos(getOriginLatitude(row))*calcCVerticalSide();
	}
	//calculate cell's top side
	public double calcCTop(int row)
	{
		return Math.cos(getOriginLatitude(row) +gs)*calcCVerticalSide();
	}
	
	//calculate altitude of the cell
	public double calcCHeight(int row)
	{
		double lv = calcCVerticalSide();
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		return Math.sqrt(Math.pow(lv,2) - (1/4)*Math.pow((lb - lt),2));
	}
	
	//perimeter of a cell
	public double calcCPerimeter(int row)
	{
		double lv = calcCVerticalSide();
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		return lt+lb+(2*lv);
	}
	
	//area of a cell
	public double calcCArea(int row)
	{
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		double h = calcCHeight(row);
		return (1/2)*(lt+lb)*h;
	}
	
	//proportion of earth's surface area taken by a cell
	public double calcCSurfaceArea(int row)
	{
		return calcCArea(row)/esurfaceArea;
	}
	
	public int getXCoordinate(int col)
	{
		int cols = getCols();
		return (int) ((col+(cols/2))%cols-((cols/2)-1)*(ecircumference/cols));
	}
	
	public int getYCoordinate(int row)
	{
		int rows = getRows();
		return (int) ((row-(rows/2))*(ecircumference/rows));
	}
	
	
	
	
	//Methods return Latitude - Longtitude mapping to Grid indices
	public double getGridRowIndex()
	{
		return 0;
		
	}
	
	//Identify cell location based on rotation.
	
	
	
	//TODO : Add code for solar heating, cooling , attenuation.
	
}
