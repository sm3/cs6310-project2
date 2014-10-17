/**
 * Created by Subha Melapalayam
 */
package EarthSim;

public class HeatedEarthSimulation implements Runnable
{
	GridCell[][] gridcellsSurface1;
	
	GridCell[][] gridcellsSurface2;
	
	int timeInterval=0;
	EarthRepresentation earthRepresentation;
	GridCell gc;
	private boolean running; //copied this from TestSimulator
	
	public HeatedEarthSimulation(int gs, int interval)
	{
		 
		 timeInterval = interval;
		 earthRepresentation = new EarthRepresentation(gs);
		 gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		 
		
	}
	
	

	//Initialize GridCells.
	public void Initialize()
	{
	
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{
				 //gc = gridcells[i][j];
				gridcellsSurface1[i][j].setLatitude(earthRepresentation.getOriginLatitude(i));
				gridcellsSurface1[i][j].setLongtitude(earthRepresentation.getOriginLongtitude(j));
				gridcellsSurface1[i][j].setLt(earthRepresentation.calcCTop(i));
				gridcellsSurface1[i][j].setLb(earthRepresentation.calcCBase(i));
				gridcellsSurface1[i][j].setLv(earthRepresentation.calcCVerticalSide());
				gridcellsSurface1[i][j].setArea(earthRepresentation.calcCArea(i));
				gridcellsSurface1[i][j].setPerimeter(earthRepresentation.calcCPerimeter(i));
				gridcellsSurface1[i][j].setH(earthRepresentation.calcCHeight(i));
				gridcellsSurface1[i][j].setProportion(earthRepresentation.calcCSurfaceArea(i));
				gridcellsSurface1[i][j].setxCoordinate(earthRepresentation.getXCoordinate(j));
				gridcellsSurface1[i][j].setyCoordinate(earthRepresentation.getYCoordinate(i));
				 
				 //TODO: set previous temperature of the cell, current temperature, and neighbors.
				
				gridcellsSurface1[i][j].setNeighbors(gridcellsSurface1);
				
				 
			}
		}
		
		

	}
	 
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		
	}
	
	
	private void diffuse(GridCell[][] grid1, GridCell[][] grid2)
	{
		
		
		
		
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	
}