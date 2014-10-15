package EarthSim;

public class SunHelper {
	
	
	
	long startCol = 0;
	long endCol = 180;
	/*
	 *     sun
	 *    /|  <------ theta 
	 * 	 / |	
	 *  /__|
	 *  ^
	 *  |
	 * cell    
	 * 
	 */
	
	public static double calculateTemperatureDueToSun(double latitude, double longitude)
	{
		
		return 288 * Math.cos(latitude) * Math.cos(longitude);
		
	}
	

	
	public static double calculateTemperatureDueToCooling()
	{
		double area = 0;
		double numberOfCells = 0;
		double averageCellSize = area / numberOfCells; 
		
		double actualCellSize = 0;
		
		double relativeSizeFactor = actualCellSize / averageCellSize;
		
		double cellTemperature = 0;
		double averageCellTemperature = 0;
		
		double relativeTemperatureFactor = cellTemperature/averageCellTemperature;
		
		double temperatureFromCooling = relativeSizeFactor*-1  * relativeTemperatureFactor * 278; // might be 288 instead of avg cell temp
		
		
		
		
		return temperatureFromCooling;
		
	}
	
	
	public double calculateCellTemperature(GridCell cell)
	{
		
		double initialTemperature = cell.getTemp();
		double temperatureDueToSun = calculateTemperatureDueToSun(cell.getLatitude(), cell.getLongtitude());
		double temperatureDueToCooling = calculateTemperatureDueToCooling();
		
		double temperateCooledPerHour = 23.16;
		double timePassed = 1;
		
		double percentageOfCooling = temperatureDueToCooling / initialTemperature;
		
		double actualCooling = percentageOfCooling * temperateCooledPerHour * timePassed;
		
		double temperatureOfNeighbors = 0;
		
		double cellTemperature = initialTemperature;
		
		if ( temperatureDueToSun != 0.0 )
		{
			cellTemperature = (cellTemperature + temperatureDueToSun) / 2;
		}
		
		
		cellTemperature = cellTemperature + actualCooling;
			
		cellTemperature = (cellTemperature +  temperatureOfNeighbors) /2;

		
		return cellTemperature;
	}
	
	

}
