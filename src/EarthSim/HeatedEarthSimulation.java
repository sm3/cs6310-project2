/**
 * Created by Subha Melapalayam
 */
package EarthSim;

public class HeatedEarthSimulation implements Runnable
{
	GridCell[][] gridcells;
	
	int timeInterval=0;
	HeatedEarthHelper util;
	GridCell gc;
	private boolean running; //copied this from TestSimulator
	
	public HeatedEarthSimulation(int gs, int interval)
	{
		 
		 timeInterval = interval;
		 util = new HeatedEarthHelper(gs);
		 gridcells = new GridCell[util.getRows()][util.getCols()];
		 
		
	}
	
	

	//Initialize GridCells.
	public void Initialize()
	{
	
		for (int i =0; i<util.getRows(); i++)
		{
			for(int j=0; j<util.getCols(); j++)
			{
				 //gc = gridcells[i][j];
				 gridcells[i][j].setLatitude(util.getOriginLatitude(i));
				 gridcells[i][j].setLongtitude(util.getOriginLongtitude(j));
				 gridcells[i][j].setLt(util.calcCTop(i));
				 gridcells[i][j].setLb(util.calcCBase(i));
				 gridcells[i][j].setLv(util.calcCVerticalSide());
				 gridcells[i][j].setArea(util.calcCArea(i));
				 gridcells[i][j].setPerimeter(util.calcCPerimeter(i));
				 gridcells[i][j].setH(util.calcCHeight(i));
				 gridcells[i][j].setProportion(util.calcCSurfaceArea(i));
				 gridcells[i][j].setxCoordinate(util.getXCoordinate(j));
				 gridcells[i][j].setyCoordinate(util.getYCoordinate(i));
				 
				 //TODO: set previous temperature of the cell, current temperature, and neighbors.
				 
			}
		}
	}
	 
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	
}