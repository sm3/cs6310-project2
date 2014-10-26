package EarthSim;

public class SunRepresentation {
	
	
	
	static long sunLocation = 0;
	static final double sunHeatOutput = 288; 
	static final double sunHeatOutputPerHour = 4; 

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
		
		return sunHeatOutputPerHour * timeOffset * Math.cos(Math.toRadians(cell.getCenterLatitude() )) * longitudeDiffCosine;
		
	}
	

	
	public static double calculateTemperatureDueToCooling(GridCell cell, EarthRepresentation earthRepresentation)
	{

		double cellTemperature = cell.getTemp();
		double averageCellTemperature = earthRepresentation.getAverageTemperature();
		
		double relativeTemperatureFactor = cellTemperature/averageCellTemperature;
		
		double timeOffset = ((double)earthRepresentation.getTimeInterval() )/ 60.0 ;
		//total attenuation for a sunfacing cells is approx (number of cells /2) * Cos(66)
		//double attenuationSum = earthRepresentation.getNumberOfCells() / 2.0 * .406867508241966;
		//to simplify the calculations - and since we will divide by number of cells to get average anyway, we use this constant
//		double totalheat = sunHeatOutput1 * attenuationConstant * timeOffset; 
		double attenuationConstant = .406867508241966 /2.0;
		
		double temperatureFromCooling = -1.0 * sunHeatOutputPerHour * attenuationConstant * timeOffset  * relativeTemperatureFactor ;
		
		
		return temperatureFromCooling;
		
	}
	
	

}
