package EarthSim;

public class EarthRepresentation {
	
	
	private static int eradius = 6371000; // in meters
	private static double esurfaceArea = 5.10072E14;
	private static double ecircumference =40030140;
	private static double eAreaVisibleToSun = 2.55036E14;
	private int gs;
	private int p; //proportion of equator used by one unit of gs
	
	private double averageTemperature = 288;
	
	//add method to get grid proportion to the equator
	
	double sunLocation = 0;
	
	public EarthRepresentation (int gridSpacing)
	{
		this.gs= gridSpacing;
		p = 360/gs;
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
	
	
	private double initialSpace = gs/2;
	
	//Origin of a cell is the lower left hand corner. 
	public double getOriginLatitude(int row)
	{
		// i = 0  gs = 15   expected lat = 82.5

		double initialSpace = gs/2;
		
		double oLat = 0;

		if (row < this.getRows())
		{
			oLat = 90 - (initialSpace + (gs* row) );
		}
		else
		{
			oLat = 90 - (initialSpace + (gs* (this.getRows() - row)) );
		}
	

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
	
	public double getArea()
	{
		return esurfaceArea;
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
	
	public void calculateAverageTemperature(GridCell [][] grid)
	{
		
		double totalCellTemp = 0; 
		
		for (int i =0; i<this.getRows(); i++)
		{
			
			for(int j=0; j<this.getCols(); j++)
			{		
				totalCellTemp = totalCellTemp + grid[i][j].getTemp();
			}
		}
		
		this.averageTemperature = totalCellTemp / this.getNumberOfCells();
		
	}
	
	public double getAverageTemperature()
	{
		return this.averageTemperature;
	}
	
	public double calculateCellTemperature(GridCell cell)
	{
		
		double initialTemperature = cell.getTemp();
		double temperatureDueToSun = SunRepresentation.calculateTemperatureDueToSun(cell);
		double temperatureDueToCooling = SunRepresentation.calculateTemperatureDueToCooling(cell, this);
		
		double temperateCooledPerHour = 23.16;
		double timePassed = 1;
		
		double percentageOfCooling = temperatureDueToCooling / initialTemperature;
		
		double actualCooling = percentageOfCooling * temperateCooledPerHour * timePassed;
		
		double temperatureOfNeighbors = cell.getNeighborsAverageTemp();
		
		double cellTemperature = initialTemperature;
		
		if ( temperatureDueToSun != 0.0 )
		{
			cellTemperature = (cellTemperature + temperatureDueToSun) / 2;
		}
		
		if (Double.isNaN(cellTemperature) )
		{
			
			System.out.println("nan sun ");
			
		}
		
		
		
		cellTemperature = cellTemperature + actualCooling;
			
		cellTemperature = (cellTemperature +  temperatureOfNeighbors) /2;

		if (Double.isNaN(cellTemperature) )
		{
			
			System.out.println("nan final ");
			
		}
		
		return cellTemperature;
	}

}
