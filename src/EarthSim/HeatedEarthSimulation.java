/**
 * Created by Subha Melapalayam
 */
package EarthSim;
import java.util.concurrent.BlockingQueue;

public class HeatedEarthSimulation implements Runnable
{
	GridCell[][] gridcellsSurface1;
	
	GridCell[][] gridcellsSurface2;
	private BlockingQueue<Message> queue;
	
	int timeInterval=0;
	EarthRepresentation earthRepresentation;
	GridCell gc;
	private boolean running; //copied this from TestSimulator
	
	
	public HeatedEarthSimulation(int gs, int interval, BlockingQueue<Message> queue)
	{
		 this.queue=queue;
		 
		 timeInterval = interval;
		 earthRepresentation = new EarthRepresentation(gs);
		 gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		 gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
	}
	
	

	//Initialize GridCells.
	public void Initialize()
	{
	
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				setGridData(gridcellsSurface1, i, j );
				setGridData(gridcellsSurface2, i, j );	 
			}
		}
		
		

	}
	
	private void setGridData(GridCell [][] grid, int i, int j)
	{
		 GridCell cell = grid[i][j];
		
		
		cell.setLatitude(earthRepresentation.getOriginLatitude(i));
		cell.setLongtitude(earthRepresentation.getOriginLongtitude(j));
		cell.setLt(earthRepresentation.calcCTop(i));
		cell.setLb(earthRepresentation.calcCBase(i));
		cell.setLv(earthRepresentation.calcCVerticalSide());
		cell.setArea(earthRepresentation.calcCArea(i));
		cell.setPerimeter(earthRepresentation.calcCPerimeter(i));
		cell.setH(earthRepresentation.calcCHeight(i));
		cell.setProportion(earthRepresentation.calcCSurfaceArea(i));
		cell.setxCoordinate(earthRepresentation.getXCoordinate(j));
		cell.setyCoordinate(earthRepresentation.getYCoordinate(i));
		 
		 //TODO: set previous temperature of the cell, current temperature, and neighbors.
		
		cell.setNeighbors(grid);
		
	}
	 
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		
		while(running){
			
		//queue.put(new Message(grid,sunsLongitude));
		}
		
	}
	
	
	private void diffuse(GridCell[][] grid1, GridCell[][] grid2)
	{
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		

				grid2[i][j].setTemp(earthRepresentation.calculateCellTemperature(grid1[i][j]));
				
			}
		}
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	
}