/**
 * Created by Subha Melapalayam
 */
package EarthSim;

public class HeatedEarthSimulation implements Runnable
{
	double[][] gridcells;
	
	int timeInterval=0;
	HeatedEarthHelper util;
	
	public HeatedEarthSimulation(int gs, int interval)
	{
		 
		 timeInterval = interval;
		 util = new HeatedEarthHelper(gs);
		 gridcells = new double[util.getRows()][util.getCols()];
	}
	
	

	//Initialize GridCells.
	public void Initialize()
	{
		
	}
	 
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		
	}
	
}