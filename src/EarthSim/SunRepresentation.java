package EarthSim;

public class SunRepresentation {
	
	
	
	static long sunLocation = 0;
	static final double sunHeatOutput = 288; //TODO - Kelly reset to 278?
	static final double sunHeatOutput1 = 1; //TODO - Kelly reset to 278?
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
	
	public static double calculateTemperatureDueToSun(GridCell cell, EarthRepresentation earthRepresentation)
	{
		
		///----c165----*195-----x210-------
		double longitudeDiffCosine = 0;
				
		longitudeDiffCosine = Math.cos(Math.toRadians(sunLocation - cell.getLongtitude()));
		
		//if the cosine is negative, then the difference is greater than 90 degrees, 
		//meaning cell is on dark side - so we will multiply sunHeat by zero
		if (longitudeDiffCosine < 0)
			longitudeDiffCosine = 0;
		
		double timeOffset = ((double)earthRepresentation.getTimeInterval() )/ 60.0 ;
		
		return sunHeatOutput1 * timeOffset * Math.cos(Math.toRadians(cell.getCenterLatitude() )) * longitudeDiffCosine;
		
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
		
//		double temperatureFromCooling = relativeSizeFactor*-1  * relativeTemperatureFactor * sunHeatOutput; // might be 288 instead of avg cell temp
		
		double timeOffset = ((double)earthRepresentation.getTimeInterval() )/ 60.0 ;
		double totalheat = sunHeatOutput1 * earthRepresentation.getNumberOfCells() / 2.0 * .406867508241966 * timeOffset;
		double temperatureFromCooling = -1.0 * totalheat / earthRepresentation.getNumberOfCells() * relativeSizeFactor * relativeTemperatureFactor ;
		
		
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
