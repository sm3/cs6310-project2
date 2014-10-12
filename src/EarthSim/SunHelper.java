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
	
	public double calculateTemperatureDueToSun(double latitude, double longitude)
	{
		
		return 288 * Math.cos(latitude) * Math.cos(longitude);
		
	}
	

	
	public double calculateTemperatureDueToCooling()
	{
		double area = 0;
		double numberOfCells = 0;
		double averageCellSize = area / numberOfCells; 
		
		double actualCellSize = 0;
		
		double relativeSizeFactor = actualCellSize / averageCellSize;
		
		double cellTemperature = 0;
		double averageCellTemperature = 0;
		
		double relativeTemperatureFactor = cellTemperature/averageCellTemperature;
		
		double temperatureFromCooling = relativeSizeFactor*-1  * relativeTemperatureFactor * averageCellTemperature; // might be 288 instead of avg cell temp
		
		
		
		
		return 0;
		
	}
	
	
	public double calculateCellTemperature(GridCell cell)
	{
		
		double initialTemperature = cell.getTemp();
		double temperatureDueToSun = 0;
		double temperatureDueToCooling = 0;
		double temperatureOfNeighbors = 0;
		
		
		
		double cellTemperature = initialTemperature + calculateTemperatureDueToSun(cell.getLatitude(), cell.getLongtitude()) + calculateTemperatureDueToCooling() + temperatureOfNeighbors;
		
		return cellTemperature;
	}
	
	

}
