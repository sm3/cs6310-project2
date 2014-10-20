package EarthSim;

public class SunRepresentation {
	
	
	
	static long sunLocation = 0;
	static final double sunHeatOutput = 278;
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
	
	public static double calculateTemperatureDueToSun(GridCell cell)
	{
		
		///----c165----*195-----x210-------
		
		double cellLongitudeFromSun = 0;
				
		if ( Math.abs (sunLocation - Math.abs( cell.getLongtitude())) >= 90)
		{
			cellLongitudeFromSun = 90;
		}
		else
		{
			cellLongitudeFromSun = Math.abs(sunLocation - cell.getLongtitude());
		}
		
		return sunHeatOutput * Math.cos(cell.getLatitude()) * Math.cos(cellLongitudeFromSun);
		
	}
	

	
	public static double calculateTemperatureDueToCooling(GridCell cell, EarthRepresentation earthRepresentation)
	{
		double area = earthRepresentation.getArea();  // earth area
		long numberOfCells = earthRepresentation.getNumberOfCells();    // number of cells in the earth
		double averageCellSize = area / numberOfCells; 
		
		double actualCellSize = cell.getArea();
		
		double relativeSizeFactor = actualCellSize / averageCellSize;
		
		double cellTemperature = cell.getTemp();
		double averageCellTemperature = earthRepresentation.getAverageTemperature();
		
		double relativeTemperatureFactor = cellTemperature/averageCellTemperature;
		
		double temperatureFromCooling = relativeSizeFactor*-1  * relativeTemperatureFactor * sunHeatOutput; // might be 288 instead of avg cell temp
		
		
		
		
		return temperatureFromCooling;
		
	}
	
	
//	public static double calculateCellTemperature(GridCell cell)
//	{
//		
//		double initialTemperature = cell.getTemp();
//		double temperatureDueToSun = calculateTemperatureDueToSun(cell);
//		double temperatureDueToCooling = calculateTemperatureDueToCooling(cell);
//		
//		double temperateCooledPerHour = 23.16;
//		double timePassed = 1;
//		
//		double percentageOfCooling = temperatureDueToCooling / initialTemperature;
//		
//		double actualCooling = percentageOfCooling * temperateCooledPerHour * timePassed;
//		
//		double temperatureOfNeighbors = 0;
//		
//		double cellTemperature = initialTemperature;
//		
//		if ( temperatureDueToSun != 0.0 )
//		{
//			cellTemperature = (cellTemperature + temperatureDueToSun) / 2;
//		}
//		
//		
//		cellTemperature = cellTemperature + actualCooling;
//			
//		cellTemperature = (cellTemperature +  temperatureOfNeighbors) /2;
//
//		
//		return cellTemperature;
//	}
//	
	

}
